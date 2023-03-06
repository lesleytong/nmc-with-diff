package edu.ustb.sei.mde.compare.test;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import edu.ustb.sei.mde.compare.match.eobject.EditionDistance;

public class TestEObjectMutate {
	
	static EditionDistance edition = new EditionDistance();
	static ElementMutator mutator;
	
	public static void main(String[] args) {
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		
		URI branch1URI = URI.createFileURI("/Users/lesley/git/nmc-with-diff/edu.ustb.sei.mde.compare/src/edu/ustb/sei/mde/compare/test/Drn.ecore");
		Resource branch1Resource = resourceSet.getResource(branch1URI, true);
		
		
		TreeIterator<EObject> iterator = branch1Resource.getAllContents();	
		List<EObject> contents = branch1Resource.getContents();
		
	
		mutator.prepare(contents);
		
		
	}
	
	public static void testMutate(EObject e) {
		EClass eClass = e.eClass();
		mutator = new ElementMutator(eClass);
		
		mutator.doMutation(e);
		
		EObject mutant = mutator.getMutant(e);
		
		List<EObject> selectAll = mutator.selectAll();
		
		for(EObject target : selectAll) {
			System.out.println(target);
			
			checkBelong(target, mutant);
			
			
		}
		
		
	}
	
	 public static boolean checkBelong(final EObject original, final EObject mutant) {
		    boolean _xblockexpression = false;
		    {
		      final double dist_thresh = Math.max(edition.getThresholdAmount(original), edition.getThresholdAmount(mutant));
		      System.out.println(dist_thresh);
		      
		      final double[] dist = edition.distance(original, mutant);
		      System.out.println(dist[0]);
		      
		      final boolean belong = (dist[1] < dist_thresh);
		      _xblockexpression = belong;
		    }
		    return _xblockexpression;
		    }
	
}
