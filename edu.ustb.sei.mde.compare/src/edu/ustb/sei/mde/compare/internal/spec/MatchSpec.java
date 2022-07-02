/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.internal.spec;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.impl.MatchImpl;
import edu.ustb.sei.mde.compare.internal.SubMatchIterable;

/**
 * This specialization of the {@link MatchImpl} class allows us to define the derived features and operations
 * implementations.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchSpec extends MatchImpl {
	/**
	 * Function returning {@link #getDifferences() all DIFFERENCES} of the given match.
	 */
	private static final Function<Match, List<Diff>> DIFFERENCES = new Function<Match, List<Diff>>() {
		public List<Diff> apply(Match match) {
			if (match == null) {
				return Lists.newArrayList();
			}
			return match.getDifferences();
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.MatchImpl#getComparison()
	 */
	@Override
	public Comparison getComparison() {
		Comparison ret = null;

		EObject eContainer = eContainer();
		while (!(eContainer instanceof Comparison) && eContainer != null) {
			eContainer = eContainer.eContainer();
		}

		if (eContainer != null) {
			ret = (Comparison)eContainer;
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.MatchImpl#getAllSubmatches()
	 */
	@Override
	public Iterable<Match> getAllSubmatches() {
		return new SubMatchIterable(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.impl.MatchImpl#getAllDifferences()
	 */
	@Override
	public Iterable<Diff> getAllDifferences() {
		final Iterable<Diff> allSubDifferences = concat(transform(getAllSubmatches(), DIFFERENCES));
		return concat(getDifferences(), allSubDifferences);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.impl.BasicEObjectImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(this).add("left", EObjectUtil.getLabel(getLeft()))
				.add("right", EObjectUtil.getLabel(getRight()))
				.add("origin", EObjectUtil.getLabel(getOrigin())).add("#differences", getDifferences().size())
				.add("#submatches", getSubmatches().size()).toString();
		// @formatter:on
	}
}
