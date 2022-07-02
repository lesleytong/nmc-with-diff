/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.merge;

import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceSource;

/**
 * Enumeration of all ways of merging differences.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public enum MergeMode {
	/** Merge from left to right. */
	LEFT_TO_RIGHT,

	/** Merge form right to left. */
	RIGHT_TO_LEFT,

	/** Accept the diff to merge. */
	ACCEPT,

	/** Reject the diff to merge. */
	REJECT;

	/**
	 * Returns the inverse of this enum.
	 * 
	 * @return the inverse of this enum.
	 */
	public MergeMode inverse() {
		final MergeMode ret;
		switch (this) {
			case LEFT_TO_RIGHT:
				ret = MergeMode.RIGHT_TO_LEFT;
				break;
			case RIGHT_TO_LEFT:
				ret = MergeMode.LEFT_TO_RIGHT;
				break;
			case ACCEPT:
				ret = MergeMode.REJECT;
				break;
			case REJECT:
				ret = MergeMode.ACCEPT;
				break;
			default:
				throw new IllegalStateException();
		}
		return ret;
	}

	/**
	 * Returns the target of the merge with the given condition about the left and right sides.
	 * 
	 * @param isLeftEditable
	 *            is the left side editable.
	 * @param isRightEditable
	 *            is the right side editable.
	 * @return the target of the merge with the given condition about the left and right sides.
	 */
	public DifferenceSource getMergeTarget(boolean isLeftEditable, boolean isRightEditable) {
		final DifferenceSource ret;
		switch (this) {
			case LEFT_TO_RIGHT:
				ret = DifferenceSource.RIGHT;
				break;
			case RIGHT_TO_LEFT:
				ret = DifferenceSource.LEFT;
				break;
			case ACCEPT:
			case REJECT:
				if (isLeftEditable) {
					ret = DifferenceSource.LEFT;
				} else if (isRightEditable) {
					ret = DifferenceSource.RIGHT;
				} else {
					throw new IllegalStateException();
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return ret;
	}

	/**
	 * Returns if this mode will lead to merge to left to right depending whether left and/or right are
	 * editable.
	 * 
	 * @param isLeftEditable
	 *            is left side of the comparison editable.
	 * @param isRightEditable
	 *            is right side of the comparison editable.
	 * @return if this mode will lead to merge to left to right depending whether left and/or right are
	 *         editable.
	 */
	public boolean isLeftToRight(boolean isLeftEditable, boolean isRightEditable) {
		return getMergeTarget(isLeftEditable, isRightEditable) == DifferenceSource.RIGHT;
	}

	/**
	 * To exactly know the way of merge (to compute consequences) we need the source of the diff.
	 * 
	 * @param diff
	 *            the diff to merge.
	 * @param isLeftEditable
	 *            is left side of the comparison editable.
	 * @param isRightEditable
	 *            is right side of the comparison editable.
	 * @return the way of merge.
	 */
	public boolean isLeftToRight(Diff diff, boolean isLeftEditable, boolean isRightEditable) {
		final boolean leftToRight;
		if (this == MergeMode.ACCEPT && diff.getSource() == DifferenceSource.LEFT && isLeftEditable) {
			leftToRight = true;
		} else if (this == MergeMode.REJECT && diff.getSource() == DifferenceSource.RIGHT && isLeftEditable) {
			leftToRight = true;
		} else if (this == MergeMode.ACCEPT && diff.getSource() == DifferenceSource.RIGHT
				&& isRightEditable) {
			leftToRight = false;
		} else if (this == MergeMode.REJECT && diff.getSource() == DifferenceSource.LEFT && isRightEditable) {
			leftToRight = false;
		} else {
			leftToRight = isLeftToRight(isLeftEditable, isRightEditable);
		}
		return leftToRight;
	}

	/**
	 * Returns the required action to be done to the given difference in this mode.
	 * 
	 * @param difference
	 *            the difference to analyze.
	 * @param isLeftEditable
	 *            is left side of the comparison editable.
	 * @param isRightEditable
	 *            is right side of the comparison editable.
	 * @return the required action to be done to the given difference in this mode.
	 */
	public MergeOperation getMergeAction(Diff difference, boolean isLeftEditable, boolean isRightEditable) {
		final MergeOperation ret;
		switch (this) {
			case LEFT_TO_RIGHT:
			case RIGHT_TO_LEFT:
				ret = MergeOperation.MERGE;
				break;
			case ACCEPT:
				if (isLeftEditable) {
					if (difference.getSource() == DifferenceSource.LEFT) {
						ret = MergeOperation.MARK_AS_MERGE;
					} else {
						ret = MergeOperation.MERGE;
					}
				} else if (isRightEditable) {
					if (difference.getSource() == DifferenceSource.LEFT) {
						ret = MergeOperation.MERGE;
					} else {
						ret = MergeOperation.MARK_AS_MERGE;
					}
				} else {
					throw new IllegalStateException();
				}
				break;
			case REJECT:
				if (isLeftEditable) {
					if (difference.getSource() == DifferenceSource.LEFT) {
						ret = MergeOperation.MERGE;
					} else {
						ret = MergeOperation.MARK_AS_MERGE;
					}
				} else if (isRightEditable) {
					if (difference.getSource() == DifferenceSource.LEFT) {
						ret = MergeOperation.MARK_AS_MERGE;
					} else {
						ret = MergeOperation.MERGE;
					}
				} else {
					throw new IllegalStateException();
				}
				break;
			default:
				throw new IllegalStateException();
		}
		return ret;
	}
}
