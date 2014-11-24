package iid.ai.util;

import java.util.StringTokenizer;

public class Tokenizer extends StringTokenizer {

	public Tokenizer(String str) {
		super(str);
	}
	public String nextToken() {
		return super.nextToken().replaceAll("[^a-zA-Z0-9]","").toLowerCase();
	}
}