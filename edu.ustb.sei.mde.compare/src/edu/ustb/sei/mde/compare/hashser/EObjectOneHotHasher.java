package edu.ustb.sei.mde.compare.hashser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.google.common.collect.Iterables;
import edu.ustb.sei.mde.compare.hashser.URIComputer;
import edu.ustb.sei.mde.compare.hashser.URIComputer.FragmentIterable;

/**
 * The basic idea of onehot hasher is as follows.
 * <ol>
 * <li>Build the word table.</li>
 * <li>Construct a word bag of each EObject.</li>
 * </ol>
 * @author hexiao
 *
 */
public class EObjectOneHotHasher {
	
	private static final Map<Character, String> DICTIONARY = new HashMap<Character, String>() {
		{
			put('0', "zero");
			put('1', "one");
			put('2', "two");
			put('3', "three");
			put('4', "four");
			put('5', "five");
			put('6', "six");
			put('7', "seven");
			put('8', "eight");
			put('9', "nine");
		}
	};
	
	
//	protected int worldSize = 2048;
	
	protected URIComputer uriEncoder = new URIComputer();
//	protected NGramSplitter stringSplitter = new NGramSplitter();
	
	public void reset() {
		uriEncoder = new URIComputer();
		wordTable.clear();
	}
	public EObjectOneHotHasher() {
//		words = new HashSet<>(1000);
//		wordBagMap = new HashMap<>(1000);
		wordTable = new HashMap<>(2048);
//		vectorMap = new HashMap<>();
	}
	
//	protected Set<Object> words;
	protected Map<Object, Integer> wordTable;
//	protected Map<EObject, Set<Object>> wordBagMap;
//	protected Map<EObject, int<Integer>> vectorMap;
	
	public int[] hash(EObject data) {
		Set<Object> bow = extractWordBag(data);		
		int[] code = new int[bow.size()];
		
		Iterator<Object> itr = bow.iterator();
		for(int i=0;i<code.length;i++) {
			Object v = itr.next();
			int c = getWordID(v);
			code[i] = c;
		}
		
		Arrays.sort(code);
		
		return code;
	}
	
	synchronized public Integer getWordID(Object v) {
		return wordTable.computeIfAbsent(v, x->{
			return wordTable.size();
		});
	}
	
	static public double oneHotJaccardSim(int[] code1, int[] code2) {
		int size = 0;
		for(int i=0, j=0;i<code1.length && j< code2.length;) {
			if(code1[i]>code2[j]) j++;
			else if(code1[i]<code2[j]) i++;
			else {
				size++;
				i++;
				j++;
			}
		}
		return ((double) size) / (code1.length + code2.length - size);
	}
	
//	public void prehash(EObject data) {
//		Set<Object> bow = extractWordBag(data);
//		
//		
//		words.addAll(bow);
//		wordBagMap.put(data, bow);
//	}
	
//	public void doHash() {
//		buildWordTable();
//		buildHashVectors();
//	}
	
//	private void buildHashVectors() {
//		wordBagMap.entrySet().forEach(e->{
//			Set<Integer> vec = new HashSet<>();
//			e.getValue().forEach(o->{
//				vec.add(wordTable.get(o));
//			});
//			vectorMap.put(e.getKey(), vec);
//		});
//	}

//	private void buildWordTable() {
//		ArrayList<Object> list = new ArrayList<>(words);
//		Collections.shuffle(list);
//		for(int i = 0;i<list.size(); i++) {
//			wordTable.put(list.get(i), i % worldSize);
//		}
//	}

	@SuppressWarnings("unchecked")
	protected Set<Object> extractWordBag(EObject object) {
		Set<Object> bag = new HashSet<>();
		EClass clazz = object.eClass();		
		for(EStructuralFeature feature : getHashableFeatures(clazz)) {
			
			Object raw = object.eGet(feature);
						
			if(raw!=null) {
								
				if(feature.isMany()) {
					if(feature instanceof EReference) {
						List<EObject> objs = (List<EObject>) raw;
						for(EObject o : objs) extractURI(o, bag);
					} else {
						List<Object> objs = (List<Object>) raw;
						for(Object o : objs) extractValue(o, bag);
					}
				} else {
					if(feature instanceof EReference) {
						extractURI((EObject)raw, bag);
					} else {
						extractValue(raw, bag);
					}
				}
			}
		}
		
		return bag;
	}
	
