package edu.ustb.sei.mde.compare.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.cpython.initproc;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.fasttext.FastText;
import org.deeplearning4j.models.fasttext.FastText.FastTextBuilder;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

public class TestFastText {

	static String myfilePath = "/Users/lesley/Documents/modelset/modelset/ecore-txt-all/ecore-txt-all-EClassImpl.txt";

	static String storedPath = "/Users/lesley/Documents/modelset/modelset/FastText/FastText-EClassImpl";
	
	static File cbowModelFile = new File("/Users/lesley/Documents/modelset/modelset/FastText/FastText-EClassImpl.bin");

	static FastText fastText = null;

	public static void main(String[] args) {
		
//		 train();
		
//		TestFastText testFastText = new TestFastText();
//		testFastText.init();

//		 load();

//		 sentenceVector();

	}
	
	public void init() {
		
		long start = System.currentTimeMillis();
		
		fastText = new FastText(cbowModelFile);
		
		long end = System.currentTimeMillis();
		System.out.println("Load fastText: " + (end - start) + "ms. ");
		
	}
	
	public FastText getFastText() {
		return fastText;
	}
	
	public float[] generateVector(String str) {

		String[] split = str.split(" ");

		// Create an empty array to hold the vectors for each word
		INDArray vectors = Nd4j.create(split.length, fastText.getDimension());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = fastText.getWordVectorMatrix(split[i]);			
			vectors.putRow(i, wordVector);
		}

		// Calculate the average vector of the sentence
		INDArray sentenceVector = vectors.mean(0);

		// Normalize the sentence vector
		INDArray normalizedVector = Transforms.unitVec(sentenceVector);
		float[] floatVector = normalizedVector.toFloatVector();

		return floatVector;

	}
	
	public INDArray generateVectorIND(String str) {

		String[] split = str.split(" ");

		// Create an empty array to hold the vectors for each word
		INDArray vectors = Nd4j.create(split.length, fastText.getDimension());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = fastText.getWordVectorMatrix(split[i]);
			vectors.putRow(i, wordVector);
		}

		// Calculate the average vector of the sentence
		INDArray sentenceVector = vectors.mean(0);

		// Normalize the sentence vector
		INDArray normalizedVector = Transforms.unitVec(sentenceVector);

		return normalizedVector;

	}

	private static void sentenceVector() {
		
		String str = "eclass purchase ";		
		String[] split = str.split(" ");

		// Create an empty array to hold the vectors for each word
		INDArray vectors = Nd4j.create(split.length, fastText.getDimension());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = fastText.getWordVectorMatrix(split[i]);
			vectors.putRow(i, wordVector);
		}

		// Calculate the average vector of the sentence
		INDArray sentenceVector = vectors.mean(0);

		// Normalize the sentence vector
		INDArray normalizedVector = Transforms.unitVec(sentenceVector);
		float[] floatVector = normalizedVector.toFloatVector();
		System.out.println(floatVector.length);
		System.out.println(Arrays.toString(floatVector));
		
	}

	private static void load() {
		
		System.out.println("dimension(): " + fastText.getDimension());
		
		double[] wordVector = fastText.getWordVector("eclass");
		System.out.println(Arrays.toString(wordVector));
		
		System.out.println();
		
		wordVector = fastText.getWordVector("eclass_change");
		System.out.println(Arrays.toString(wordVector));

	}

	private static void train() {

		long start = System.currentTimeMillis();

		File inputFile = new File(myfilePath);
		File outputFile = new File(storedPath);
		
		FastText fastText = FastText.builder()
									.supervised(false)
									.cbow(true)
									.epochs(10)
									.inputFile(inputFile.getAbsolutePath())
									.outputFile(outputFile.getAbsolutePath())
									.build();
		
		
		fastText.fit();

		long end = System.currentTimeMillis();
		System.out.println("\n" + "*********** Train: " + (end - start) + "ms. ");

		double[] inferVector = fastText.getWordVector("eclass");
		System.out.println("inferVector: " + Arrays.toString(inferVector));
	}

}
