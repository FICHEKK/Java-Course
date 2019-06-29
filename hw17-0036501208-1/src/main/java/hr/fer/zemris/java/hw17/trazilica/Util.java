package hr.fer.zemris.java.hw17.trazilica;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * The utility class used by the search engine and console.
 *
 * @author Filip Nemec
 */
final class Util {
	
	/* We don't want any instances. */
	private Util() {}

	/**
	 * Converts the given text into a list of words and returns that list.
	 *
	 * @param text the text to be converted to the words
	 * @return the list of words in the given text
	 */
	public static List<String> convertTextToWords(String text) {
		List<String> words = new LinkedList<>();
		
		int wordStart = -1;
		int wordEnd = -1;
		boolean processingWord = false;
		
		for(int index = 0; index < text.length(); index++) {
			if(processingWord) {
				if(Character.isAlphabetic(text.charAt(index))) {
					wordEnd++;
				} else {
					processingWord = false;
					words.add( text.substring(wordStart, wordEnd).toLowerCase() );
				}
				
			} else {
				if(Character.isAlphabetic(text.charAt(index))) {
					wordStart = index;
					wordEnd = wordStart + 1;
					processingWord = true;
				}
			}
		}
		
		if(processingWord) {
			words.add( text.substring(wordStart).toLowerCase() );
		}
		
		return words;
	}
	
	/**
	 * Returns the number of documents in the hierarchy.
	 *
	 * @param root the root of the hierarchy
	 * @return the number of documents in the hierarchy
	 */
	public static int getDocumentCount(File root) {
		File[] documents = root.listFiles();
		int count = 0;
		
		for (File document : documents) {
			if (document.isDirectory()) {
				count += getDocumentCount(document);
			} else {
				count++;
			}
		}

		return count;
	}
}
