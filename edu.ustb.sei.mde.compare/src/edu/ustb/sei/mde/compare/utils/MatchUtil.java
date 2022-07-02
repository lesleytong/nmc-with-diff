/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Michael Borkowski - bug 467576
 *******************************************************************************/
package edu.ustb.sei.mde.compare.utils;

import static com.google.common.base.Predicates.and;
import static edu.ustb.sei.mde.compare.DifferenceKind.ADD;
import static edu.ustb.sei.mde.compare.DifferenceKind.DELETE;
import static edu.ustb.sei.mde.compare.DifferenceSource.LEFT;
import static edu.ustb.sei.mde.compare.DifferenceSource.RIGHT;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.ofKind;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.onFeature;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.valueIs;
import static edu.ustb.sei.mde.compare.utils.ReferenceUtil.getAsList;

import com.google.common.collect.Iterables;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import edu.ustb.sei.mde.compare.AttributeChange;
import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.DifferenceSource;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.util.CompareSwitch;

/**
 * This utility class holds methods that will be used by the diff and merge processes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class MatchUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private MatchUtil() {
		// Hides default constructor
	}

	/**
	 * Get the object which is the origin value from the given matching <code>object</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param object
	 *            The given object.
	 * @return The origin value.
	 */
	public static EObject getOriginObject(Comparison comparison, EObject object) {
		EObject result = null;
		Match match = comparison.getMatch(object);
		if (match != null) {
			if (comparison.isThreeWay()) {
				result = match.getOrigin();
			} else {
				if (object == match.getLeft()) {
					result = match.getRight();
				} else {
					result = match.getLeft();
				}
			}
		}
		return result;
	}

	/**
	 * This will be used whenever we check for conflictual MOVEs in order to determine whether we have a
	 * pseudo conflict or a real conflict.
	 * <p>
	 * Namely, this will retrieve the value of the given {@code feature} on the right and left sides of the
	 * given {@code match}, then check whether the two given values are on the same index.
	 * </p>
	 * <p>
	 * Note that no sanity checks will be made on either the match's sides or the feature.
	 * </p>
	 * 
	 * @param match
	 *            Match for which we need to check a feature.
	 * @param feature
	 *            The feature which values we need to check.
	 * @param value1
	 *            First of the two values which index we are to compare.
	 * @param value2
	 *            Second of the two values which index we are to compare.
	 * @return {@code true} if the two given values are located at the same index in the given feature's
	 *         values list, {@code false} otherwise.
	 * @since 3.4
	 */
	public static boolean matchingIndices(Match match, EStructuralFeature feature, Object value1,
			Object value2) {
		boolean matching = false;
		if (feature.isMany()) {
			// FIXME the detection _will_ fail for non-unique lists with multiple identical values...
			int leftIndex = computeIndex(match, feature, value1, LEFT);
			int rightIndex = computeIndex(match, feature, value2, RIGHT);
			matching = leftIndex == rightIndex;
		} else {
			matching = true;
		}
		return matching;
	}

	/**
	 * Compute the index of an object in the list of elements of a given match+feature on a given side. This
	 * index is computed without taking objects that have a diff into account, except if this diff is an ADD.
	 * 
	 * @param match
	 *            The match
	 * @param feature
	 *            The structural feature
	 * @param value
	 *            The object the index of which must be computed
	 * @param side
	 *            The side on which to compute the index
	 * @return The index of the given object.
	 * @since 3.4
	 */
	public static int computeIndex(Match match, EStructuralFeature feature, Object value,
			DifferenceSource side) {
		Comparison comparison = match.getComparison();
		int result = -1;
		@SuppressWarnings("unchecked")
		final List<Object> sideValues = (List<Object>)ReferenceUtil
				.safeEGet(MatchUtil.getMatchedObject(match, side), feature);
		for (int i = 0; i < sideValues.size(); i++) {
			final Object sideObject = sideValues.get(i);
			if (comparison.getEqualityHelper().matchingValues(sideObject, value)) {
				break;
			} else if ((hasDiff(match, feature, sideObject) && match.getOrigin() != null)
					|| hasDeleteDiff(match, feature, sideObject)) {
				// Do not increment.
			} else {
				result++;
			}
		}
		return result;
	}

	/**
	 * Checks whether the given {@code value} has been deleted from the given {@code feature} of {@code match}
	 * .
	 * 
	 * @param match
	 *            The match which differences we'll check.
	 * @param feature
	 *            The feature on which we expect a difference.
	 * @param value
	 *            The value we expect to have been removed from {@code feature}.
	 * @return <code>true</code> if there is such a Diff on {@code match}, <code>false</code> otherwise.
	 * @since 3.4
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasDeleteDiff(Match match, EStructuralFeature feature, Object value) {
		Comparison comparison = match.getComparison();
		final Object expectedValue;
		if (value instanceof EObject && comparison.isThreeWay()) {
			final Match valueMatch = comparison.getMatch((EObject)value);
			if (valueMatch != null) {
				expectedValue = valueMatch.getOrigin();
			} else {
				expectedValue = value;
			}
		} else {
			expectedValue = value;
		}
		return Iterables.any(match.getDifferences(),
				and(onFeature(feature.getName()), valueIs(expectedValue), ofKind(DELETE)));
	}

	/**
	 * Checks whether the given {@code match} presents a difference of any kind on the given {@code feature}'s
	 * {@code value}.
	 * 
	 * @param match
	 *            The match which differences we'll check.
	 * @param feature
	 *            The feature on which we expect a difference.
	 * @param value
	 *            The value we expect to have changed inside {@code feature}.
	 * @return <code>true</code> if there is such a Diff on {@code match}, <code>false</code> otherwise.
	 * @since 3.4
	 */
	public static boolean hasDiff(Match match, EStructuralFeature feature, Object value) {
		return Iterables.any(match.getDifferences(), and(onFeature(feature.getName()), valueIs(value)));
	}

	/**
	 * From a given mono-valued reference change, get the origin value.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The given reference change.
	 * @return The origin value.
	 */
	public static EObject getOriginValue(Comparison comparison, ReferenceChange difference) {
		final EReference reference = difference.getReference();
		if (!reference.isContainment() && !reference.isMany()
				&& difference.getKind().equals(DifferenceKind.CHANGE)) {
			EObject originContainer = getOriginContainer(comparison, difference);
			if (originContainer != null) {
				Object originValue = ReferenceUtil.safeEGet(originContainer, reference);
				if (originValue instanceof EObject) {
					return (EObject)originValue;
				}
			}
		}
		return null;
	}

	/**
	 * Get the business model object containing the given <code>difference</code> in the origin side.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The difference.
	 * @return The object.
	 */
	public static EObject getOriginContainer(Comparison comparison, Diff difference) {
		final EObject diffContainer;
		if (comparison.isThreeWay()) {
			diffContainer = difference.getMatch().getOrigin();
		} else {
			if (getContainer(comparison, difference) == difference.getMatch().getLeft()) {
				diffContainer = difference.getMatch().getRight();
			} else {
				diffContainer = difference.getMatch().getLeft();
			}
		}
		return diffContainer;
	}

	/**
	 * Get the business model object containing the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @param difference
	 *            The difference.
	 * @return The object.
	 */
	public static EObject getContainer(Comparison comparison, Diff difference) {
		EObject result = null;
		Match match = difference.getMatch();
		final DifferenceSource source = difference.getSource();
		final DifferenceKind kind = difference.getKind();
		switch (kind) {
			case DELETE:
				if (comparison.isThreeWay()) {
					result = match.getOrigin();
				} else {
					result = match.getRight();
				}
				break;
			case ADD:
				// fall through
			case MOVE:
				if (source == DifferenceSource.LEFT) {
					result = match.getLeft();
				} else {
					result = match.getRight();
				}
				break;
			case CHANGE:
				final Object value = getValue(difference);
				final EStructuralFeature feature = getStructuralFeature(difference);
				if (value == null || feature == null) {
					// TODO ?
					throw new IllegalArgumentException();
				}
				if (source == DifferenceSource.LEFT) {
					if (featureContains(match.getLeft(), feature, value)) {
						result = match.getLeft();
					} else if (comparison.isThreeWay()) {
						result = match.getOrigin();
					} else {
						result = match.getRight();
					}
				} else {
					if (featureContains(match.getRight(), feature, value)) {
						result = match.getRight();
					} else if (comparison.isThreeWay()) {
						result = match.getOrigin();
					} else {
						// Cannot happen ... for now
						result = match.getLeft();
					}
				}
				break;
			default:
				// no other case for now.
		}
		return result;
	}

	/**
	 * Determines whether the given feature of the given {@link EObject} contains the provided value, while
	 * correctly handling proxies (in other words, in case of proxies, the proxy URI is compared instead of
	 * the objects, which would otherwise lead to false negatives).
	 * 
	 * @param eObject
	 *            The object of which a feature is to be checked
	 * @param feature
	 *            The feature of which containment is to be checked
	 * @param value
	 *            The value which is to be verified in the feature
	 * @return <code>true</code> if the feature contains the given value
	 */
	// public for testing
	public static boolean featureContains(EObject eObject, EStructuralFeature feature, Object value) {
		boolean contains = false;

		// only compute the value's URI once, and only if needed
		URI valueURI = null;

		for (Object element : getAsList(eObject, feature)) {
			if (element == value) {
				contains = true;
				break;
			}
			if (element != null && element.equals(value)) {
				contains = true;
				break;
			}

			if (element instanceof EObject && ((EObject)element).eIsProxy()) {
				if (valueURI == null && value instanceof EObject) {
					valueURI = EcoreUtil.getURI((EObject)value);
				}
				if (EcoreUtil.getURI((EObject)element).equals(valueURI)) {
					contains = true;
					break;
				}
			}
		}

		return contains;
	}

	/**
	 * Get the value of any difference.
	 * 
	 * @param input
	 *            The difference.
	 * @return the value of the difference.
	 */
	public static Object getValue(Diff input) {
		final CompareSwitch<Object> customSwitch = new CompareSwitch<Object>() {
			@Override
			public Object caseAttributeChange(AttributeChange object) {
				return object.getValue();
			}

			@Override
			public Object caseReferenceChange(ReferenceChange object) {
				return object.getValue();
			}

		};
		return customSwitch.doSwitch(input);
	}

	/**
	 * Get the structural feature of any difference.
	 * 
	 * @param input
	 *            The difference.
	 * @return the structural feature.
	 */
	public static EStructuralFeature getStructuralFeature(Diff input) {
		final CompareSwitch<EStructuralFeature> customSwitch = new CompareSwitch<EStructuralFeature>() {
			@Override
			public EStructuralFeature caseAttributeChange(AttributeChange object) {
				return object.getAttribute();
			}

			@Override
			public EStructuralFeature caseReferenceChange(ReferenceChange object) {
				return object.getReference();
			}

		};
		return customSwitch.doSwitch(input);
	}

	/**
	 * Get the object matched by a Match on a given side.
	 * 
	 * @param m
	 *            The match, must not be <code>null</code>
	 * @param side
	 *            The side for which we want the matched value, use <code>null</code> for ORIGIN.
	 * @return The value matched by this match on the given side.
	 * @since 3.4
	 */
	public static EObject getMatchedObject(Match m, DifferenceSource side) {
		if (side == null) {
			return m.getOrigin();
		}
		switch (side) {
			case LEFT:
				return m.getLeft();
			case RIGHT:
				return m.getRight();
			default:
				throw new IllegalArgumentException("Value " + side + " is not a valid DifferenceSource."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Get the potential ReferenceChanges that represent add/delete containment differences in the parent
	 * Match of the given Match.
	 * 
	 * @param match
	 *            the given Match.
	 * @return the potential ReferenceChanges that represent add/delete containment differences in the parent
	 *         Match of the given Match, <code>null</code> otherwise.
	 */
	public static Iterable<Diff> findAddOrDeleteContainmentDiffs(Match match) {
		final EObject container = match.eContainer();
		if (container instanceof Match) {
			return Iterables.filter(((Match)container).getDifferences(),
					and(CONTAINMENT_REFERENCE_CHANGE, ofKind(ADD, DELETE)));
		}
		return null;
	}

}
