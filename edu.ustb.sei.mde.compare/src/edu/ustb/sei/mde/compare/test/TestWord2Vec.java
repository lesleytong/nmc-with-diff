package edu.ustb.sei.mde.compare.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
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

public class TestWord2Vec {

	static String myfilePath = "/Users/lesley/Documents/modelset/modelset/ecore-txt-all/ecore-txt-all-EClassImpl.txt";

	static String storedPath = "/Users/lesley/Documents/modelset/modelset/word2vec/word2vec-EClassImpl.bin";

	static Word2Vec vec = null;

	public static void main(String[] args) {

		train();

//		TestWord2Vec testWord2Vec = new TestWord2Vec();
//		testWord2Vec.init();
//
//		 load();

		// sentenceVector();

	}

	public void init() {
		long start = System.currentTimeMillis();

		vec = WordVectorSerializer.readWord2VecModel(new File(storedPath));

		long end = System.currentTimeMillis();
		System.out.println("Load Word2Vec: " + (end - start) + "ms. ");

	}

	public Word2Vec getWord2Vec() {
		return vec;
	}

	public float[] generateVector(String str) {

		String[] split = str.split(" ");

		// Create an empty array to hold the vectors for each word
		INDArray vectors = Nd4j.create(split.length, vec.getLayerSize());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = vec.getWordVectorMatrix(split[i]);
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
		INDArray vectors = Nd4j.create(split.length, vec.getLayerSize());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = vec.getWordVectorMatrix(split[i]);
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
		INDArray vectors = Nd4j.create(split.length, vec.getLayerSize());

		for (int i = 0; i < split.length; i++) {
			INDArray wordVector = vec.getWordVectorMatrix(split[i]);
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

		double[] inferVector = vec.getWordVector("eclass");
		System.out.println("inferVector: " + Arrays.toString(inferVector));

		vec = WordVectorSerializer.readWord2VecModel(new File(storedPath));

		inferVector = vec.getWordVector("eclass");
		System.out.println("inferVector: " + Arrays.toString(inferVector));

	}

	private static void train() {

		long start = System.currentTimeMillis();

		try {

			File file = new File(myfilePath);

			SentenceIterator iter = new BasicLineIterator(file);

			// Define tokenizer factory
			TokenizerFactory t = new DefaultTokenizerFactory();

			vec = new Word2Vec.Builder().minWordFrequency(1).layerSize(100).seed(42)
					.windowSize(5).iterate(iter).tokenizerFactory(t).epochs(10).build();

			vec.fit();

			long end = System.currentTimeMillis();
			System.out.println("\n" + "*********** Train: " + (end - start) + "ms. ");

			double[] inferVector = vec.getWordVector("eclass");
			System.out.println("inferVector: " + Arrays.toString(inferVector));

			// Save trained model to a file
			start = System.currentTimeMillis();

			WordVectorSerializer.writeWord2VecModel(vec, storedPath);

			end = System.currentTimeMillis();
			System.out.println("\n" + "*********** Save: " + (end - start) + "ms. ");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
