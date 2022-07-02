/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.merge;

import static edu.ustb.sei.mde.compare.ConflictKind.REAL;
import static edu.ustb.sei.mde.compare.DifferenceKind.DELETE;
import static edu.ustb.sei.mde.compare.DifferenceKind.MOVE;
import static edu.ustb.sei.mde.compare.DifferenceSource.LEFT;
import static edu.ustb.sei.mde.compare.DifferenceSource.RIGHT;
import static edu.ustb.sei.mde.compare.DifferenceState.MERGED;
import static edu.ustb.sei.mde.compare.DifferenceState.UNRESOLVED;

import java.util.Iterator;

import org.eclipse.emf.common.util.Monitor;

import edu.ustb.sei.mde.compare.Conflict;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.utils.IEqualityHelper;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge real conflicts.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.1
 */
public class ConflictMerger extends AbstractMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.IMerger#isMergerFor(edu.ustb.sei.mde.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target.getConflict() != null && target.getConflict().getKind() == REAL;
	}

	@Override
	public boolean apply(IMergeCriterion criterion) {
		return criterion == null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.IMerger#copyLeftToRight(edu.ustb.sei.mde.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyLeftToRight(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != UNRESOLVED) {
			return;
		}

		if (target.getSource() == LEFT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (conflictedDiff.getSource() == RIGHT) {
					if (isConflictVsMoveAndDelete(target, conflictedDiff, true)) {
						conflictedDiff.setState(MERGED);
					} else {
						mergeConflictedDiff(conflictedDiff, true, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getMergerDelegate(target).copyLeftToRight(target, monitor);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.IMerger#copyRightToLeft(edu.ustb.sei.mde.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		// Don't merge an already merged (or discarded) diff
		if (target.getState() != UNRESOLVED) {
			return;
		}

		if (target.getSource() == RIGHT) {
			// Call the appropriate merger for each conflicted diff
			Conflict conflict = target.getConflict();
			for (Diff conflictedDiff : conflict.getDifferences()) {
				if (conflictedDiff.getSource() == LEFT) {
					if (isConflictVsMoveAndDelete(target, conflictedDiff, false)) {
						conflictedDiff.setState(MERGED);
					} else {
						mergeConflictedDiff(conflictedDiff, false, monitor);
					}
				}
			}
		}

		// Call the appropriate merger for the current diff
		getMergerDelegate(target).copyRightToLeft(target, monitor);
	}

	/**
	 * Detect if the two given diff are diffs on the same object with one move and one delete. The move diff
	 * must be the one selected by the user for merging.
	 * 
	 * @param target
	 *            The diff selected for merge by the user
	 * @param conflictedDiff
	 *            Another diff of the conflict
	 * @param leftToRight
	 *            The direction of the merge
	 * @return <code>true</code> if the diff selected by the user is a move and is conflicting with a delete
	 *         of the same element
	 */
	private boolean isConflictVsMoveAndDelete(Diff target, Diff conflictedDiff, boolean leftToRight) {
		boolean result = false;
		if (target.getConflict() != null && target.getConflict().getKind() == REAL) {
			if (target instanceof ReferenceChange && target.getKind() == MOVE) {
				ReferenceChange moveDiff = (ReferenceChange)target;
				if (conflictedDiff instanceof ReferenceChange && conflictedDiff.getKind() == DELETE) {
					ReferenceChange deleteDiff = (ReferenceChange)conflictedDiff;
					IEqualityHelper equalityHelper = target.getMatch().getComparison().getEqualityHelper();
					result = equalityHelper.matchingAttributeValues(moveDiff.getValue(),
							deleteDiff.getValue());
				}
			}
		}

		return result;
	}

	/**
	 * Manages the merge of the given conflicted diff.
	 * 
	 * @param conflictedDiff
	 *            The given diff.
	 * @param leftToRight
	 *            The way of merge.
	 * @param monitor
	 *            Monitor.
	 */
	private void mergeConflictedDiff(Diff conflictedDiff, boolean leftToRight, Monitor monitor) {
		if (conflictedDiff.getKind() != MOVE) {
			DelegatingMerger delegate = getMergerDelegate(conflictedDiff);
			if (leftToRight) {
				delegate.copyLeftToRight(conflictedDiff, monitor);
			} else {
				delegate.copyRightToLeft(conflictedDiff, monitor);
			}
		} else {
			conflictedDiff.setState(MERGED);
		}
	}

	@Override
	protected DelegatingMerger getMergerDelegate(Diff diff) {
		IMergeCriterion criterion = (IMergeCriterion)getMergeOptions().get(
				IMergeCriterion.OPTION_MERGE_CRITERION);
		Iterator<IMerger> it = ((Registry2)getRegistry()).getMergersByRankDescending(diff, criterion);
		IMerger merger = this;
		while (it.hasNext() && merger == this) {
			merger = it.next();
		}
		if (merger == null) {
			throw new IllegalStateException("No merger found for diff " + diff.getClass().getSimpleName()); //$NON-NLS-1$
		}
		return new DelegatingMerger(merger, criterion);
	}
}
