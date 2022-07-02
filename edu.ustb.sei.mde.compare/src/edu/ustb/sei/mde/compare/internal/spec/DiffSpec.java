/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.spec;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import edu.ustb.sei.mde.compare.ComparePackage;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.impl.DiffImpl;

/**
 * This specialization of the {@link DiffImpl} class allows us to define the derived features and operations
 * implementations.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public class DiffSpec extends DiffImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.DiffImpl#basicGetMatch()
	 */
	@Override
	public Match basicGetMatch() {
		if (eContainer() instanceof Match) {
			return (Match)eContainer();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.DiffImpl#setMatch(edu.ustb.sei.mde.compare.Match)
	 */
	@Override
	public void setMatch(Match newMatch) {
		Match oldMatch = basicGetMatch();
		if (newMatch != null) {
			EList<Diff> differences = newMatch.getDifferences();
			differences.add(this);
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__MATCH, oldMatch,
					newMatch));
		} else if (eContainer() instanceof Match) {
			EList<Diff> differences = ((Match)eContainer()).getDifferences();
			differences.remove(this);
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComparePackage.DIFF__MATCH, oldMatch,
					newMatch));

		}
	}
}
