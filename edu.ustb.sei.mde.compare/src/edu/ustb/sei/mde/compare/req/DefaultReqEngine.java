/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - Bug 460902
 *******************************************************************************/
package edu.ustb.sei.mde.compare.req;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static edu.ustb.sei.mde.compare.DifferenceKind.ADD;
import static edu.ustb.sei.mde.compare.DifferenceKind.CHANGE;
import static edu.ustb.sei.mde.compare.DifferenceKind.DELETE;
import static edu.ustb.sei.mde.compare.DifferenceKind.MOVE;
import static edu.ustb.sei.mde.compare.internal.utils.ComparisonUtil.isAddOrSetDiff;
import static edu.ustb.sei.mde.compare.internal.utils.ComparisonUtil.isDeleteOrUnsetDiff;
import static edu.ustb.sei.mde.compare.internal.utils.ComparisonUtil.isFeatureMapContainment;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.ofKind;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMap;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.ComparisonCanceledException;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.DifferenceSource;
import edu.ustb.sei.mde.compare.EMFCompareMessages;
import edu.ustb.sei.mde.compare.FeatureMapChange;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.ResourceAttachmentChange;
import edu.ustb.sei.mde.compare.utils.EMFComparePredicates;
import edu.ustb.sei.mde.compare.utils.MatchUtil;
import edu.ustb.sei.mde.compare.utils.ReferenceUtil;

