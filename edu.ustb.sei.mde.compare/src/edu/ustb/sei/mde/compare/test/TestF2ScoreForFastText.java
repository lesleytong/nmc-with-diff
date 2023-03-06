package edu.ustb.sei.mde.compare.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import edu.ustb.sei.mde.compare.hashser.EObjectOneHotHasher;
import edu.ustb.sei.mde.compare.match.eobject.EditionDistance;

public class TestF2ScoreForFastText {

	static EObjectOneHotHasher oHasher = new EObjectOneHotHasher();

	static double TH = 0.59;

	static TestFastText testFastText = new TestFastText();

	static EditionDistance editionDistance = new EditionDistance();

	static float[][] W;

	static int rows;

	static int cols = 64;

	public static void main(String[] args) {

		init();

//		 simpleTest();

		allEClassImplTest();

	}

	private static void init() {

		testFastText.init();

		rows = testFastText.getFastText().getDimension();

		W = TestW.loadW(rows, cols);
	}

	private static void allEClassImplTest() {

		oHasher.reset();

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		Path directory = Paths.get("/Users/lesley/Documents/modelset/modelset/80-ecore/repo-ecore-all/data");

		try {
			
			long start = System.currentTimeMillis();

			// Collect all ecore files in a directory
			List<File> ecoreFiles = Files.walk(directory).filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".ecore")).map(Path::toFile)
					.collect(Collectors.toList());

