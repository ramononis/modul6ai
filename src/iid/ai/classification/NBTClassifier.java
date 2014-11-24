package iid.ai.classification;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NBTClassifier {
	public enum GenderClass {
		MALE, FEMALE;
	}
	private int k = 1;
	public NBTClassifier(int k) {
		this.k = k;
	}
	class Dictionary {
		private GenderClass gender;

		public Dictionary(GenderClass g) {
			gender = g;
		}

		private Map<String, Integer> dict = new HashMap<String, Integer>();
		private int wordCount = 0;
		
		public int getWordCount() {
			return wordCount;
		}

		public void pushWords(Collection<String> words) {
			for(String word : words) {
				if(dict.containsKey(word)) {
					dict.put(word, dict.get(word) + 1);
				} else {
					dict.put(word, 1);
				}
			}
			wordCount += words.size();
		}

		public double log2Prob(String word) {
			int count = dict.containsKey(word) ? dict.get(word) : 0;
			double prob = (count + k) / (wordCount + getDictSize());
			return Math.log(prob) / Math.log(2);
		}
	}
	public Dictionary maleDict = new Dictionary(GenderClass.MALE);
	public Dictionary femaleDict = new Dictionary(GenderClass.FEMALE);
	public int getDictSize() {
		return maleDict.getWordCount() + femaleDict.getWordCount();
	}
}