	/**
	 * Count all the features of an EObject
	 * @param object
	 * @param clazzMap
	 * @param featureMap
	 * @param classFeaturesMap
	 */
	protected void countFeatures(EObject object, HashMap<EClass, Integer> clazzMap, LinkedHashMap<EStructuralFeature, Integer> featureMap, Map<EClass, Set<EStructuralFeature>> classFeaturesMap) {
		EClass clazz = object.eClass();	
		
		if(!clazzMap.containsKey(clazz)) {
			clazzMap.put(clazz, -(clazzMap.size()+1));
			
			// Because skip some classes
			HashSet<EStructuralFeature> hashSet = new HashSet<>();
			classFeaturesMap.put(clazz, hashSet);
			
			for(EStructuralFeature feature : getHashableFeatures(clazz)) {	
				hashSet.add(feature);
				
				if(!featureMap.containsKey(feature)) {
					featureMap.put(feature, featureMap.size()+1);
				}
			}
			
		}
	}
	
	
	public String extractFeaturesForEObject(EObject object) {
		
		StringBuilder sb = new StringBuilder();
		
		EClass eClass = object.eClass();
		
		for(EStructuralFeature feature : getHashableFeatures(eClass)) {
					
			Object defaultValue = feature.getDefaultValue();
			Object rawValue = object.eGet(feature);
			
			if(defaultValue == rawValue) {
				continue;
			}
						
			if(rawValue != null) {
				
				StringBuilder current_sb = new StringBuilder();
								
				if(feature.isMany()) {
					if(feature instanceof EReference) {
						List<EObject> objs = (List<EObject>) rawValue;
						for(EObject o : objs) extractURI(o, current_sb);
					} else {
						List<Object> objs = (List<Object>) rawValue;
						for(Object o : objs) extractValue(o, current_sb);
					}
				} else {
					if(feature instanceof EReference) {
						extractURI((EObject)rawValue, current_sb);
					} else {
						extractValue(rawValue, current_sb);
					}
				}
								
				String tmp = current_sb.toString();
				if(tmp.length()>0 && tmp.contains("file:")==false) {
					String token = convertDigitToWord(tmp.toLowerCase());
					sb.append(token);
				}
				
			} 
		}
		
		return sb.toString();
		
	}
	

	public void extractFeaturesForDoc2Vec(EObject object, StringBuilder sb) {
		
		EClass eClass = object.eClass();
		
		for(EStructuralFeature feature : getHashableFeatures(eClass)) {
					
			Object defaultValue = feature.getDefaultValue();
			Object rawValue = object.eGet(feature);
			
			if(defaultValue == rawValue) {
				continue;
			}
						
			if(rawValue != null) {
				
				StringBuilder current_sb = new StringBuilder();
								
				if(feature.isMany()) {
					if(feature instanceof EReference) {
						List<EObject> objs = (List<EObject>) rawValue;
						for(EObject o : objs) extractURI(o, current_sb);
					} else {
						List<Object> objs = (List<Object>) rawValue;
						for(Object o : objs) extractValue(o, current_sb);
					}
				} else {
					if(feature instanceof EReference) {
						extractURI((EObject)rawValue, current_sb);
					} else {
						extractValue(rawValue, current_sb);
					}
				}

				String tmp = current_sb.toString();
				if(tmp.length()>0 && tmp.contains("file:")==false) {
					String token = convertDigitToWord(tmp.toLowerCase());
					sb.append(token);
				}
			} 
		}
		
		sb.append("\n");
	}
	
	public void extractFeaturesforWord2Vec(EObject object, StringBuilder sb) {
		
		EClass eClass = object.eClass();
		
		for(EStructuralFeature feature : getHashableFeatures(eClass)) {
					
			Object defaultValue = feature.getDefaultValue();
			Object rawValue = object.eGet(feature);
			
			if(defaultValue == rawValue) {
				continue;
			}
						
			if(rawValue != null) {
				
				StringBuilder current_sb = new StringBuilder();
								
				if(feature.isMany()) {
					if(feature instanceof EReference) {
						List<EObject> objs = (List<EObject>) rawValue;
						for(EObject o : objs) extractURIforWord2Vec(o, current_sb);
					} else {
						List<Object> objs = (List<Object>) rawValue;
						for(Object o : objs) extractValueforWord2Vec(o, current_sb);
					}
				} else {
					if(feature instanceof EReference) {
						extractURIforWord2Vec((EObject)rawValue, current_sb);
					} else {
						extractValueforWord2Vec(rawValue, current_sb);
					}
				}
								
				String tmp = current_sb.toString();
				if(tmp.length()>0 && tmp.contains("file:")==false) {
					String token = convertDigitToWord(tmp.toLowerCase());
					sb.append(token);
				}
			} 
		}
		
	}
		
	private static String convertDigitToWord(String str) {
		StringBuilder newString = new StringBuilder();
		for (char ch : str.toCharArray()) {
			if (Character.isDigit(ch)) {
				String dw = DICTIONARY.get(ch);
				newString.append("_").append(dw);
			} else {
				newString.append(ch);
			}
		}
		return newString.toString();
	}
		
