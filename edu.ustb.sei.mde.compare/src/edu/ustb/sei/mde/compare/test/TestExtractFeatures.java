package edu.ustb.sei.mde.compare.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.hashser.EObjectOneHotHasher;

public class TestExtractFeatures {

	public static void main(String[] args) {

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

		long start = System.currentTimeMillis();
		int count = 0;
		EObjectOneHotHasher oHasher = new EObjectOneHotHasher();

		Path directory = Paths.get("/Users/lesley/Documents/modelset/modelset/raw-data/repo-ecore-all/data");

		String storefilePath = "/Users/lesley/Documents/modelset/modelset/ecore-txt-all/ecore-txt-all-EClassImpl.txt";

		try {

			List<File> ecoreFiles = Files.walk(directory).filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".ecore")).map(Path::toFile)
					.collect(Collectors.toList());

			// Sort files by length
			Collections.sort(ecoreFiles, new Comparator<File>() {
				public int compare(File f1, File f2) {
					return Long.compare(f1.length(), f2.length());
				}
			});
			
			System.out.println("size: " + ecoreFiles.size());

			StringBuilder sb = new StringBuilder();

			for (File file : ecoreFiles) {

				String fileName = file.getName();

				URI leftURI = URI.createFileURI(file.getAbsolutePath());
				Resource leftResource = resourceSet.getResource(leftURI, true);
				TreeIterator<EObject> iterator = leftResource.getAllContents();

				for (EObject e = null; iterator.hasNext();) {
					e = iterator.next();

					Class<? extends EObject> cur_class = e.getClass();
					if (cur_class.getName().contains("EClassImpl") == false) {
						continue;
					}

					oHasher.extractFeaturesforWord2Vec(e, sb);

				}

				System.out.println(count++ + ": " + fileName);

			}

			File newFile = new File(storefilePath);
			if (!newFile.exists()) {
				newFile.getParentFile().mkdirs();
				newFile.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
				writer.write(sb.toString());
				writer.newLine();

			} catch (Exception e) {
				System.out.println(e);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("\n" + (end - start) + "ms. ");

	}

}
