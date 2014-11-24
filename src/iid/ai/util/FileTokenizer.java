package iid.ai.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class FileTokenizer {
	public static List<String> fileTokenize(File file) throws IOException {
		String text = readFile(file);
		return Tokenizer.tokenize(text);
	}
	private static String readFile(File file) throws IOException {

	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {        
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
	}
}
