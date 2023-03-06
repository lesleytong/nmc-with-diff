package edu.ustb.sei.mde.compare.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.EMFCompare;
import edu.ustb.sei.mde.compare.Match;
import edu.ustb.sei.mde.compare.internal.spec.ComparisonSpec;
import edu.ustb.sei.mde.compare.match.IMatchEngine;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryImpl;
import edu.ustb.sei.mde.compare.match.impl.MatchEngineFactoryRegistryImpl;
import edu.ustb.sei.mde.compare.scope.DefaultComparisonScope;
import edu.ustb.sei.mde.compare.scope.IComparisonScope;
import edu.ustb.sei.mde.compare.utils.UseIdentifiers;

public class TestSimilarityMatrix {

	public static void main(String[] args) {

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		URI leftURI = URI
				.createFileURI("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/bank_simple1.ecore");
		URI rightURI = URI
				.createFileURI("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/bank_simple2.ecore");

		Resource leftResource = resourceSet.getResource(leftURI, true);
		Resource rightResource = resourceSet.getResource(rightURI, true);

		// Compute the matching pairs
		// Never use identifiers
		long start = System.currentTimeMillis();
		IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl.createStandaloneInstance();
		final MatchEngineFactoryImpl matchEngineFactory = new MatchEngineFactoryImpl(UseIdentifiers.NEVER);
		matchEngineFactory.setRanking(20);
		registry.add(matchEngineFactory);

		EMFCompare build = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).build();
		IComparisonScope scope = new DefaultComparisonScope(leftResource, rightResource, null);
		
		Comparison comparisonSimilar = new ComparisonSpec();
		Map<EObject, List<EObject>> eObjecSimilarMap = new HashMap<>();
		
		TreeIterator<EObject> leftEObjects = leftResource.getAllContents();
		TreeIterator<EObject> rightEObjects = rightResource.getAllContents();
		
		// preparation		
		HashMap<EObject, Integer> leftMap = new HashMap<>();
		HashMap<EObject, Integer> rightMap = new HashMap<>();

		int rows = 0;
		for (EObject e = null; leftEObjects.hasNext();) {
			e = leftEObjects.next();
			leftMap.put(e, rows);
			rows++;
		}

		System.out.println("rows: " + rows);

		int columns = 0;
		for (EObject e = null; rightEObjects.hasNext();) {
			e = rightEObjects.next();
			rightMap.put(e, columns);
			columns++;
		}

		System.out.println("columns: " + columns);

		int offset = rows;
		int n = rows + columns;
		int[][] similarityMatrix = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					similarityMatrix[i][j] = 1;
				}
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("Time cost of first phase: " + (end - start) + "ms.");
		System.out.println();
		
		
		// compute the similar matrix
		start = System.currentTimeMillis();
		leftEObjects = leftResource.getAllContents();
		rightEObjects = rightResource.getAllContents();
		
		build.compareSimilar(comparisonSimilar, scope, leftEObjects, rightEObjects, eObjecSimilarMap);
		
		Set<Entry<EObject, List<EObject>>> entrySet = eObjecSimilarMap.entrySet();
		
		for(Entry<EObject, List<EObject>> entry : entrySet) {
			EObject key = entry.getKey();
			List<EObject> value = entry.getValue();
			
			int i = leftMap.get(key);
			for(EObject eObject : value) {
				int j = rightMap.get(eObject) + offset;
				similarityMatrix[i][j] = similarityMatrix[j][i] = 1;
				
			}
			
		}

		end = System.currentTimeMillis();
		System.out.println("Time cost of second phase: " + (end - start) + "ms.");
		System.out.println();
		
		// Save as file
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<similarityMatrix.length; i++) {
			for(int j=0; j<similarityMatrix[0].length; j++) {
				builder.append(similarityMatrix[i][j] + " ");
			}
			builder.append("\n");
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/SimilarityMatrix.txt"));
			writer.write(builder.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		System.out.println("File saved.");
		
	}
}
