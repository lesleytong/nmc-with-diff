package edu.ustb.sei.mde.compare.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.EMFCompare;
import edu.ustb.sei.mde.compare.match.IMatchEngine;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryImpl;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryRegistryImpl;
import edu.ustb.sei.mde.compare.merge.BatchMerger;
import edu.ustb.sei.mde.compare.merge.IBatchMerger;
import edu.ustb.sei.mde.compare.merge.IMerger;
import edu.ustb.sei.mde.compare.scope.DefaultComparisonScope;
import edu.ustb.sei.mde.compare.scope.IComparisonScope;
import edu.ustb.sei.mde.compare.utils.UseIdentifiers;

public class TestEMFCompare3 {
	public static void main(String[] args) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		URI baseURI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\edu\\ustb\\sei\\mde\\compare\\test\\bank_m1.ecore");
		URI branch1URI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\edu\\ustb\\sei\\mde\\compare\\test\\bank_merge.ecore");
//		URI branch2URI = URI
//				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank3.ecore");

		List<URI> uriList = new ArrayList<>();
		uriList.add(baseURI);
		uriList.add(branch1URI);
//		uriList.add(branch2URI);

		int size = uriList.size();
		List<Resource> resourceList = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			Resource resource = resourceSet.getResource(uriList.get(i), true);
			resourceList.add(resource);
		}

		// never use identifiers
		IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl.createStandaloneInstance();
		final MatchEngineFactoryImpl matchEngineFactory = new MatchEngineFactoryImpl(UseIdentifiers.NEVER);
		matchEngineFactory.setRanking(20);
		registry.add(matchEngineFactory);

		// 只使用一个EMFCompare
		EMFCompare build = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).build();

		size = resourceList.size();
		Comparison comparison = null;
		IComparisonScope scope = null;
		Resource baseResource = resourceList.get(0);
		Resource leftResource = null;
		
		for(int i=1; i<size; i++) {
			System.out.println("\n\ni: " + i);
			leftResource = resourceList.get(i);
			scope = new DefaultComparisonScope(leftResource, baseResource, null);
			comparison = build.compare(scope);
			
			System.out.println("**************************Match");
			comparison.getMatches().forEach(m -> {
				System.out.println(m);
				m.getAllSubmatches().forEach(sm -> {
					System.out.println(sm);
				});
			});

			System.out.println("\n\n**************************Diff");
			comparison.getDifferences().forEach(diff -> {
				System.out.println("diff.getSource(): " + diff.getSource());
				System.out.println("diff.getMatch(): " + diff.getMatch());
				System.out.println("diff: " + diff);
				System.out.println("---");
			});
								
		}
											
		IBatchMerger merger = new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance());
		
		IMerger.Registry mergerRegistry = IMerger.RegistryImpl.createStandaloneInstance();
		
		for(Diff diff : comparison.getDifferences()) {
			mergerRegistry.getHighestRankingMerger(diff).copyLeftToRight(diff, new BasicMonitor()); 
			
		}
		
		
	}
}
