/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.merge;

import org.eclipse.emf.common.util.Monitor;

import edu.ustb.sei.mde.compare.ConflictKind;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceState;

/**
 * A simple merger for pseudo conflict. It only marks the differences as merged without doing anything except
 * browsing the requirements and the equivalences.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class PseudoConflictMerger extends AbstractMerger {

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.IMerger#isMergerFor(edu.ustb.sei.mde.compare.Diff)
	 */
	public boolean isMergerFor(Diff target) {
		return target.getConflict() != null && target.getConflict().getKind() == ConflictKind.PSEUDO;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.AbstractMerger#copyLeftToRight(edu.ustb.sei.mde.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyLeftToRight(Diff target, Monitor monitor) {
		super.copyLeftToRight(target, monitor);
		for (Diff pseudoConflictedDiff : target.getConflict().getDifferences()) {
			pseudoConflictedDiff.setState(DifferenceState.MERGED);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.merge.AbstractMerger#copyRightToLeft(edu.ustb.sei.mde.compare.Diff,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		super.copyRightToLeft(target, monitor);
		for (Diff pseudoConflictedDiff : target.getConflict().getDifferences()) {
			pseudoConflictedDiff.setState(DifferenceState.MERGED);
		}
	}
}