/**
 * The requirements engine is in charge of actually computing the requirements between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities. TODO to test on XSD models for FeatureMaps
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultReqEngine implements IReqEngine {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultReqEngine.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.req.IReqEngine#computeRequirements(Comparison, Monitor)
	 */
	public void computeRequirements(Comparison comparison, Monitor monitor) {
		long start = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("detect requirements - START")); //$NON-NLS-1$
		}
		monitor.subTask(EMFCompareMessages.getString("DefaultReqEngine.monitor.req")); //$NON-NLS-1$
		for (Diff difference : comparison.getDifferences()) {
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
			checkForRequiredDifferences(comparison, difference);
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("detect requirement - END - Took %d ms", Long.valueOf(System //$NON-NLS-1$
					.currentTimeMillis() - start)));
		}
	}

	/**
	 * Checks the potential required differences from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForRequiredDifferences(Comparison comparison, Diff difference) {

		Set<Diff> requiredDifferences = new LinkedHashSet<Diff>();
		Set<Diff> requiredByDifferences = new LinkedHashSet<Diff>();

		Match match = difference.getMatch();
		EObject value = getValue(comparison, difference);
		DifferenceKind kind = difference.getKind();

		if (value != null) {
			boolean isAddition = isAddOrSetDiff(difference);
			boolean isDeletion = !isAddition && isDeleteOrUnsetDiff(difference);

			if (isAddition && isDeleteOrAddResourceAttachmentChange(comparison, difference)) {
				requiredDifferences.addAll(getDiffsThatShouldDependOn((ResourceAttachmentChange)difference));
				// ADD object
			} else if (isAddition && isReferenceContainment(difference)) {
				// if (isAddition && isReferenceContainment(difference)) {

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContainer(),
						difference.getSource(), ADD));

				// -> requires DELETE of the origin value on the same containment mono-valued reference
				requiredDifferences.addAll(getDELOriginValueOnContainmentRefSingle(comparison, difference));

				// ADD reference
			} else if (isAddition && !isFeatureMapContainment(difference)) {

				// -> requires ADD of the value of the reference (target object)
				requiredDifferences
						.addAll(getDifferenceOnGivenObject(comparison, value, difference.getSource(), ADD));

				// -> requires ADD of the object containing the reference
				final EObject container = MatchUtil.getContainer(comparison, difference);
				if (container != null) {
					requiredDifferences.addAll(
							getDifferenceOnGivenObject(comparison, container, difference.getSource(), ADD));
				}
				requiredDifferences.addAll(Collections2.filter(match.getDifferences(),
						and(instanceOf(ResourceAttachmentChange.class), ofKind(ADD))));

			} else if (isDeletion && isDeleteOrAddResourceAttachmentChange(comparison, difference)) {
				requiredByDifferences
						.addAll(getDiffsThatShouldDependOn((ResourceAttachmentChange)difference));
				// DELETE object
			} else if (isDeletion && isReferenceContainment(difference)) {

				// -> requires DELETE of the outgoing references and contained objects
				requiredDifferences.addAll(getDELOutgoingReferences(comparison, difference));
				requiredDifferences.addAll(getDifferenceOnGivenObject(comparison, value.eContents(),
						difference.getSource(), DELETE));

				// -> requires MOVE of contained objects
				requiredDifferences.addAll(getMOVEContainedObjects(comparison, difference));

				// The DELETE or CHANGE of incoming references are handled in the DELETE reference and CHANGE
				// reference cases.

				// DELETE reference
			} else if (isDeletion && !isFeatureMapContainment(difference)) {

				// -> is required by DELETE of the target object
				requiredByDifferences.addAll(
						getDifferenceOnGivenObject(comparison, value, difference.getSource(), DELETE));

				// MOVE object
			} else if (kind == MOVE && isReferenceContainment(difference)) {

				EObject container = value.eContainer();

				// -> requires ADD on the container of the object
				requiredDifferences.addAll(
						getDifferenceOnGivenObject(comparison, container, difference.getSource(), ADD));

				// -> requires MOVE of the container of the object
				requiredDifferences.addAll(
						getDifferenceOnGivenObject(comparison, container, difference.getSource(), MOVE));

				// CHANGE reference
			} else if (kind == CHANGE && !isAddition && !isDeletion
					&& !(difference instanceof FeatureMapChange)) {

				// -> is required by DELETE of the origin target object
				requiredByDifferences.addAll(getDifferenceOnGivenObject(comparison,
						MatchUtil.getOriginValue(comparison, (ReferenceChange)difference),
						difference.getSource(), DELETE));

				// -> requires ADD of the value of the reference (target object) if required
				requiredDifferences
						.addAll(getDifferenceOnGivenObject(comparison, value, difference.getSource(), ADD));
			}

			difference.getRequires().addAll(Collections2.filter(requiredDifferences,
					EMFComparePredicates.fromSide(difference.getSource())));
			difference.getRequiredBy().addAll(Collections2.filter(requiredByDifferences,
					EMFComparePredicates.fromSide(difference.getSource())));
		}

	}

	/**
	 * Checks whether the given diff corresponds to a reference change associated with the addition or the
	 * deletion of an object.
	 * 
	 * @param comparison
	 *            The comparison
	 * @param diff
	 *            The diff to consider
	 * @return <code>true</code> if the given {@code diff} is to be considered a ResourceAttachmentChange with
	 *         ADD or DELETE dependencies, <code>false</code> otherwise.
	 */
	private boolean isDeleteOrAddResourceAttachmentChange(Comparison comparison, Diff diff) {
		if (diff instanceof ResourceAttachmentChange && (diff.getKind() == ADD || diff.getKind() == DELETE)) {
			EObject container = MatchUtil.getContainer(comparison, diff);
			if (container != null) {
				EList<Diff> differences = comparison.getDifferences(container);
				for (Diff containedDiff : differences) {
					if (containedDiff instanceof ReferenceChange
							&& ((ReferenceChange)containedDiff).getReference().isContainment()
							&& containedDiff.getKind() == diff.getKind()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Compute the dependencies specific to ResourceAttachmentChanges DELETE or ADD. (The addition or deletion
	 * of the package controlled/uncontrolled must be a dependency of the RAC)
	 * 
	 * @param diff
	 *            The given difference
	 * @return a list of dependencies
	 */
	private Set<ReferenceChange> getDiffsThatShouldDependOn(ResourceAttachmentChange diff) {
		Set<ReferenceChange> result = new LinkedHashSet<ReferenceChange>();
		Comparison comparison = diff.getMatch().getComparison();
		EObject container = MatchUtil.getContainer(comparison, diff);
		for (ReferenceChange rc : filter(comparison.getDifferences(container), ReferenceChange.class)) {
			if (diff.getSource() == rc.getSource() && diff.getKind() == rc.getKind()) {
				result.add(rc);
			}
		}
		return result;
	}

	/**
	 * From a <code>sourceDifference</code> (ADD) on a containment mono-valued reference, it retrieves a
	 * potential DELETE difference on the origin value.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<Diff> getDELOriginValueOnContainmentRefSingle(Comparison comparison, Diff sourceDifference) {
		Set<Diff> result = new LinkedHashSet<Diff>();
		if (!(sourceDifference instanceof ReferenceChange)) {
			return result;
		}
		EReference reference = ((ReferenceChange)sourceDifference).getReference();
		if (!reference.isMany()) {
			EObject originContainer = MatchUtil.getOriginContainer(comparison, sourceDifference);
			if (originContainer != null) {
				Object originValue = ReferenceUtil.safeEGet(originContainer, reference);
				if (originValue instanceof EObject) {
					result = getDifferenceOnGivenObject(comparison, (EObject)originValue,
							sourceDifference.getSource(), DELETE);
				}
			}
		}
		return result;
	}

	/**
	 * Retrieve candidate reference changes based on the given <code>object</code> (value) which are from the
	 * given <code>kind</code>.
	 * 
	 * @param comparison
	 *            the comparison to search in.
	 * @param object
	 *            The given object.
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The given kind.
	 * @return The found differences.
	 */
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, EObject object,
			DifferenceSource source, DifferenceKind kind) {
		final Set<Diff> result = new LinkedHashSet<Diff>();
		for (Diff diff : filter(comparison.getDifferences(object),
				isRequiredContainmentChange(object, source, kind))) {
			result.add(diff);
		}
		return result;
	}

	/**
	 * Will be used to filter out of a list of differences all those that relate to containment changes on the
	 * given object (a containment reference change or a resource attachment change if the given object has no
	 * direct container.
	 * 
	 * @param object
	 *            The object for which we seek containmnent differences.
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The kind of difference we seek.
	 * @return The created predicate.
	 */
	private Predicate<? super Diff> isRequiredContainmentChange(final EObject object,
			final DifferenceSource source, final DifferenceKind kind) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				if (input == null || input.getKind() != kind || input.getSource() != source) {
					return false;
				}

				boolean result = false;
				if (input instanceof ReferenceChange
						&& ((ReferenceChange)input).getReference().isContainment()) {
					result = true;
				} else if (input instanceof ResourceAttachmentChange && object.eContainer() == null) {
					result = true;
				}
				return result;
			}
		};
	}

	/**
	 * Retrieve candidate reference changes from a list of given <code>objects</code>.
	 * 
	 * @see DefaultReqEngine#getDifferenceOnGivenObject(EObject, DifferenceKind).
	 * @param comparison
	 *            the comparison to search in.
	 * @param objects
	 *            The given objects.
	 * @param source
	 *            source of the differences. A diff from the left cannot "require" a diff from the right...
	 * @param kind
	 *            The kind of requested differences.
	 * @return The found differences.
	 */
	private Set<Diff> getDifferenceOnGivenObject(Comparison comparison, List<EObject> objects,
			DifferenceSource source, DifferenceKind kind) {
		Set<Diff> result = new LinkedHashSet<Diff>();
		for (EObject object : objects) {
			result.addAll(getDifferenceOnGivenObject(comparison, object, source, kind));
		}
		return result;
	}

	/**
	 * From a <code>sourceDifference</code> (DELETE) on a containment reference, it retrieves potential DELETE
	 * differences on the outgoing references from the value object of the <code>sourceDifference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<Diff> getDELOutgoingReferences(Comparison comparison, Diff sourceDifference) {
		Set<Diff> result = new LinkedHashSet<Diff>();

		EObject value = getValue(comparison, sourceDifference);

		if (value != null) {
			final Match valueMatch = comparison.getMatch(value);
			if (valueMatch != null) {
				for (Diff candidate : filter(valueMatch.getDifferences(),
						or(instanceOf(ReferenceChange.class), instanceOf(FeatureMapChange.class)))) {
					if (candidate.getSource() == sourceDifference.getSource()
							&& (candidate.getKind() == DELETE || isDeleteOrUnsetDiff(candidate))) {
						result.add(candidate);
					}
				}
			}
		}

		return result;
	}

	/**
	 * From a <code>sourceDifference</code> (DELETE) on a containment reference, it retrieves potential MOVE
	 * differences on the objects contained in the value object of the <code>sourceDifference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param sourceDifference
	 *            The given difference.
	 * @return The found differences.
	 */
	private Set<ReferenceChange> getMOVEContainedObjects(Comparison comparison, Diff sourceDifference) {
		Set<ReferenceChange> result = new LinkedHashSet<ReferenceChange>();
		EObject value = getValue(comparison, sourceDifference);
		if (value != null) {
			List<EObject> contents = value.eContents();
			for (EObject content : contents) {
				EObject originObject = MatchUtil.getOriginObject(comparison, content);
				if (originObject != null) {
					for (ReferenceChange difference : filter(comparison.getDifferences(originObject),
							ReferenceChange.class)) {
						if (difference.getReference().isContainment()
								&& difference.getSource() == sourceDifference.getSource()
								&& difference.getKind() == MOVE) {
							result.add(difference);
						}

					}
				}
			}
		}
		return result;
	}

	/**
	 * Checks whether the given diff corresponds to a containment change. This holds true for differences on
	 * containment references' values, but also for feature map or resource attachment changes.
	 * 
	 * @param diff
	 *            The diff to consider.
	 * @return <code>true</code> if the given {@code diff} is to be considered a containment change,
	 *         <code>false</code> otherwise.
	 */
	private static boolean isReferenceContainment(Diff diff) {
		return diff instanceof ReferenceChange && ((ReferenceChange)diff).getReference().isContainment()
				|| diff instanceof ResourceAttachmentChange || diff instanceof FeatureMapChange;
	}

	/**
	 * Retrieves the "value" of the given containment change. This will be either the "value" field of a
	 * ReferenceChange, or the side of the parent match for a resource attachment change.
	 * 
	 * @param comparison
	 *            The comparison during which this {@code diff} was detected.
	 * @param diff
	 *            The diff which value we are to retrieve.
	 * @return The "value" of the given containment change.
	 */
	private static EObject getValue(Comparison comparison, Diff diff) {
		EObject value = null;
		if (diff instanceof ReferenceChange) {
			value = ((ReferenceChange)diff).getValue();
		} else if (diff instanceof ResourceAttachmentChange) {
			value = MatchUtil.getContainer(comparison, diff);
		} else if (diff instanceof FeatureMapChange) {
			Object entry = ((FeatureMapChange)diff).getValue();
			if (entry instanceof FeatureMap.Entry) {
				Object entryValue = ((FeatureMap.Entry)entry).getValue();
				if (entryValue instanceof EObject) {
					value = (EObject)entryValue;
				}
			}
		}
		return value;
	}

}
