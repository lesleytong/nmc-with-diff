/*******************************************************************************
 * Copyright (c) 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.conflict;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.ecore.EObject;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.ComparisonCanceledException;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.EMFCompareMessages;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.ReferenceChange;
import edu.ustb.sei.mde.compare.internal.conflict.AbstractConflictSearch;
import edu.ustb.sei.mde.compare.internal.conflict.ConflictSearchFactory;

/**
 * This conflict detector searches for conflicting {@link Diff}s in the same {@link Match} as the current
 * {@link Diff}, as well as among {@link ReferenceChange}s that reference the same {@link EObject} as the
 * current {@link Diff}, if it is a {@link ReferenceChange}.
 * <p>
 * This implementation of {@link IConflictDetector} is a generic as the default one but scales better since it
 * is not O(n²) but rather O(n) or O(n log(n)), n being the number of differences in the comparison.
 * </p>
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 3.3
 */
public class MatchBasedConflictDetector implements IConflictDetector {

	/** The logger. */
	private static final Logger LOGGER = Logger.getLogger(MatchBasedConflictDetector.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.conflict.IConflictDetector#detect(edu.ustb.sei.mde.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void detect(Comparison comparison, Monitor monitor) {
		long start = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("detect conflicts - START"); //$NON-NLS-1$
		}
		final List<Diff> differences = comparison.getDifferences();
		final int diffCount = differences.size();

		ConflictSearchFactory conflictSearchFactory = new ConflictSearchFactory(comparison, monitor);
		for (int i = 0; i < diffCount; i++) {
			if (i % 100 == 0) {
				monitor.subTask(EMFCompareMessages.getString("DefaultConflictDetector.monitor.detect", //$NON-NLS-1$
						Integer.valueOf(i + 1), Integer.valueOf(diffCount)));
			}
			if (monitor.isCanceled()) {
				throw new ComparisonCanceledException();
			}
			final Diff diff = differences.get(i);
			AbstractConflictSearch<? extends Diff> search = conflictSearchFactory.doSwitch(diff);
			search.detectConflicts();
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(String.format("detect conflicts - END - Took %d ms", Long.valueOf(System //$NON-NLS-1$
					.currentTimeMillis() - start)));
		}
	}
}