	/**
	 * 
	 * @param object
	 * @param rowItem
	 * @param clazzMap
	 * @param featureMap
	 * @param classFeaturesMap
	 */
	public void buildFeatureMatrix(EObject object, List<Integer> rowItem, HashMap<EClass, Integer> clazzMap,
			LinkedHashMap<EStructuralFeature, Integer> featureMap, Map<EClass, Set<EStructuralFeature>> classFeaturesMap) {
		
		EClass clazz = object.eClass();
		// First, add identical integer of class
		rowItem.add(clazzMap.get(clazz));
				
		// Note that featureMap is LinkedHashMap
		// Iterate the key in order, but the value is nonsense
		for(EStructuralFeature feature : featureMap.keySet()) {
						
			// If the current class does't have this feature, add 0
			if(classFeaturesMap.get(clazz).contains(feature) == false) {
				rowItem.add(0);
				continue;
			}
			
			Object rawValue = object.eGet(feature);
			
			if(rawValue != null) {
				StringBuilder sb = new StringBuilder();
				
				if(feature.isMany()) {
					if(feature instanceof EReference) {
						List<EObject> objs = (List<EObject>) rawValue;
						for(EObject o : objs) extractURI(o, sb);
					} else {
						List<Object> objs = (List<Object>) rawValue;
						for(Object o : objs) extractValue(o, sb);
					}
				} else {
					if(feature instanceof EReference) {
						extractURI((EObject)rawValue, sb);
					} else {
						extractValue(rawValue, sb);
					}
				}
							
				// one-hot
				int code = getWordID(sb);
				rowItem.add(code+1);
				
			} else { // If rawValue is null, add 0
				rowItem.add(0);
			}
			
		}
		
		// lyt
//		System.out.println(rowItem);
		
	}
	
	private void extractValue(Object value, Set<Object> bag) {
//		if(value instanceof String) {
//			bag.addAll(stringSplitter.split((String) value));
//		} else {
//		}
		bag.add(value);
	}
	
	private void extractURI(EObject value, Set<Object> bag) {
		FragmentIterable uriFrags = uriEncoder.getOrComputeLocation(value);
		StringBuilder builder = new StringBuilder();
		for(String str : uriFrags) {
			builder.append(str);
			builder.append("/");
		}
		bag.add(builder.toString());
	}
	
	/**
	 * Store the value
	 * @param value
	 * @param sb
	 */
	private void extractValue(Object value, StringBuilder sb) {
//		if(value instanceof String) {
//			bag.addAll(stringSplitter.split((String) value));
//		} else {
//		}
		sb.append(value + " ");
	}
	
	private void extractValueforWord2Vec(Object value, StringBuilder sb) {
//		if(value instanceof String) {
//			bag.addAll(stringSplitter.split((String) value));
//		} else {
//		}
		sb.append(value + "\n");
	}


		
	/**
	 * Store the URI
	 * @param value
	 * @param bag
	 */
	private void extractURI(EObject value, StringBuilder sb) {
		FragmentIterable uriFrags = uriEncoder.getOrComputeLocation(value);
		StringBuilder builder = new StringBuilder();
		for(String str : uriFrags) {
			builder.append(str);
			builder.append("/");
		}
		sb.append(builder.toString() + " ");
	}
	
	private void extractURIforWord2Vec(EObject value, StringBuilder sb) {
		FragmentIterable uriFrags = uriEncoder.getOrComputeLocation(value);
		StringBuilder builder = new StringBuilder();
		for(String str : uriFrags) {
			builder.append(str);
			builder.append("/");
		}
		sb.append(builder.toString() + "\n");
	}


	public List<EStructuralFeature> getHashableFeatures(EClass clazz) {
		List<EStructuralFeature> it = classFeatureHasherMap.get(clazz);
		if(it==null) {
			Iterable<EStructuralFeature> features = Iterables.filter(clazz.getEAllStructuralFeatures(), f->!shouldSkip(f));
			List<EStructuralFeature> list = new LinkedList<>();
			features.forEach(f->{
				list.add(f);
			});
			it = list;
			classFeatureHasherMap.put(clazz, it);
		}
		
		return it;
	}
	
	private Map<EClass, List<EStructuralFeature>> classFeatureHasherMap = new HashMap<>();
	
