package edu.ustb.sei.mde.compare.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.nd4j.linalg.api.ndarray.INDArray;

import edu.ustb.sei.mde.compare.hashser.EObjectOneHotHasher;
import edu.ustb.sei.mde.compare.match.eobject.EditionDistance;

public class TestMatrixForWord2Vec {
	
	static String similarMatirxDir = "/Users/lesley/Documents/modelset/modelset/similarityMatrix/word2vec";
	static String featureMatirxDir = "/Users/lesley/Documents/modelset/modelset/featureMatirx/word2vec";
	
	static EObjectOneHotHasher oHasher = new EObjectOneHotHasher();
	
	static TestWord2Vec testWord2Vec = new TestWord2Vec();

	public static void main(String[] args) {
		
		// Collect all ecore files in a directory,
		// and partition them by type.
		Path directory = Paths.get("/Users/lesley/Documents/modelset/modelset/80-ecore/repo-ecore-all/data");
		HashMap<Class<? extends EObject>, List<EObject>> partitionByTypeMap = new HashMap<>();
		
		long start = System.currentTimeMillis();
		
		partitionByType(directory, partitionByTypeMap);
		
		System.out.println();
		
		long end = System.currentTimeMillis();
		System.out.println("\n" + "Partition by type: " + (end - start) + "ms. ");
		
		System.out.println();
		

		// Compute each similarityMatrix and featureMatrix by type.
		start = System.currentTimeMillis();
				
		computeMatrix(partitionByTypeMap);
		
		end = System.currentTimeMillis();
		System.out.println("\n" + "Compute similarity and feature matrix: " + (end - start) + "ms. ");
		
		System.out.println();
			
	}

	/**
	 * Compute the similarity Matrix and feature Matrix.
	 * @param partitionByTypeMap
	 * @param similarityMatrixMap
	 * @param featureMatrixMap
	 */
	private static void computeMatrix(HashMap<Class<? extends EObject>, List<EObject>> partitionByTypeMap) {
		
		Set<Entry<Class<? extends EObject>, List<EObject>>> entrySet = partitionByTypeMap.entrySet();
		EditionDistance edition = new EditionDistance();
		
		int cnt = 1;
		
		int simCount = 0;
		int notSimCount = 0;
		
		for(Entry<Class<? extends EObject>, List<EObject>> entry : entrySet) {
			
			long start = System.currentTimeMillis();
			Class<? extends EObject> cur_class = entry.getKey();
			List<EObject> list = entry.getValue();
			
			String cur_class_name = cur_class.getName();
			int size = list.size();
			
			System.out.println("Process: " + cur_class_name + ", size: " + size);
			
			// 先只看EClassImpl
			if(cur_class_name.contains("EClassImpl") == false) {
				continue;
			}
			
			// 减小为10000，不然内存不够
			size = 10000;
			
			float[][] similarityMatrix = new float[size][size];
			
			// Compare two EObjects by distance function.
			for(int i=0; i<size-1; i++) {
				EObject aEObject = list.get(i);
				System.out.println("i: " + i);
				
				for(int j=i; j<size; j++) {					
					if(j == i) {
						similarityMatrix[i][j] = 1;
						continue;
					}
					
					EObject bEObject = list.get(j);
					double[] distance = edition.distance(aEObject, bEObject);
					
					if(distance[1] == Double.MAX_VALUE) { // not similar
						similarityMatrix[i][j] = similarityMatrix[j][i] = -1;
						notSimCount++;
						
					} else if(distance[1] < Double.MAX_VALUE){ // similar
						similarityMatrix[i][j] = similarityMatrix[j][i] = 1; 
						simCount++;
					} 
									
					// if the pairwise relation is undefined, the distance is 0.
					// So far, I compare every two EObject.
				}
			}
			
			long end = System.currentTimeMillis();
			System.out.println("Compute similarity matrix: " + (end - start) + "ms. ");
			
			start = System.currentTimeMillis();
			saveMatrix(cur_class_name, similarityMatrix, similarMatirxDir);
			end = System.currentTimeMillis();
			System.out.println("Save similarity matrix: " + (end-start) + "ms. ");
			
			System.out.println("*** simCount: " + simCount);
			System.out.println("*** notSimCount: " + notSimCount);
			
			
			// Produce vector for each EObject in the list.
			// Use pre-trained doc2vec model.
			
			start = System.currentTimeMillis();
			
			testWord2Vec.init();
			
			float[][] featureMatrix = new float[size][testWord2Vec.getWord2Vec().getLayerSize()];
			
			for(int i=0; i<size; i++) {
				EObject e = list.get(i);
				
				String str = oHasher.extractFeaturesForEObject(e);	// Noted
				featureMatrix[i] = testWord2Vec.generateVector(str);
			}
			
			end = System.currentTimeMillis();
			System.out.println("Compute feature matrix: " + (end - start) + "ms. ");
			
			start = System.currentTimeMillis();
			saveMatrix(cur_class_name, featureMatrix, featureMatirxDir);
			end = System.currentTimeMillis();
			System.out.println("Save feature matrix: " + (end - start) + "ms. ");
			
		}
	}

