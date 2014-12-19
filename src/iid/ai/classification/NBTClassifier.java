package iid.ai.classification;

import iid.ai.util.FileTokenizer;
import iid.ai.util.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NTBClassifier represents a naïve bayesian text classifier.
 * 
 * @author R
 * 
 */
public class NBTClassifier {
	/**
	 * Defines the male/female genders.
	 */
	public enum Gender {
		MALE, FEMALE;
	}

	private int k = 4;// the constant used for smoothing

	// male/female vocabularies.
	private ClassVocabulary maleVoc = new ClassVocabulary();
	private ClassVocabulary femaleVoc = new ClassVocabulary();
	private Set<String> vocabulary = new HashSet<String>();
	/**
	 * Returns the total word count(of both the female and male dictionary)
	 */
	public int getVocSize() {
		return vocabulary.size();
	}

	/**
	 * Trains the classifier by giving it a list of words with the associated
	 * gender.
	 */
	public void putWords(Gender gender, List<String> words) {
		if (gender == Gender.FEMALE) {// determine if the words are typed by a
										// female or a male.
			femaleVoc.putWords(words);// put it in the correct dictionary.
		} else {
			maleVoc.putWords(words);
		}
		vocabulary.addAll(words);
	}

	/**
	 * Classifies a list of words.
	 */
	public Gender classify(List<String> words) {
		// calcutate the probability for both male and female.
		double maleProb = maleVoc.log2ProbSentence(words);
		double femaleProb = femaleVoc.log2ProbSentence(words);
		if (maleProb > femaleProb) {// determine which gender gives the greater
									// probability.
			return Gender.MALE;// return the correct gender.
		} else {
			return Gender.FEMALE;
		}
	}

	/**
	 * Represents a dictionary that keeps track of how many times certain words
	 * are used.
	 * 
	 * @author R
	 * 
	 */
	class ClassVocabulary {

		/**
		 * The map(http://en.wikipedia.org/wiki/Associative_array) containing
		 * the amount of occurences for each word using the words as key and the
		 * amount of occurences as value.
		 */
		private Map<String, Integer> dict = new HashMap<String, Integer>();// initially
																			// empty.
		private int wordCount = 0;// keep track of the total amount of words.

		/**
		 * Puts a list of words in this dictionary.
		 */
		public void putWords(List<String> words) {
			for (String word : words) {// loop through all words.
				if (dict.containsKey(word)) {// check if the dictionary contains
												// the word(as a key).
					dict.put(word, dict.get(word) + 1);// then increment the
														// occurence amount for
														// that word with 1.
				} else {
					dict.put(word, 1);// otherwise add a new (key,value) pair to
										// the map initially with a value equal
										// to 1.
				}
			}
			wordCount += words.size();// update the total amount of words.
		}

		/**
		 * Returns probability for 1 word in log space(base 2)
		 */
		public double log2ProbWord(String word) {
			int count = dict.containsKey(word) ? dict.get(word) : 0;// occurence
																	// amount of
																	// this
																	// word.
			double prob = (double) (count + k) / (wordCount + getVocSize() * k);// calculate
																				// probability
																				// using
																				// formula
																				// as
																				// provided
																				// in
			return Math.log(prob) / Math.log(2);// returns the value in log
												// space(base 2)
		}

		/**
		 * Returns probability for a list of words in log space(base 2)
		 */
		public double log2ProbSentence(List<String> words) {
			double prob = 0;// we are using log space->we start with 0.
			for (String word : words) {// loop through all words.
				prob += log2ProbWord(word);// add the probability for the word
											// to the total probability.
			}
			return prob;// return the result.
		}
	}

	// TEST
	public static void test1() {
		NBTClassifier classifier = new NBTClassifier();// initialize the
														// classifier
		String maleWords = "YO MAN I WANNA EAT A HOTDOG";// a sentence used
															// often by a male.
		String femaleWords = "I'M GOING TO SHOP THE WHOLE DAY!";// a sentence
																// used often by
																// a female
		classifier.putWords(Gender.MALE, Tokenizer.tokenize(maleWords));// train
																		// the
																		// classifier
		classifier.putWords(Gender.FEMALE, Tokenizer.tokenize(femaleWords));// train
																			// the
																			// classifier.
		String testWords = "SHALL WE BUY A HOTDOG?";// test sentence
		System.out.println(classifier.classify(Tokenizer.tokenize(testWords)));// output
																				// the
																				// predicted
																				// gender(should
																				// be
																				// MALE).
	}

	public static void test2() {
		try {
			NBTClassifier classifier = new NBTClassifier();// initialize the
															// classifier.
			// TRAINING
			File[] maleFiles = new File("blogstrain/M").listFiles();// list all
																	// male
																	// training
																	// files
			for (File file : maleFiles) {// loop through the files
				classifier.putWords(Gender.MALE,
						FileTokenizer.fileTokenize(file));// train the
															// classifier using
															// the content of
															// the file.
			}
			File[] femaleFiles = new File("blogstrain/F").listFiles();// list
																		// all
																		// female
																		// training
																		// files
			for (File file : femaleFiles) {// loop through the files
				classifier.putWords(Gender.FEMALE,
						FileTokenizer.fileTokenize(file));// train the
															// classifier using
															// the content of
															// the file.
			}
			
			// PREDICTING/CLASSIFY
			maleFiles = new File("blogstest/M").listFiles();// list all male
															// test files
			for (File file : maleFiles) {// loop through the files
				Gender result = classifier.classify(FileTokenizer
						.fileTokenize(file));// predict the gender of the file
												// text.
				if (result.equals(Gender.FEMALE)) {// check if the prediction is
													// wrong
					System.out.println("fail:" + file.getName());// then output
																	// a fail.
				}
			}
			femaleFiles = new File("blogstest/F").listFiles();// list all female
																// test files

			for (File file : femaleFiles) {// loop through the files
				Gender result = classifier.classify(FileTokenizer
						.fileTokenize(file));// predict the gender of the file
												// text.
				if (result.equals(Gender.MALE)) {// check if the prediction is
													// wrong
					System.out.println("fail:" + file.getName());// then output
																	// a fail.
				}
			}
		} catch (IOException e) {// we are reading files->input/output exception
									// may occur
			e.printStackTrace();// print the exception to the console.
		}
	}

	public static void main(String[] args) {
		test2();// use either test1() or test2() to run a test.
	}
}
