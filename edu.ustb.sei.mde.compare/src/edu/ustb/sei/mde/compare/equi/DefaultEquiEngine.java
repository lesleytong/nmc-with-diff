/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Stefan Dirix - Fixes for Bugs 452147 and 453218
 *******************************************************************************/
package edu.ustb.sei.mde.compare.equi;

import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

import edu.ustb.sei.mde.compare.CompareFactory;
import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.ComparisonCanceledException;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.EMFCompareMessages;
import edu.ustb.sei.mde.compare.Equivalence;
import edu.ustb.sei.mde.compare.FeatureMapChange;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.internal.utils.ComparisonUtil;
import edu.ustb.sei.mde.compare.utils.IEqualityHelper;

/**
 * The requirements engine is in charge of actually computing the equivalences between the differences.
 * <p>
 * This default implementation aims at being generic enough to be used for any model, whatever the metamodel.
 * However, specific requirements might be necessary.
 * </p>
 * TODO document available extension possibilities.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DefaultEquiEngine implements IEquiEngine {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(DefaultEquiEngine.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.equi.IEquiEngine#computeEquivalences(edu.ustb.sei.mde.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void computeEquivalences(Comparison comparison, Monitor monitor) {
		long start = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("detect equivalences - START")); //$NON-NLS-1$
		}
		monitor.subTask(EMFCompareMessages.getString("DefaultEquiEngine.monitor.eq")); //$NON-NLS-1$
		for (Diff difference : comparison.getDifferences()) {
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
			checkForEquivalences(comparison, difference);
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("detect equivalences - END - Took %d ms", Long.valueOf(System //$NON-NLS-1$
					.currentTimeMillis() - start)));
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param difference
	 *            The difference that is to be checked
	 */
	protected void checkForEquivalences(Comparison comparison, Diff difference) {
		if (difference instanceof ReferenceChange) {
			ReferenceChange referenceChange = (ReferenceChange)difference;
			EReference reference = referenceChange.getReference();
			EReference eOpposite = reference.getEOpposite();

			// If reference change on an opposite reference
			if (eOpposite != null && !eOpposite.isContainer() && !eOpposite.isDerived()) {
				checkForEquivalences(comparison, referenceChange);
			}
		} else if (difference instanceof FeatureMapChange) {
			FeatureMapChange featureMapChange = (FeatureMapChange)difference;
			// Case of a FeatureMap, it may be linked with FeatureMap-derived references.
			checkForEquivalences(comparison, featureMapChange);
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param referenceChange
	 *            The difference that is to be checked
	 */
	protected void checkForEquivalences(final Comparison comparison, final ReferenceChange referenceChange) {
		Equivalence equivalence = referenceChange.getEquivalence();
		if (equivalence == null) {
			// If no equivalence, create one
			equivalence = CompareFactory.eINSTANCE.createEquivalence();

			// Add the current difference to the equivalence
			equivalence.getDifferences().add(referenceChange);

			/*
			 * Add the difference where the value is the object containing the current difference, which is
			 * contained by the value of the current difference, where the reference is linked to the opposite
			 * one
			 */
			final Match valueMatch = comparison.getMatch(referenceChange.getValue());
			final EReference eOpposite = referenceChange.getReference().getEOpposite();

			final Object referenceContainer = ComparisonUtil.getExpectedSide(referenceChange.getMatch(),
					referenceChange.getSource(), false);
			final Object referenceValue = ComparisonUtil.getExpectedSide(valueMatch,
					referenceChange.getSource(), false);
			final boolean valueIsContainer = referenceContainer == referenceValue
					&& referenceContainer != null;

			if (eOpposite != null && valueMatch != null) {
				final Predicate<? super Diff> candidateFilter = new Predicate<Diff>() {
					public boolean apply(Diff input) {
						if (input instanceof ReferenceChange
								&& ((ReferenceChange)input).getReference() == eOpposite) {
							final Match candidateMatch = comparison
									.getMatch(((ReferenceChange)input).getValue());

							final boolean sameMatch = candidateMatch == referenceChange.getMatch();
							final boolean oneIsMany = referenceChange.getReference().isMany()
									|| eOpposite.isMany();

							return sameMatch && (oneIsMany || !valueIsContainer);
						}
						return false;
					}
				};
				final Iterable<Diff> candidates = filter(valueMatch.getDifferences(), candidateFilter);

				for (Diff candidate : candidates) {
					equivalence.getDifferences().add(candidate);
				}

				addChangesFromOrigin(comparison, referenceChange, equivalence);
			}
			if (equivalence.getDifferences().size() > 1) {
				comparison.getEquivalences().add(equivalence);
			}
		}
	}

	/**
	 * Add to the given <code>equivalence</code> potential changes based on the same reference as the given
	 * <code>diff</code>.
	 * 
	 * @param comparison
	 *            The comparison which enable to know from which side come the business model objects owning
	 *            the differences.
	 * @param diff
	 *            The current difference.
	 * @param equivalence
	 *            The current equivalence attached to the difference.
	 */
	private void addChangesFromOrigin(Comparison comparison, ReferenceChange diff, Equivalence equivalence) {
		if (!diff.getReference().isMany()) {
			final EObject originContainer = ComparisonUtil.getExpectedSide(diff.getMatch(), diff.getSource(),
					false);
			final Match valueMatch = comparison.getMatch(diff.getValue());
			if (originContainer != null) {
				for (Diff candidate : comparison.getDifferences(originContainer)) {
					if (!(candidate instanceof ReferenceChange)) {
						continue;
					}
					final ReferenceChange candidateRC = (ReferenceChange)candidate;

					final boolean sameReference = diff.getReference()
							.equals(candidateRC.getReference().getEOpposite());

					final boolean sameContainer = originContainer == ComparisonUtil
							.getExpectedSide(candidate.getMatch(), candidate.getSource(), false);
					final boolean containerIsValue = originContainer == ComparisonUtil
							.getExpectedSide(valueMatch, candidate.getSource(), false);

					if (sameReference
							&& (candidateRC.getReference().isMany() || !sameContainer || !containerIsValue)) {
						equivalence.getDifferences().add(candidate);
					}
				}
			}
		}
	}

	/**
	 * Checks the potential equivalence from the given <code>difference</code>.
	 * 
	 * @param comparison
	 *            The comparison this engine is expected to complete.
	 * @param featureMapChange
	 *            The difference that is to be checked
	 * @since 3.2
	 */
	protected void checkForEquivalences(final Comparison comparison,
			final FeatureMapChange featureMapChange) {
		Equivalence equivalence = featureMapChange.getEquivalence();
		if (equivalence == null) {
			final Set<Diff> differences = new LinkedHashSet<Diff>();
			differences.addAll(featureMapChange.getMatch().getDifferences());

			final Object featureMapEntry = featureMapChange.getValue();
			final Object entryValue = ((FeatureMap.Entry)featureMapEntry).getValue();

			if (entryValue instanceof EObject) {
				addMatchDifferences(comparison.getMatch((EObject)entryValue), comparison, differences);
			}

			final EStructuralFeature entryKey = ((FeatureMap.Entry)featureMapEntry).getEStructuralFeature();
			final Set<ReferenceChange> equivalentDiffs = Sets.newLinkedHashSet();
			final IEqualityHelper equalityHelper = comparison.getEqualityHelper();
			for (ReferenceChange refChange : filter(differences, ReferenceChange.class)) {
				// The current diff has the same ref & value than the Map Entry of the FeatureMapChange.
				boolean sameValue = equalityHelper.matchingValues(refChange.getValue(), entryValue);
				boolean sameSource = featureMapChange.getSource() == refChange.getSource();
				boolean sameReference = refChange.getReference() == entryKey;
				boolean sameMove = refChange.getKind() == DifferenceKind.MOVE
						&& featureMapChange.getKind() == DifferenceKind.MOVE;

				if (sameSource && sameValue && (sameReference || sameMove)) {
					equivalentDiffs.add(refChange);
					if (equivalence == null && refChange.getEquivalence() != null) {
						equivalence = refChange.getEquivalence();
					}
				}
			}

			if (!equivalentDiffs.isEmpty()) {
				if (equivalence == null) {
					equivalence = CompareFactory.eINSTANCE.createEquivalence();
					comparison.getEquivalences().add(equivalence);
				}
				// Add the current difference to the equivalence
				equivalence.getDifferences().add(featureMapChange);
				// Add the MapFeature-derived references to the equivalence
				equivalence.getDifferences().addAll(equivalentDiffs);
			}
		}
	}

	/**
	 * Add the differences of the given match.
	 * 
	 * @param match
	 *            The match to deal with, can be <code>null</code>
	 * @param comparison
	 *            The comparison
	 * @param differences
	 *            The set of differences to add to, its content will be modified
	 */
	private void addMatchDifferences(final Match match, final Comparison comparison,
			final Set<Diff> differences) {
		if (match != null) {
			differences.addAll(match.getDifferences());
			if (match.getLeft() != null) {
				final Match leftParentMatch = comparison.getMatch(match.getLeft().eContainer());
				if (leftParentMatch != null) {
					differences.addAll(leftParentMatch.getDifferences());
				}
			}
			if (match.getRight() != null) {
				final Match rightParentMatch = comparison.getMatch(match.getRight().eContainer());
				if (rightParentMatch != null) {
					differences.addAll(rightParentMatch.getDifferences());
				}
			}
		}
	}
}
