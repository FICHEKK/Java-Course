package hr.fer.zemris.java.hw03.prob1;

/**
 * Types that the token can be.
 */
public enum TokenType {
	/** End-of-file token. The last token in sequence. */
	EOF,
	
	/** A word token represented by a {@code String} value. */
	WORD,
	
	/** A number token represented by a {@code Long} value. */
	NUMBER,
	
	/** A symbol token represented by a {@code Character} value. */
	SYMBOL
}
