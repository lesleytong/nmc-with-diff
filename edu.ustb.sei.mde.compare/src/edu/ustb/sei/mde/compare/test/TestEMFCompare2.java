package edu.ustb.sei.mde.compare.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.Diff;
import edu.ustb.sei.mde.compare.DifferenceSource;
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

public class TestEMFCompare2 {
	public static void main(String[] args) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		URI m2URI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank_m2.ecore");

		URI baseURI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank.ecore");
		URI branch1URI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank1.ecore");
		URI branch2URI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank2.ecore");
		URI branch3URI = URI
				.createFileURI("E:\\eclipse-dsl202203\\edu.ustb.sei.mde.compare\\src\\test\\bank3.ecore");

		List<URI> uriList = new ArrayList<>();
		uriList.add(baseURI);
		uriList.add(branch1URI);
		uriList.add(branch2URI);
		uriList.add(branch3URI);

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
		Resource leftResource = resourceList.get(1);
		Resource rightResource = null;
		IBatchMerger merger = new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance());
		
		for(int i=2; i<size; i++) {
			rightResource = resourceList.get(i);
			scope = new DefaultComparisonScope(leftResource, rightResource, baseResource);
			comparison = build.compare(scope);
			
			Set<Diff> rightDiffs = new HashSet<>();
			comparison.getDifferences().forEach(d -> {
				if (d.getSource() == DifferenceSource.RIGHT) {
					rightDiffs.add(d);
				}
			});
			merger.copyAllRightToLeft(rightDiffs, null);
		}
		
		try {
			leftResource.setURI(m2URI);
			leftResource.save(null);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		System.out.println("写入到合并文件 [" + m2URI + "]");
		
		
	}
}
