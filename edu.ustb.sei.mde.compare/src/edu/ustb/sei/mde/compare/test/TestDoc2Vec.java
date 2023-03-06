package edu.ustb.sei.mde.compare.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.cpython.newfunc;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

public class TestDoc2Vec {

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

	static String myfilePath = "/Users/lesley/Documents/modelset/modelset/80-ecore-txt-all/80-ecore-txt-EClassImpl.txt";

	static File storedModel = new File("/Users/lesley/Documents/modelset/modelset/doc2vec/doc2vec-EClassImpl.pv");

	public static void main(String[] args) {

//		 train();

//		testLoad();

//		 Runtime runtime = Runtime.getRuntime();
//		 System.out.println("JVM空闲内存=" + runtime.freeMemory() / 1024 / 1024 + "M");
//		 System.out.println("JVM总内存=" + runtime.totalMemory() / 1024 / 1024 + "M");
//		 System.out.println("JVM可用最大内存=" + runtime.maxMemory() / 1024 / 1024 + "M");
//		 
//		 int size = 45000;
//		 float[][] matrix = new float[size][size];
//		 System.out.println(matrix.length);
		
	}

	private static void train() {
		long start = System.currentTimeMillis();

		try {

			File file = new File(myfilePath);

			SentenceIterator iter = new BasicLineIterator(file);

			AbstractCache<VocabWord> cache = new AbstractCache<>();

			// Define tokenizer factory
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new TokenPreProcess() {
				@Override
				public String preProcess(String token) {
					String base = token.toLowerCase();
					base = convertDigitToWord(base);
					return base;
				}
			});

			LabelsSource source = new LabelsSource("DOC_");

			ParagraphVectors vec = new ParagraphVectors.Builder()
					.iterations(5)
					.epochs(10)
					.layerSize(50)
					.learningRate(0.025)
					.labelsSource(source)
					.windowSize(5)
					.iterate(iter)
					.trainWordVectors(false)
					.vocabCache(cache)
					.allowParallelTokenization(false)
					.workers(1)
					.seed(0)
					.tokenizerFactory(t)
					.sampling(0)
					.build();

			vec.fit();

			long end = System.currentTimeMillis();
			System.out.println("\n" + "*********** Train: " + (end - start) + "ms. ");

			int cnt1 = cache.wordFrequency("eclass");
			System.out.println("eclass count: " + cnt1);

			INDArray inferredVectorA = vec
					.inferVector("name Object @eClassifiers.3/0/ ");
			System.out.println("inferredVectorA: " + inferredVectorA);

			// Save trained model to a file
			start = System.currentTimeMillis();

			WordVectorSerializer.writeParagraphVectors(vec, storedModel);

			end = System.currentTimeMillis();
			System.out.println("\n" + "*********** Save: " + (end - start) + "ms. ");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testLoad() {

		long start = System.currentTimeMillis();

		try {

			ParagraphVectors vec = WordVectorSerializer.readParagraphVectors(storedModel);

			// Define tokenizer factory
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new TokenPreProcess() {
				@Override
				public String preProcess(String token) {
					String base = token.toLowerCase();
					base = convertDigitToWord(base);
					return base;
				}
			});

			vec.setTokenizerFactory(t);

			long end = System.currentTimeMillis();
			System.out.println("\n Load#1: " + (end - start) + "ms. ");

			INDArray inferredVectorA = vec
					.inferVector("eClass purchaseOrder ");
			System.out.println(inferredVectorA);
			
			start = System.currentTimeMillis();
			vec = WordVectorSerializer.readParagraphVectors(storedModel);
			vec.setTokenizerFactory(t);
			end = System.currentTimeMillis();
			System.out.println("\n Load#2: " + (end - start) + "ms. ");
			
			inferredVectorA = vec
					.inferVector("eClass purchaseOrder ");
			System.out.println(inferredVectorA);
			

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static ParagraphVectors load() {
		
		long start = System.currentTimeMillis();
		
		ParagraphVectors vec = null;

		try {

			vec = WordVectorSerializer.readParagraphVectors(storedModel);

			// Define tokenizer factory
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new TokenPreProcess() {
				@Override
				public String preProcess(String token) {
					String base = token.toLowerCase();
					base = convertDigitToWord(base);
					return base;
				}
			});

			vec.setTokenizerFactory(t);

			long end = System.currentTimeMillis();
			System.out.println("\n" + "Load doc2vec model: " + (end - start) + "ms. \n");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return vec;

	}

	public static String convertDigitToWord(String string) {
		StringBuilder newString = new StringBuilder();
		for (char ch : string.toCharArray()) {
			if (Character.isDigit(ch)) {
				String dw = DICTIONARY.get(ch);
				newString.append("_").append(dw);
			} else {
				newString.append(ch);
			}
		}
		return newString.toString();
	}

}
