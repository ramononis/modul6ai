package iid.ai.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Extension of the existing java StringTokenizer that (additionally to
 * StringTokenizer) discards all non alphabetic characters and converts the tokens to lowercase.<br>
 * The java StringTokenizer converts a String(the sentence) into a list of Strings(the words).
 * 
 * @author Ramon
 * 
 */
public class Tokenizer extends StringTokenizer {

	/**
	 * Constructor(does nothing but calling StringTokenizers constructor.
	 * @param str
	 */
	public Tokenizer(String str) {
		super(str);
	}
    /**
     * Returns the next token from this string tokenizer. <br>
     */
	public String nextToken() {
		//step-by-step explanation:
		//super.nextToken(): use the nextToken() method provided by StringTokenizer.
		//replaceAll([^a-zA-Z]", ""): replace all non alphabetic characters with ""(deletes them).
		//	[a-zA-Z] means all lower/upper case characters
		//	the ^ operator inverts the selection: [^a-zA-Z] means all NON lower/upper case characters.
		//toLowerCase:
		return super.nextToken().replaceAll("[^a-zA-Z]", "").toLowerCase();
	}

	/**
	 * Uses nextToken() to return all tokens from the string.
	 */
	public List<String> allTokens() {
		List<String> tokens = new ArrayList<String>();//initialize a empty list of strings
		while (hasMoreTokens()) {//loop through all the words in the sentence
			tokens.add(nextToken());//add the next token/word to the list.
		}
		return tokens;//return the contructed list of words.
	}

	/**
	 * The following method enables you to tokenize a string in one line:<br>
	 * Tokenizer.tokenize("a string");
	 */
	public static List<String> tokenize(String sentence) {
		Tokenizer tokenizer = new Tokenizer(sentence);//initialize a tokenizer object, passing the sentence to it.
		return tokenizer.allTokens();//Use allTokens() method to retrieve the list of tokens.
	}
}