	public boolean shouldSkip(EStructuralFeature feature) {
		if(feature.isDerived() || feature.isTransient()) return true;
		if(feature == EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS) return true;
		if(feature instanceof EReference) return ((EReference) feature).isContainer() || ((EReference) feature).isContainment();
		return false;
	}


	
//	public void print() {
//		System.out.println("total words:"+words.size());
//		vectorMap.forEach((e,v)->{
//			System.out.print(e);
//			System.out.print("\t");
//			System.out.print(wordBagMap.get(e).size());
//			System.out.print("\t");
//			System.out.print("{");
//			for(int vv: v) System.out.print(vv+",");
//			System.out.println("}");
//		});
//	}
	
	
//	public Map<EObject, Collection<EObject>> testLSC(Iterable<? extends EObject> left, Iterable<? extends EObject> right) {
//		Map<EObject, Collection<EObject>> result = new HashMap<>();
//		
//		int more=0, less=0;
//		for(EObject l : left) {
//			Set<Integer> leftHash = vectorMap.get(l);
//			Set<EObject> cand = new HashSet<>();
//			for(EObject r : right) {
//				Set<Integer> rightHash = vectorMap.get(r);
//				int diff = HWTree.integerSetHamDistance(leftHash, rightHash);
//				if(diff<=50 ) {
//					cand.add(r);
//				}
//			}
//			
//			result.put(l, cand);
//		}
//		
//		System.out.println("more="+more+"\tless="+less);
//		
//		return result;
//	}
//	
//	public Map<EObject, Collection<EObject>> testHWT(Iterable<? extends EObject> left, Iterable<? extends EObject> right) {
//		Map<EObject, Collection<EObject>> result = new HashMap<>();
//		
//		
//		HWTree<Set<Integer>, EObject> hwTree = new HWTree<>((l,r)->{
//			return HWTree.integerSetHamDistance(l, r);
//		}, (h, d)->{
//			return HWTree.integerSetCodePattern(h, d, worldSize);
//		});
//		
//		for(EObject o :right) {	
//			Set<Integer> h = vectorMap.get(o);
//			hwTree.insert(h, o);
//		}
//		
//		left.forEach(l->{
//			Set<Integer> leftHash = vectorMap.get(l);
//			Collection<EObject> cand = hwTree.searchKNearest(leftHash, 1000, 50);
//			result.put(l, cand);
//		});
//		
//		return result;
//		
//	}
//	
//	public void print(EObject left, Iterable<? extends EObject> right) {
//		Set<Integer> lefthash = vectorMap.get(left);
//		Set<Object> leftbag = wordBagMap.get(left);
////		int[] leftminhash = minhashVectorMap.get(left);
//		
//		right.forEach(r->{
//			if(r.eClass()==left.eClass()) {				
//				Set<Integer> righthash = vectorMap.get(r);
//				Set<Object> rightbag = wordBagMap.get(r);
////				int[] rightminhash = minhashVectorMap.get(r);
//				
////				boolean rowEqual = false;
////				for(int i=0 ; i<lefthash.length/row;i++) {
////					rowEqual = true;
////					for(int j=0;j<row;j++) {						
////						if(lefthash[i*row + j]!=righthash[i*row + j]) rowEqual = false;
////					}
////					if(rowEqual==true) break;
////				}
//				
//				double fullHamSim, fullJacSim;
//				double minHamSim, minJacSim;
//				int fullHamDiff,  minHamDiff;
//				{
//					
//					Set<Object> union = new HashSet<>();
//					Set<Object> intersect = new HashSet<>();
//					Set<Object> diff = new HashSet<>();
//					
//					union.addAll(lefthash);
//					union.addAll(righthash);
//					
//					intersect.addAll(lefthash);
//					intersect.retainAll(righthash);
//					
//					diff.addAll(union);
//					diff.removeAll(intersect);
//					
//					minHamSim = 1.0 - (((double)diff.size())/worldSize);
//					
//					minJacSim = ((double)intersect.size())/union.size();
//					
//					minHamDiff = diff.size();
//				}
//				
//				{
//					Set<Object> union = new HashSet<>();
//					Set<Object> intersect = new HashSet<>();
//					Set<Object> diff = new HashSet<>();
//					
//					union.addAll(leftbag);
//					union.addAll(rightbag);
//					
//					intersect.addAll(leftbag);
//					intersect.retainAll(rightbag);
//					
//					diff.addAll(union);
//					diff.removeAll(intersect);
//					
//					fullHamSim = 1.0 - (((double)diff.size())/worldSize);
//					
//					fullJacSim = ((double)intersect.size())/union.size();
//					
//					fullHamDiff = diff.size();
//				}
//				
//				if(fullHamDiff <= 25) {
//					System.out.println("FullJacSim="+fullJacSim+"\tminJacSim="+minJacSim+"\tFullHamSim="+fullHamSim+"\tminHamSim="+minHamSim+"\tfullHDiff="+fullHamDiff+"\tminHDiff="+minHamDiff+"\t"+left+"\t"+r);
//				}
//			}
//		});
//	}
}
