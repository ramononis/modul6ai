package iid.ai.classification;

import iid.ai.util.FileTokenizer;
import iid.ai.util.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class NBTClassifier {
	public enum GenderClass {
		MALE, FEMALE;
	}

	private int k = 1;

	public NBTClassifier(int k) {
		this.k = k;
	}

	public Dictionary maleDict = new Dictionary();
	public Dictionary femaleDict = new Dictionary();

	public int getDictSize() {
		return maleDict.getWordCount() + femaleDict.getWordCount();
	}

	public void putWords(GenderClass gender, List<String> words) {
		if (gender == GenderClass.FEMALE) {
			femaleDict.pushWords(words);
		} else {
			maleDict.pushWords(words);
		}
	}

	public GenderClass classify(List<String> words) {
		double maleProb = maleDict.log2ProbSentence(words);
		double femaleProb = femaleDict.log2ProbSentence(words);
		if (maleProb > femaleProb) {
			return GenderClass.MALE;
		} else {
			return GenderClass.FEMALE;
		}
	}

	class Dictionary {

		private Map<String, Integer> dict = new HashMap<String, Integer>();
		private int wordCount = 0;

		public int getWordCount() {
			return wordCount;
		}

		public void pushWords(List<String> words) {
			for (String word : words) {
				if (dict.containsKey(word)) {
					dict.put(word, dict.get(word) + 1);
				} else {
					dict.put(word, 1);
				}
			}
			wordCount += words.size();
		}

		public double log2ProbWord(String word) {
			int count = dict.containsKey(word) ? dict.get(word) : 0;
			double prob = (double) (count + k) / (wordCount + getDictSize() * k);
			return Math.log(prob) / Math.log(2);
		}

		public double log2ProbSentence(List<String> words) {
			double prob = 0;
			for (String word : words) {
				prob += log2ProbWord(word);
			}
			return prob;
		}
	}

	// TEST
	public static void test1() {
		NBTClassifier classifier = new NBTClassifier(1);
		String maleWords = "YO MAN I WANNA EAT A HOTDOG";
		String femaleWords = "I'M GOING TO SHOP THE WHOLE DAY!";
		classifier.putWords(GenderClass.MALE, Tokenizer.tokenize(maleWords));
		classifier
				.putWords(GenderClass.FEMALE, Tokenizer.tokenize(femaleWords));
		String testWords = "SHALL WE BUY A HOTDOG?";
		System.out.println(classifier.classify(Tokenizer.tokenize(testWords)));
	}

	public static void test2() {
		try {
			NBTClassifier classifier = new NBTClassifier(1);
			File[] maleFiles = new File("blogstrain/M").listFiles();
			for (File file : maleFiles) {
				classifier.putWords(GenderClass.MALE,
						FileTokenizer.fileTokenize(file));
			}
			File[] femaleFiles = new File("blogstrain/F").listFiles();
			for (File file : femaleFiles) {
				classifier.putWords(GenderClass.FEMALE,
						FileTokenizer.fileTokenize(file));
			}
			maleFiles = new File("blogstest/M").listFiles();
			for (File file : maleFiles) {
				GenderClass result = classifier.classify(FileTokenizer.fileTokenize(file));
				if(result.equals(GenderClass.FEMALE)) {
					System.out.println("fail:" + file.getName());
				}
			}
			femaleFiles = new File("blogstest/F").listFiles();
			
			for (File file : femaleFiles) {
				GenderClass result = classifier.classify(FileTokenizer.fileTokenize(file));
				if(result.equals(GenderClass.MALE)) {
					System.out.println("fail:" + file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test2();
	}
}
