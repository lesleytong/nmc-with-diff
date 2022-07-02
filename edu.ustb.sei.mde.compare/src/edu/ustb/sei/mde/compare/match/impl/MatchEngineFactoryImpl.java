/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package edu.ustb.sei.mde.compare.match.impl;

import edu.ustb.sei.mde.compare.match.DefaultComparisonFactory;
import edu.ustb.sei.mde.compare.match.DefaultEqualityHelperFactory;
import edu.ustb.sei.mde.compare.match.DefaultMatchEngine;
import edu.ustb.sei.mde.compare.match.IComparisonFactory;
import edu.ustb.sei.mde.compare.match.IMatchEngine;
import edu.ustb.sei.mde.compare.match.eobject.IEObjectMatcher;
import edu.ustb.sei.mde.compare.match.eobject.WeightProvider;
import edu.ustb.sei.mde.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import edu.ustb.sei.mde.compare.scope.IComparisonScope;
import edu.ustb.sei.mde.compare.utils.UseIdentifiers;

/**
 * The default implementation of the {@link IMatchEngine.Factory.Registry}.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.0
 */
public class MatchEngineFactoryImpl implements IMatchEngine.Factory {

	/** The match engine created by this factory. */
	protected IMatchEngine matchEngine;

	/** Ranking of this match engine. */
	private int ranking;

	/** A match engine needs a WeightProvider in case of this match engine do not use identifiers. */
	private WeightProvider.Descriptor.Registry weightProviderRegistry;

	/**
	 * Constructor that instantiate a {@link DefaultMatchEngine}. This match engine will use a the standalone
	 * weight provider registry {@link WeightProviderDescriptorRegistryImpl.createStandaloneInstance()}.
	 */
	public MatchEngineFactoryImpl() {
		this(UseIdentifiers.WHEN_AVAILABLE, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
	}

	/**
	 * Constructor that instantiate a {@link DefaultMatchEngine} that will use identifiers as specified by the
	 * given {@code useIDs} enumeration. This match engine will use a the standalone weight provider registry
	 * {@link WeightProviderDescriptorRegistryImpl.createStandaloneInstance()}.
	 * 
	 * @param useIDs
	 *            the kinds of matcher to use.
	 */
	public MatchEngineFactoryImpl(UseIdentifiers useIDs) {
		this(useIDs, WeightProviderDescriptorRegistryImpl.createStandaloneInstance());
	}

	/**
	 * Constructor that instantiate a {@link DefaultMatchEngine} that will use identifiers as specified by the
	 * given {@code useIDs} enumeration.
	 * 
	 * @param useIDs
	 *            the kinds of matcher to use.
	 * @param registry
	 *            A match engine needs a WeightProvider in case of this match engine do not use identifiers.
	 */
	public MatchEngineFactoryImpl(UseIdentifiers useIDs, WeightProvider.Descriptor.Registry registry) {
		final IComparisonFactory comparisonFactory = new DefaultComparisonFactory(
				new DefaultEqualityHelperFactory());
		weightProviderRegistry = registry;
		final IEObjectMatcher matcher = DefaultMatchEngine.createDefaultEObjectMatcher(useIDs,
				weightProviderRegistry);
		matchEngine = new DefaultMatchEngine(matcher, comparisonFactory);
	}

	/**
	 * Constructor that instantiate a {@link DefaultMatchEngine} with the given parameters.
	 * 
	 * @param matcher
	 *            The matcher that will be in charge of pairing EObjects together for this comparison process.
	 * @param comparisonFactory
	 *            factory that will be use to instantiate Comparison as return by match() methods.
	 */
	public MatchEngineFactoryImpl(IEObjectMatcher matcher, IComparisonFactory comparisonFactory) {
		matchEngine = new DefaultMatchEngine(matcher, comparisonFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.match.IMatchEngine.Factory#getMatchEngine()
	 */
	public IMatchEngine getMatchEngine() {
		return matchEngine;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.match.IMatchEngine.Factory#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.match.IMatchEngine.Factory#setRanking(int)
	 */
	public void setRanking(int r) {
		ranking = r;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see edu.ustb.sei.mde.compare.match.IMatchEngine.Factory#isMatchEngineFactoryFor(edu.ustb.sei.mde.compare.scope.IComparisonScope)
	 */
	public boolean isMatchEngineFactoryFor(IComparisonScope scope) {
		return true;
	}

	/**
	 * The match engine needs a WeightProvider in case of this match engine do not use identifiers.
	 * 
	 * @param registry
	 *            the registry to associate with the match engine.
	 */
	void setWeightProviderRegistry(WeightProvider.Descriptor.Registry registry) {
		this.weightProviderRegistry = registry;
	}

}
