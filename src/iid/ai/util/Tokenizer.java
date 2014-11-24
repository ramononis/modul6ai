package iid.ai.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Tokenizer extends StringTokenizer {

	public Tokenizer(String str) {
		super(str);
	}
	public String nextToken() {
		return super.nextToken().replaceAll("[^a-zA-Z0-9]","").toLowerCase();
	}
	public List<String> allTokens() {
		List<String> tokens = new ArrayList<String>();
		while(hasMoreTokens()) {
			tokens.add(nextToken());
		}
		return tokens;
	}
	public static List<String> tokenize(String sentence){
		Tokenizer tokenizer = new Tokenizer(sentence);
		return tokenizer.allTokens();
	}
	// TEST
	public static void main(String[] args) {
		String input = new Scanner(System.in).nextLine();
		Tokenizer tokenizer = new Tokenizer(input);
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			System.out.println(token);
		}
	}
}
