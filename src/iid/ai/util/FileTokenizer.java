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
	/**
	 * Reads the contents of a file and returns the contents in a string.
	 */
	private static String readFile(File file) throws IOException {

	    StringBuilder fileContents = new StringBuilder((int)file.length());//initialize a stringbuilder using the files length as initial capacity.
	    Scanner scanner = new Scanner(file);//initialize a Scanner to read from the file.
	    try {
	        while(scanner.hasNextLine()) {//loop through all the lines in the file.  
	            fileContents.append(scanner.nextLine() + " ");//appends the stringbuilder with the next line, using a space character to seperate lines.
	        }
	        return fileContents.toString();//returns the result of the stringbuilder.
	    } finally {
	        scanner.close();//ALWAYS CLOSE A FILEREADER!!!
	    }
	}
}