	/**
	 * Collect all ecore files in a directory, 
	 * and partition them by type.
	 * @param directory
	 * @param partitionByTypeMap
	 */
	private static void partitionByType(Path directory,
			HashMap<Class<? extends EObject>, List<EObject>> partitionByTypeMap) {
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
	
		try {
			
			int count=0;
			// Collect all ecore files in a directory
			List<File> ecoreFiles = Files.walk(directory)
					.filter(Files::isRegularFile)
					.filter(path ->  path.toString().endsWith(".ecore"))
					.map(Path::toFile)
					.collect(Collectors.toList());
		
			// Sort files by length
			Collections.sort(ecoreFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.compare(f1.length(), f2.length());
				}
			});
			
			for(File file : ecoreFiles) {
				URI leftURI = URI.createFileURI(file.getAbsolutePath());
				Resource leftResource = resourceSet.getResource(leftURI, true);
				TreeIterator<EObject> iterator = leftResource.getAllContents();	
				
				for(EObject e = null; iterator.hasNext();) {
					e = iterator.next();
					Class<? extends EObject> cur_class = e.getClass();
					List<EObject> list = partitionByTypeMap.get(cur_class);
					
					if(list == null) {
						list = new ArrayList<>();
						partitionByTypeMap.put(cur_class, list);
					} 
					
					list.add(e);

				}
				
				System.out.println("file_" + count);	// start from 0
				count++;
			}    
			
			// Print partitionByTypeMap
			Set<Entry<Class<? extends EObject>, List<EObject>>> entrySet = partitionByTypeMap.entrySet();
			
			for(Entry<Class<? extends EObject>, List<EObject>> entry : entrySet) {
				Class<? extends EObject> cur_class = entry.getKey();
				List<EObject> list = entry.getValue();
				
				System.out.println(cur_class.getName() + ", size: " + list.size());
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void saveMatrix(String cur_class_name, float[][] matrix, String dir) {
		
		if(matrix==null || matrix[0]==null) {
			return;
		}
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<matrix.length; i++) {
			for(int j=0; j<matrix[0].length; j++) {
				builder.append(matrix[i][j] + " ");
			}
			builder.append("\n");
		}
				
		try {
			String fileName = cur_class_name.replace(".", "_");
			String fileSavedPath = dir + "/" + fileName + ".txt";
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileSavedPath));
			writer.write(builder.toString());
			writer.close();
			
			System.out.println(fileName + ".txt " + "is saved.");
			
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	/**
	 * Save MatrixMap as file
	 * @param matrixMap
	 * @param dir
	 */
	private static void saveMatrixMap(HashMap<Class<? extends EObject>, float[][]> matrixMap,
			String dir) {
		
		Set<Entry<Class<? extends EObject>, float[][]>> entrySet = matrixMap.entrySet();
		
		for(Entry<Class<? extends EObject>, float[][]> entry : entrySet) {
			Class<? extends EObject> cur_class = entry.getKey();
			float[][] matirx = entry.getValue();
			
			if(matirx==null || matirx[0] == null) {
				return;
			}
			
			StringBuilder builder = new StringBuilder();
			for(int i=0; i<matirx.length; i++) {
				for(int j=0; j<matirx[0].length; j++) {
					builder.append(matirx[i][j] + " ");
				}
				builder.append("\n");
			}
			
			try {
				
				String fileName = cur_class.getName().replace(".", "_");
				String fileSavedPath = dir + "/" + fileName + ".txt";
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileSavedPath));
				writer.write(builder.toString());
				writer.close();
				
				System.out.println(fileName + ".txt " + "is saved.");
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
}
