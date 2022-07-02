/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.match.eobject;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.match.eobject.internal.AccessBasedLRUCache;

/**
 * This class is able to measure similarity between "URI like" strings, basically strings separated by "/".
 * This is mainly intended to be used with EMF's fragments.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class URIDistance implements Function<EObject, Iterable<String>> {
	/**
	 * The upper bound distance we can get using this function.
	 */
	private static final int MAX_DISTANCE = 10;

	/**
	 * A computing cache for the locations.
	 */
	private Map<EObject, Iterable<String>> locationCache;

	/**
	 * A computing cache for the uri fragments.
	 */
	private Map<EObject, String> fragmentsCache;

	/**
	 * The function used to compute the fragment of an {@link EObject}.
	 */
	private Function<EObject, String> fragmentComputation;

	/**
	 * An optional comparison to retrieve matches already computed. This will impact the way the uri is
	 * computed by making sure two matching objects will have the same URI.
	 */
	private Optional<Comparison> underMatch = Optional.absent();

	/**
	 * Create a new {@link URIDistance}.
	 */
	public URIDistance() {
		locationCache = new AccessBasedLRUCache<EObject, Iterable<String>>(1000, 1000, .75F);
		fragmentsCache = new AccessBasedLRUCache<EObject, String>(1000, 1000, .75F);
		fragmentComputation = new EUriFragmentFunction();
	}

	/**
	 * Set an optional comparison used to retrieve matches already computed. This will impact the way the uri
	 * is computed by making sure two matching objects will have the same URI.
	 * 
	 * @param comparison
	 *            the comparison to use to retrieve the matches.
	 */
	public void setComparison(Comparison comparison) {
		this.underMatch = Optional.fromNullable(comparison);
	}

	/**
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param a
	 *            First of the two {@link EObject}s to compare.
	 * @param b
	 *            Second of the two {@link EObject}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(EObject a, EObject b) {
		Iterable<String> aPath = getOrComputeLocation(a);
		Iterable<String> bPath = getOrComputeLocation(b);
		return proximity(aPath, bPath);
	}

	/**
	 * Return a metric result URI similarities. It compares 2 lists of fragments and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param aPath
	 *            First of the two list of {@link String}s to compare.
	 * @param bPath
	 *            Second of the two list of {@link String}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(Iterable<String> aPath, Iterable<String> bPath) {
		int aSize = 0;
		int bSize = 0;
		Iterator<String> itA = aPath.iterator();
		Iterator<String> itB = bPath.iterator();
		boolean areSame = true;
		int commonSegments = 0;
		int remainingASegments = 0;
		int remainingBSegments = 0;
		while (itA.hasNext() && itB.hasNext() && areSame) {
			String a = itA.next();
			String b = itB.next();
			if (a.equals(b)) {
				commonSegments++;
			} else {
				areSame = false;
			}
			aSize++;
			bSize++;

		}
		if (commonSegments == 0) {
			return MAX_DISTANCE;
		}
		remainingASegments = aSize + Iterators.size(itA) - commonSegments;
		remainingBSegments = bSize + Iterators.size(itB) - commonSegments;

		int nbSegmentsToGoFromAToB = remainingASegments + remainingBSegments;
		return (nbSegmentsToGoFromAToB * 10) / (commonSegments * 2 + nbSegmentsToGoFromAToB);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public Iterable<String> apply(EObject input) {
		String result = ""; //$NON-NLS-1$
		EObject container = input.eContainer();
		if (container != null) {
			if (underMatch.isPresent()) {
				/*
				 * If we have a match for the container, we want to make sure the fragment is going to be the
				 * same.
				 */
				Match m = underMatch.get().getMatch(container);
				if (m == null) {
					result = retrieveFragment(input);
				}
			} else {
				result = retrieveFragment(input);
			}
		} else {
			result = "0"; //$NON-NLS-1$
		}

		final List<String> resultList = Lists.newArrayList(result);
		if (container != null) {
			Iterables.addAll(resultList, getOrComputeLocation(container));
		}
		return resultList;
	}

	/**
	 * The method return the location of an EObject represented as a list of fragments.
	 * 
	 * @param container
	 *            any EObject.
	 * @return a list of fragments.
	 */
	private Iterable<String> getOrComputeLocation(EObject container) {
		Iterable<String> result = locationCache.get(container);
		if (result == null) {
			result = apply(container);
			locationCache.put(container, result);
		}
		return result;
	}

	/**
	 * the containing fragment for a given {@link EObject}.
	 * 
	 * @param input
	 *            an EObject.
	 * @return a String representation of its containing fragment.
	 */
	public String retrieveFragment(EObject input) {
		String result = fragmentsCache.get(input);
		if (result == null) {
			result = fragmentComputation.apply(input);
			fragmentsCache.put(input, result);
		}
		return result;
	}

	/**
	 * return the maximum value we can get for this distance.
	 * 
	 * @return the maximum value we can get for this distance.
	 */
	public int getUpperBoundDistance() {
		return MAX_DISTANCE;
	}

}
