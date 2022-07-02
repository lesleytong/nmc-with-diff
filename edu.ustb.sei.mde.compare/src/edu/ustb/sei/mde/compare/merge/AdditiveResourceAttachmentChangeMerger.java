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
package edu.ustb.sei.mde.compare.merge;

import static edu.ustb.sei.mde.compare.DifferenceKind.DELETE;
import static edu.ustb.sei.mde.compare.DifferenceSource.LEFT;
import static edu.ustb.sei.mde.compare.DifferenceState.MERGED;
import static edu.ustb.sei.mde.compare.DifferenceState.UNRESOLVED;

import org.eclipse.emf.common.util.Monitor;

import edu.ustb.sei.mde.compare.Diff;

/**
 * This specific implementation of {@link AbstractMerger} will be used to merge resource attachment changes in
 * an additive merge context.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 3.4
 */
public class AdditiveResourceAttachmentChangeMerger extends ResourceAttachmentChangeMerger {

	/**
	 * The constructor specify the context where this merger can be used.
	 */
	public AdditiveResourceAttachmentChangeMerger() {
		super();
		mergeOptions.put(IMergeCriterion.OPTION_MERGE_CRITERION, AdditiveMergeCriterion.INSTANCE);
	}

	@Override
	public boolean apply(IMergeCriterion criterion) {
		return criterion == AdditiveMergeCriterion.INSTANCE;
	}

	@Override
	public void copyRightToLeft(Diff target, Monitor monitor) {
		if (target.getState() != UNRESOLVED) {
			return;
		}

		if (target.getKind() == DELETE) {
			if (target.getSource() == LEFT) {
				super.copyRightToLeft(target, monitor);
			} else {
				target.setState(MERGED);
				for (Diff refiningDiff : target.getRefinedBy()) {
					refiningDiff.setState(MERGED);
				}
			}
		} else {
			super.copyRightToLeft(target, monitor);
		}
	}

}
