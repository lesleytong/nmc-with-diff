/*******************************************************************************
 * Copyright (c) 2012, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.spec;

import com.google.common.base.Objects;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import edu.ustb.sei.mde.compare.ComparePackage;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceState;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.impl.AttributeChangeImpl;
import edu.ustb.sei.mde.compare.merge.IMerger;

/**
 * This specialization of the {@link AttributeChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class AttributeChangeSpec extends AttributeChangeImpl {
	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.DiffImpl#copyLeftToRight()
	 * @deprecated Use
	 *             {@link edu.ustb.sei.mde.compare.merge.IMerger#copyLeftToRight(edu.ustb.sei.mde.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyLeftToRight() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyLeftToRight(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.DiffImpl#copyRightToLeft()
	 * @deprecated Use
	 *             {@link edu.ustb.sei.mde.compare.merge.IMerger#copyRightToLeft(edu.ustb.sei.mde.compare.Diff, org.eclipse.emf.common.util.Monitor)}
	 *             instead. See javadoc of IMerger.Registry for usage instructions.
	 */
	@Deprecated
	@Override
	public void copyRightToLeft() {
		/*
		 * This is not extensible : we create a registry for each call and use the default mergers. This
		 * implementation is merely a placeholder to avoid API breakage. Please refer to IMerger.Registry for
		 * merging instructions.
		 */
		final IMerger merger = IMerger.RegistryImpl.createStandaloneInstance().getHighestRankingMerger(this);
		merger.copyRightToLeft(this, new BasicMonitor());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
		// Should we also discard equivalent diffs? And diffs that require this one?
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.AttributeChangeImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		EDataType eAttributeType = getAttribute().getEAttributeType();
		final String valueString;
		if (eAttributeType.isSerializable()) {
			valueString = EcoreUtil.convertToString(eAttributeType, getValue());
		} else {
			valueString = getValue().toString();
		}

		return Objects.toStringHelper(this)
				.add("attribute",
						getAttribute().getEContainingClass().getName() + "." + getAttribute().getName())
				.add("value", valueString).add("kind", getKind()).add("source", getSource())
				.add("state", getState()).toString();
	}

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
