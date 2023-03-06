package edu.ustb.sei.mde.compare.hashser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


/**
 * This class is able to measure similarity between "URI like" strings, basically strings separated by "/".
 * This is mainly intended to be used with EMF's fragments.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class URIComputer implements Function<EObject, Iterable<String>> {
//	/**
//	 * The upper bound distance we can get using this function.
//	 */
//	private static final int MAX_DISTANCE = 10;

	/**
	 * A computing cache for the locations.
	 */
	private Map<EObject, FragmentIterable> locationCache;

	/**
	 * A computing cache for the uri fragments.
	 */
	private Map<EObject, String> fragmentsCache;

	/**
	 * The function used to compute the fragment of an {@link EObject}.
	 */
	private Function<EObject, String> fragmentComputation;

	/**
	 * Create a new {@link URIComputer}.
	 */
	public URIComputer() {
		locationCache = new AccessBasedLRUCache<EObject, FragmentIterable>(1000, 1000, .75F);
		fragmentsCache = new AccessBasedLRUCache<EObject, String>(1000, 1000, .75F);
		fragmentComputation = new EUriFragmentFunction();
	}

//	/**
//	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
//	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
//	 * different. "adding a fragment", "removing a fragment".
//	 * 
//	 * @param a
//	 *            First of the two {@link EObject}s to compare.
//	 * @param b
//	 *            Second of the two {@link EObject}s to compare.
//	 * @return The number of changes to transform one uri to another one.
//	 */
//	public int proximity(EObject a, EObject b) {
//		Iterable<String> aPath = getOrComputeLocation(a);
//		Iterable<String> bPath = getOrComputeLocation(b);
//		return proximity(aPath, bPath);
//	}

//	/**
//	 * Return a metric result URI similarities. It compares 2 lists of fragments and return an int
//	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
//	 * different. "adding a fragment", "removing a fragment".
//	 * 
//	 * @param aPath
//	 *            First of the two list of {@link String}s to compare.
//	 * @param bPath
//	 *            Second of the two list of {@link String}s to compare.
//	 * @return The number of changes to transform one uri to another one.
//	 */
//	public int proximity(Iterable<String> aPath, Iterable<String> bPath) {
//		int aSize = 0;
//		int bSize = 0;
//		Iterator<String> itA = aPath.iterator();
//		Iterator<String> itB = bPath.iterator();
//		boolean areSame = true;
//		int commonSegments = 0;
//		int remainingASegments = 0;
//		int remainingBSegments = 0;
//		while (itA.hasNext() && itB.hasNext() && areSame) {
//			String a = itA.next();
//			String b = itB.next();
//			if (a.equals(b)) {
//				commonSegments++;
//			} else {
//				areSame = false;
//			}
//			aSize++;
//			bSize++;
//
//		}
//		if (commonSegments == 0) {
//			return MAX_DISTANCE;
//		}
//		remainingASegments = aSize + Iterators.size(itA) - commonSegments;
//		remainingBSegments = bSize + Iterators.size(itB) - commonSegments;
//
//		int nbSegmentsToGoFromAToB = remainingASegments + remainingBSegments;
//		return (nbSegmentsToGoFromAToB * 10) / (commonSegments * 2 + nbSegmentsToGoFromAToB);
//	}
	
	public static class FragmentIterable implements Iterable<String> {
		public FragmentIterable(Iterable<String> data) {
			super();
			this.data = data;
		}

		private Iterable<String> data;
		
		static public FragmentIterable of(Iterable<String> data) {
			return new FragmentIterable(data);
		}

		@Override
		public Iterator<String> iterator() {
			return data.iterator();
		}
		
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public Iterable<String> apply(EObject input) {
		if(input.eIsProxy()) {
			String str = ((InternalEObject)input).eProxyURI().toString();
			return  Lists.newArrayList(str);
		}
		
		String result = ""; //$NON-NLS-1$
		EObject container = input.eContainer();
		if (container != null ) {
			result = retrieveFragment(input);
		} else {
			result = "0"; //$NON-NLS-1$
		}

		final List<String> resultList = Lists.newArrayList(result);
		if (container != null) {
			addAll(resultList, getOrComputeLocation(container));
		}
		return resultList;
	}

	private void addAll(List<String> resultList, FragmentIterable orComputeLocation) {
		Iterables.addAll(resultList, orComputeLocation.data);
	}

	/**
	 * The method return the location of an EObject represented as a list of fragments.
	 * 
	 * @param container
	 *            any EObject.
	 * @return a list of fragments.
	 */
	public FragmentIterable getOrComputeLocation(EObject container) {
		FragmentIterable result = locationCache.get(container);
		if (result == null) {
			result = FragmentIterable.of(apply(container));
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
//
//	/**
//	 * return the maximum value we can get for this distance.
//	 * 
//	 * @return the maximum value we can get for this distance.
//	 */
//	public int getUpperBoundDistance() {
//		return MAX_DISTANCE;
//	}

}