			// Sort files by length
			Collections.sort(ecoreFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.compare(f1.length(), f2.length());
				}
			});

			List<EObject> list = new ArrayList<>();

			for (File file : ecoreFiles) {

				URI leftURI = URI.createFileURI(file.getAbsolutePath());
				Resource leftResource = resourceSet.getResource(leftURI, true);
				TreeIterator<EObject> iterator = leftResource.getAllContents();

				for (EObject e = null; iterator.hasNext();) {
					e = iterator.next();

					if (e.getClass().getName().contains("EClassImpl") == false) {
						continue;
					}

					list.add(e);

				}

			}

			int size = list.size();
			System.out.println("size: " + size);
			
			// 暂时设置size的大小为2000
			size = 500;

			long end = System.currentTimeMillis();
			System.out.println("Collect all EClassImpl EObjects: " + (end - start) + "ms.");

			// L2H metrics
			int L2H_TP = 0;
			int L2H_TN = 0;
			int L2H_FP = 0;
			int L2H_FN = 0;

			// one-hot metrics
			int OH_TP = 0;
			int OH_TN = 0;
			int OH_FP = 0;
			int OH_FN = 0;

			start = System.currentTimeMillis();

			for (int i = 0; i < size - 1; i++) {

				System.out.println("i: " + i);

				EObject aEObject = list.get(i);

				// one-hot
				int[] oneHotCode1 = oHasher.hash(aEObject);
				
				// L2H
				INDArray l2HCode1 = getL2HCode(aEObject);

				for (int j = 0; j < size; j++) {

					EObject bEObject = list.get(j);

					double[] dist = editionDistance.distance(aEObject, bEObject);

					// one-hot
					int[] oneHotCode2 = oHasher.hash(bEObject);

					double oneHotSim = EObjectOneHotHasher.oneHotJaccardSim(oneHotCode1, oneHotCode2);

					// L2H
					INDArray l2HCode2 = getL2HCode(bEObject);

					double jaccardDistance = Transforms.jaccardDistance(l2HCode1, l2HCode2);
					double jaccardSim = 1 - jaccardDistance;

					if (dist[1] < dist[0]) { // distanceFun thinks the pair are similar

						// L2H
						if (jaccardSim >= TH) {
							L2H_TP++;
						} else {
							L2H_FN++;
						}

						// one-hot
						if (oneHotSim >= TH) {
							OH_TP++;
						} else {
							OH_FN++;
						}

					} else {

						// L2H
						if (jaccardSim >= TH) {
							L2H_FP++;
						} else {
							L2H_TN++;
						}

						// one-hot
						if (oneHotSim >= TH) {
							OH_FP++;
						} else {
							OH_TN++;
						}

					}

				}
			}

			System.out.println();

			double L2H_Pr = getPrecision(L2H_TP, L2H_FP);
			System.out.println("L2H_Pr: " + L2H_Pr);

			double L2H_TPR = getTPR(L2H_TP, L2H_FN);
			System.out.println("L2H_TPR: " + L2H_TPR);

			double L2H_F2Score = getF2Score(L2H_Pr, L2H_TPR);
			System.out.println("L2H_F2Score: " + L2H_F2Score);

			System.out.println("\n===============================\n");

			double OH_Pr = getPrecision(OH_TP, OH_FP);
			System.out.println("OH_Pr: " + OH_Pr);

			double OH_TPR = getTPR(OH_TP, OH_FN);
			System.out.println("OH_TPR: " + OH_TPR);

			double OH_F2Score = getF2Score(OH_Pr, OH_TPR);
			System.out.println("OH_F2Score: " + OH_F2Score);

			System.out.println();

			end = System.currentTimeMillis();
			System.out.println("Compute F2 score: " + (end - start) + "ms.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void simpleTest() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		// URI branch1URI = URI.createFileURI(
		// "/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/Drn.ecore");
		// Resource branch1Resource = resourceSet.getResource(branch1URI, true);
		//
		// testMutate(branch1Resource);

		// URI leftURI = URI.createFileURI(
		// "/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/iot.ecore");
		// URI rightURI = URI.createFileURI(
		// "/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/iot1.ecore");
		//
		// Resource leftResource = resourceSet.getResource(leftURI, true);
		// Resource rightResource = resourceSet.getResource(rightURI, true);
		//
		// testSim(leftResource, rightResource);
		//
		// testL2Hash(leftResource, rightResource);
		//
		// testOneHot(leftResource, rightResource);
	}

	private static void testOneHot(Resource leftResource, Resource rightResource) {

		// When two resources are comparing, reset the oHasher
		oHasher.reset();

		TreeIterator<EObject> allContents = rightResource.getAllContents();
		List<EObject> list = new ArrayList<>();

		for (EObject e = null; allContents.hasNext();) {
			e = allContents.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			list.add(e);

		}

		TreeIterator<EObject> iterator = leftResource.getAllContents();
		iterator = leftResource.getAllContents();

		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;

		for (EObject e = null; iterator.hasNext();) {
			e = iterator.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> e: " + e);

			int[] oneHotCode1 = oHasher.hash(e);

			for (EObject target : list) {

				double[] dist = editionDistance.distance(e, target);

				int[] oneHotCode2 = oHasher.hash(target);

				double onehotSim = EObjectOneHotHasher.oneHotJaccardSim(oneHotCode1, oneHotCode2);

				if (dist[1] < dist[0]) {
					// distanceFun thinks the pair are similar
					if (onehotSim >= TH) {
						TP++;
					} else {
						FN++;
					}

				} else {
					if (onehotSim >= TH) {
						FP++;
					} else {
						TN++;
					}
				}

			}

		}

		double Pr = getPrecision(TP, FP);
		System.out.println("Pr: " + Pr);

		double TPR = getTPR(TP, FN);
		System.out.println("TPR: " + TPR);

		double F2Score = getF2Score(Pr, TPR);
		System.out.println("F2Score: " + F2Score);

		System.out.println();

	}

	private static void testSim(Resource leftResource, Resource rightResource) {

		TreeIterator<EObject> allContents = rightResource.getAllContents();
		List<EObject> list = new ArrayList<>();

		for (EObject e = null; allContents.hasNext();) {
			e = allContents.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			list.add(e);

		}

		TreeIterator<EObject> iterator = leftResource.getAllContents();
		iterator = leftResource.getAllContents();

		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;

		for (EObject e = null; iterator.hasNext();) {
			e = iterator.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> e: " + e);

			String str1 = oHasher.extractFeaturesForEObject(e); // Noted
			INDArray wordVector1 = testFastText.generateVectorIND(str1);

			for (EObject target : list) {

				double[] dist = editionDistance.distance(e, target);

				String str2 = oHasher.extractFeaturesForEObject(e); // Noted
				INDArray wordVector2 = testFastText.generateVectorIND(str2);

				// Compute cosine similarity between word vectors
				double jaccardDistance = Transforms.jaccardDistance(wordVector1, wordVector2);
				double jaccardSim = 1 - jaccardDistance;
				System.out.println(jaccardSim);

				if (dist[1] < dist[0]) {
					// distanceFun thinks the pair are similar
					if (jaccardSim >= TH) {
						TP++;
					} else {
						FN++;
					}

				} else {
					if (jaccardSim >= TH) {
						FP++;
					} else {
						TN++;
					}
				}

			}

		}

		double Pr = getPrecision(TP, FP);
		System.out.println("Pr: " + Pr);

		double TPR = getTPR(TP, FN);
		System.out.println("TPR: " + TPR);

		double F2Score = getF2Score(Pr, TPR);
		System.out.println("F2Score: " + F2Score);

		System.out.println();

	}

	private static void testL2Hash(Resource leftResource, Resource rightResource) {

		TreeIterator<EObject> allContents = rightResource.getAllContents();
		List<EObject> list = new ArrayList<>();

		for (EObject e = null; allContents.hasNext();) {
			e = allContents.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			list.add(e);

		}

		TreeIterator<EObject> iterator = leftResource.getAllContents();
		iterator = leftResource.getAllContents();

		int TP = 0;
		int TN = 0;
		int FP = 0;
		int FN = 0;

		for (EObject e = null; iterator.hasNext();) {
			e = iterator.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> e: " + e);

			INDArray l2HashCode1 = getL2HCode(e);

			for (EObject target : list) {

				double[] dist = editionDistance.distance(e, target);

				INDArray l2HashCode2 = getL2HCode(target);

				double jaccardDistance = Transforms.jaccardDistance(l2HashCode1, l2HashCode2);
				double jaccardSim = 1 - jaccardDistance;

				if (dist[1] < dist[0]) {
					// distanceFun thinks the pair are similar
					if (jaccardSim >= TH) {
						TP++;
					} else {
						FN++;
					}

				} else {
					if (jaccardSim >= TH) {
						FP++;
					} else {
						TN++;
					}
				}

			}

		}

		double Pr = getPrecision(TP, FP);
		System.out.println("Pr: " + Pr);

		double TPR = getTPR(TP, FN);
		System.out.println("TPR: " + TPR);

		double F2Score = getF2Score(Pr, TPR);
		System.out.println("F2Score: " + F2Score);

		System.out.println();

	}

	private static void testMutate(Resource resource) {

		List<EObject> contents = resource.getContents();
		TreeIterator<EObject> iterator = resource.getAllContents();

		for (EObject e = null; iterator.hasNext();) {
			e = iterator.next();

			if (e.getClass().getName().contains("EClassImpl") == false) {
				continue;
			}

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> e: " + e);

			EClass eClass = e.eClass();

			ElementMutator mutator = new ElementMutator(eClass);
			mutator.prepare(contents);

			mutator.doMutation(e);

			EObject mutant = mutator.getMutant(e);
			System.out.println("************************* mutant: " + mutant);
			// for (EStructuralFeature feature : eClass.getEAllStructuralFeatures()) {
			// System.out.println(feature.getName());
			// System.out.println(e.eGet(feature));
			// System.out.println(mutant.eGet(feature));
			// System.out.println();
			// }

			INDArray l2HashCode1 = getL2HCode(e);

			int TP = 0;
			int TN = 0;
			int FP = 0;
			int FN = 0;

			List<EObject> selectAll = mutator.selectAll();
			for (EObject target : selectAll) {
				System.out.println(target);

				double[] dist = editionDistance.distance(mutant, target);

				INDArray l2HashCode2 = getL2HCode(target);

				double cosineSim = Transforms.cosineSim(l2HashCode1, l2HashCode2);

				if (dist[1] < dist[0]) {
					// distanceFun thinks the pair are similar
					if (cosineSim >= TH) {
						TP++;
					} else {
						FN++;
					}

				} else {
					if (cosineSim >= TH) {
						FP++;
					} else {
						TN++;
					}
				}

				System.out.println();

			}

			double Pr = getPrecision(TP, FP);
			double TPR = getTPR(TP, FN);
			double F2Score = getF2Score(Pr, TPR);
			System.out.println("F2Score: " + F2Score);

			System.out.println();

		}
	}

	public static double getF2Score(double Pr, double TPR) {
		if (4 * Pr + TPR == 0) {
			return 0;
		}
		return 5 * ((Pr * TPR) / (4 * Pr + TPR));
	}

	public static double getPrecision(double TP, double FP) {
		if (TP + FP == 0) {
			return 0;
		}
		return TP / (TP + FP);
	}

	public static double getTPR(double TP, double FN) {
		if (TP + FN == 0) {
			return 0;
		}
		return TP / (TP + FN);
	}

	public static double cosineSimForL2H(int[] a, int[] b) {
		double dotProduct = 0;
		// Because the length is the same
		int n = a.length;

		for (int i = 0; i < n; i++) {
			dotProduct += a[i] * b[i];
		}

		return dotProduct / n;

	}
	
	public static INDArray getL2HCode(EObject e) {

		float[] hashCode = new float[cols];

		String str = oHasher.extractFeaturesForEObject(e);
		float[] floatVector = testFastText.generateVector(str);

		float[][] query_data = new float[1][rows];
		query_data[0] = floatVector;

		float[][] query_result = new float[1][cols];
		for (int i = 0; i < cols; i++) {
			float dotProduct = 0;
			for (int j = 0; j < rows; j++) {
				dotProduct += query_data[0][j] * W[j][i];
			}
			query_result[0][i] = dotProduct;
		}

		for (int i = 0; i < cols; i++) {
			if (query_result[0][i] > 0) {
				hashCode[i] = 1;
			} else {
				hashCode[i] = 0;
			}

		}

		INDArray create = Nd4j.create(hashCode);

		return create;

	}

}
