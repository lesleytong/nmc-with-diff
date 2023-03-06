package edu.ustb.sei.mde.compare.test;


import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.Comparison;
import edu.ustb.sei.mde.compare.internal.spec.ComparisonSpec;
import edu.ustb.sei.mde.compare.match.eobject.EditionDistance;

public class TestDistanceFunction {

	public static void main(String[] args) {
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		long start = System.currentTimeMillis();
		
		URI leftURI = URI.createFileURI("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/bank1.ecore");
		Resource leftResource = resourceSet.getResource(leftURI, true);
		
		URI rightURI = URI.createFileURI("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/bank2.ecore");
		Resource rightResource = resourceSet.getResource(rightURI, true);		
		

		
		EditionDistance edition = new EditionDistance();
		
		EObject left_Account = null;
		EObject left_Bank = null;
		EObject left_CurrentAccount = null;
		EObject right_Account = null;
		EObject right_SavingAccount = null;
		
		int leftCount=0;
		TreeIterator<EObject> iterator = leftResource.getAllContents();	
		
		for(EObject e = null; iterator.hasNext();) {
			e = iterator.next();
			EClass eClass = e.eClass();
						
			if(eClass.getName().equals("EClass")) {
				if(leftCount==0) {
					left_Bank = e;
				} else if(leftCount==1) {
					left_Account = e;
				} else if(leftCount == 5) {
					left_CurrentAccount = e;
					break;
				} 
				leftCount++;
			}
		}
		
		int rightCount = 0;
		iterator = rightResource.getAllContents();
		
		for(EObject e = null; iterator.hasNext();) {
			e = iterator.next();
			EClass eClass = e.eClass();
						
			if(eClass.getName().equals("EClass")) {
				if(rightCount == 1) {
					right_Account = e;
				} else if(rightCount == 4) {
					right_SavingAccount = e;
					break;
				}
				rightCount++;
			}
		}
		
		
		Comparison compariosn = new ComparisonSpec();
		double distance;
		double[] answer;
		
//		distance = edition.distance(compariosn, left_Account, left_Bank);
//		System.out.println(distance);
//		
//		distance = edition.distance(compariosn, left_Account, left_CurrentAccount);
//		System.out.println(distance);
//
//		System.out.println("-----");
//		
//		distance = edition.distance(left_Account, left_Bank);
//		System.out.println(distance);
//		
//		distance = edition.distance(left_Account, left_CurrentAccount);
//		System.out.println(distance);
		
		
		System.out.println("**********************************");
		
		distance = edition.distance(compariosn, left_Account, right_Account);
		System.out.println(distance);
		
		distance = edition.distance(compariosn, left_CurrentAccount, right_SavingAccount);
		System.out.println(distance);
		
		System.out.println("-----");
		
		answer = edition.distance(left_Account, right_Account);
		System.out.println(distance);
		
		answer = edition.distance(left_CurrentAccount, right_SavingAccount);
		System.out.println(distance);
		
				
		long end = System.currentTimeMillis();
		System.out.println("\n" + (end - start) + "ms. ");
		
	}
}
