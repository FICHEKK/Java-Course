package hr.fer.zemris.lsystems.impl;

import java.util.Objects;

/**
 * A simple utility class that converts the given
 * sequence of characters into an array of {@code String}
 * objects.
 * 
 * @author Filip Nemec
 */
public class WordGetter {
	/**
	 * Returns an array of words that the given sequence contains.
	 * 
	 * @param sequence the sequence of characters
	 * @return an array of {@code String} words
	 * @throws NullPointerException if the given sequence is {@code null}
	 */
	public static String[] getWords(String sequence) {
		Objects.requireNonNull(sequence, "Can't get words from a null string.");
		
		// removes all the extra spaces and splits by " "
		return sequence.trim().replaceAll(" +", " ").split(" ");
	}
}
