/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.conflict;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static edu.ustb.sei.mde.compare.ConflictKind.PSEUDO;
import static edu.ustb.sei.mde.compare.ConflictKind.REAL;
import static edu.ustb.sei.mde.compare.DifferenceKind.ADD;
import static edu.ustb.sei.mde.compare.DifferenceKind.CHANGE;
import static edu.ustb.sei.mde.compare.DifferenceKind.DELETE;
import static edu.ustb.sei.mde.compare.DifferenceKind.MOVE;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.ofKind;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.onFeature;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.possiblyConflictingWith;
import static edu.ustb.sei.mde.compare.utils.EMFComparePredicates.valueMatches;
import static edu.ustb.sei.mde.compare.utils.MatchUtil.matchingIndices;

import com.google.common.collect.Iterables;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.FeatureMap;

import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceKind;
import edu.ustb.sei.mde.compare.FeatureMapChange;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.ReferenceChange;

/**
 * Search conflicts for {@link FeatureMapChange}s.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class FeatureMapChangeConflictSearch {

	/**
	 * Search conflicts for {@link FeatureMapChange} of kind {@link DifferenceKind#ADD}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Add extends AbstractConflictSearch<FeatureMapChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Add(FeatureMapChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			EAttribute feature = diff.getAttribute();
			// Only unique features can conflict
			// FIXME Make sure the above assumption is true
			if (feature.isUnique()) {
				Object value = diff.getValue();
				EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
				for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
						instanceOf(FeatureMapChange.class), onFeature(feature), ofKind(ADD)))) {
					Object candidateValue = ((FeatureMapChange)candidate).getValue();
					if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
						// This is a conflict. Is it real?
						if (matchingIndices(diff.getMatch(), feature, value, candidateValue)) {
							conflict(candidate, PSEUDO);
						} else {
							conflict(candidate, REAL);
						}
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link ReferenceChange} of kind {@link DifferenceKind#CHANGE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Change extends AbstractConflictSearch<FeatureMapChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Change(FeatureMapChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Object value = diff.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(FeatureMapChange.class), onFeature(feature), ofKind(CHANGE)))) {
				Object candidateValue = ((FeatureMapChange)candidate).getValue();
				if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
					// Same value added on both side in the same container
					conflict(candidate, PSEUDO);
				} else if (!isFeatureMapChangeOrMergeableStringAttributeChange(diff, candidate)) {
					conflict(candidate, REAL);
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link FeatureMapChange} of kind {@link DifferenceKind#DELETE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Delete extends AbstractConflictSearch<FeatureMapChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Delete(FeatureMapChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Object value = diff.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diff.getMatch().getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch, and(possiblyConflictingWith(diff),
					instanceOf(FeatureMapChange.class), onFeature(feature), ofKind(MOVE, DELETE)))) {
				Object candidateValue = ((FeatureMapChange)candidate).getValue();
				if (comparison.getEqualityHelper().matchingValues(value, candidateValue)) {
					if (candidate.getKind() == MOVE) {
						conflict(candidate, REAL);
					} else if (diff.getMatch() == candidate.getMatch()) {
						conflict(candidate, PSEUDO);
					}
				}
			}
		}
	}

	/**
	 * Search conflicts for {@link FeatureMapChange} of kind {@link DifferenceKind#MOVE}.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	public static class Move extends AbstractConflictSearch<FeatureMapChange> {

		/**
		 * Constructor.
		 * 
		 * @param diff
		 *            The diff to search for conflicts
		 * @param index
		 *            Comparison index, must not be null
		 * @param monitor
		 *            the monitor to report progress to, must not be null
		 */
		public Move(FeatureMapChange diff, ComparisonIndex index, Monitor monitor) {
			super(diff, index, monitor);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void detectConflicts() {
			Match diffMatch = diff.getMatch();
			FeatureMap.Entry entry = (FeatureMap.Entry)diff.getValue();
			Object value = entry.getValue();
			EAttribute feature = diff.getAttribute();
			EList<Diff> diffsInSameMatch = diffMatch.getDifferences();
			for (Diff candidate : Iterables.filter(diffsInSameMatch,
					and(possiblyConflictingWith(diff), instanceOf(FeatureMapChange.class),
							valueMatches(comparison.getEqualityHelper(), value), onFeature(feature),
							ofKind(MOVE)))) {
				Object candidateValue = ((FeatureMapChange)candidate).getValue();
				if (matchingIndices(diff.getMatch(), feature, value, candidateValue)) {
					conflict(candidate, PSEUDO);
				} else {
					conflict(candidate, REAL);
				}
			}
		}
	}
}
