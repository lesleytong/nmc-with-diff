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
package edu.ustb.sei.mde.compare.match.eobject.internal;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.match.eobject.EObjectIndex;
import edu.ustb.sei.mde.compare.match.eobject.ProximityEObjectMatcher;
import edu.ustb.sei.mde.compare.match.eobject.ScopeQuery;
import edu.ustb.sei.mde.compare.match.eobject.ProximityEObjectMatcher.DistanceFunction;

/**
 * An implementation of EObjectIndex which segregates given EObjects using their type and then delegate to
 * other indexes.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ByTypeIndex implements EObjectIndex, MatchAheadOfTime {
	/**
	 * All the type specific indexes, created on demand.
	 */
	private Map<String, EObjectIndex> allIndexes;

	/**
	 * The distance function to use to create the delegates indexes.
	 */
	private DistanceFunction meter;

	/**
	 * An instance responsible for telling whether something is in the scope or not.
	 */
	private ScopeQuery scope;

	/**
	 * Create a new instance using the given {@link DistanceFunction} to instantiate delegate indexes on
	 * demand.
	 * 
	 * @param meter
	 *            the function passed when instantiating delegate indexes.
	 * @param scope
	 *            an instance
	 */
	public ByTypeIndex(ProximityEObjectMatcher.DistanceFunction meter, final ScopeQuery scope) {
		this.meter = meter;
		this.scope = scope;
		this.allIndexes = Maps.newHashMap();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#getValuesStillThere(org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public Iterable<EObject> getValuesStillThere(Side side) {
		List<Iterable<EObject>> allLists = Lists.newArrayList();
		for (EObjectIndex typeSpecificIndex : allIndexes.values()) {
			allLists.add(typeSpecificIndex.getValuesStillThere(side));
		}
		return Iterables.concat(allLists);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#findClosests(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side, int)
	 */
	public Map<Side, EObject> findClosests(Comparison inProgress, EObject obj, Side side) {
		EObjectIndex typeSpecificIndex = getOrCreate(obj);
		return typeSpecificIndex.findClosests(inProgress, obj, side);
	}

	/**
	 * Get the index used to store this object, create a new one if needed.
	 * 
	 * @param obj
	 *            any EObject.
	 * @return the index used to store this object, create a new one if needed.
	 */
	private EObjectIndex getOrCreate(EObject obj) {
		String key = eClassKey(obj);
		EObjectIndex found = allIndexes.get(key);
		if (found == null) {
			found = new ProximityIndex(meter, scope);
			allIndexes.put(key, found);
		}
		return found;
	}

	/**
	 * Compute a key identifying the EClass of the given EObject.
	 * 
	 * @param obj
	 *            any eObject.
	 * @return a key for its EClass.
	 */
	private String eClassKey(EObject obj) {
		EClass clazz = obj.eClass();
		if (clazz.getEPackage() != null) {
			return clazz.getEPackage().getNsURI() + ":" + clazz.getName(); //$NON-NLS-1$
		}
		return clazz.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#remove(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public void remove(EObject obj, Side side) {
		EObjectIndex typeSpecificIndex = getOrCreate(obj);
		typeSpecificIndex.remove(obj, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.eobject.EObjectIndex#index(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.compare.match.eobject.EObjectIndex.Side)
	 */
	public void index(EObject eObjs, Side side) {
		EObjectIndex typeSpecificIndex = getOrCreate(eObjs);
		typeSpecificIndex.index(eObjs, side);
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<EObject> getValuesToMatchAhead(Side side) {
		List<Iterable<EObject>> allLists = Lists.newArrayList();
		for (MatchAheadOfTime typeSpecificIndex : Iterables.filter(allIndexes.values(),
				MatchAheadOfTime.class)) {
			allLists.add(typeSpecificIndex.getValuesToMatchAhead(side));
		}
		return Iterables.concat(allLists);
	}

	// lyt
	public Map<Side, EObject> findClosests(Comparison inProgress, EObject obj, Side side,
			MultiKeyMap<EObject, Double> distanceMap) {
		EObjectIndex typeSpecificIndex = getOrCreate(obj);
		return typeSpecificIndex.findClosests(inProgress, obj, side, distanceMap);
	}
	
	// lyt
	public Map<Side, EObject> findClosests(Comparison inProgress, EObject obj, Side side,
			Map<EObject, List<EObject>> eObjectSimilarMap) {
		EObjectIndex typeSpecificIndex = getOrCreate(obj);
		return typeSpecificIndex.findClosests(inProgress, obj, side, eObjectSimilarMap);
	}

}
