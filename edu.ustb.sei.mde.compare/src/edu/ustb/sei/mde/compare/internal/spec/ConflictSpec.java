/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.spec;

import com.google.common.collect.Iterables;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceSource;
import edu.ustb.sei.mde.compare.impl.ConflictImpl;
import edu.ustb.sei.mde.compare.utils.EMFComparePredicates;

/**
 * This specialization of the {@link ConflictImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ConflictSpec extends ConflictImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.ConflictImpl#getLeftDifferences()
	 */
	@Override
	public EList<Diff> getLeftDifferences() {
		final EList<Diff> leftDiffs = new BasicEList<Diff>();
		for (Diff diff : Iterables.filter(getDifferences(),
				EMFComparePredicates.fromSide(DifferenceSource.LEFT))) {
			leftDiffs.add(diff);
		}
		return leftDiffs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.ConflictImpl#getRightDifferences()
	 */
	@Override
	public EList<Diff> getRightDifferences() {
		final EList<Diff> rightDiffs = new BasicEList<Diff>();
		for (Diff diff : Iterables.filter(getDifferences(),
				EMFComparePredicates.fromSide(DifferenceSource.RIGHT))) {
			rightDiffs.add(diff);
		}
		return rightDiffs;
	}
}
