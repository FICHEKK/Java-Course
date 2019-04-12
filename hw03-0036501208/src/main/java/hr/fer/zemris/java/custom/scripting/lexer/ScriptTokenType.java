package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Types that the script token can be.
 */
public enum ScriptTokenType {
	/** Text token represented by a {@code String}. */
	TEXT,
	
	/** Tag opening token which indicates parser that we are entering the tag. */
	TAG_OPEN,
	
	/** Tag closing token which indicates parser that we are exiting the tag. */
	TAG_CLOSE,
	
	/** End-of-file token. It is the last token that lexer returns. */
	EOF,
	
	/** Token that represents the {@code String} value. */
	STRING,
	
	/** Token that represents the identifier value stored as a {@code String}. */
	IDENTIFIER,
	
	/** Token that represents the function value stored as a {@code String}. */
	FUNCTION,
	
	/** Token that represents the operator value stored as a {@code String}. */
	OPERATOR,
	
	/** Token that represents the integer value stored as an {@code int}. */
	INTEGER,
	
	/** Token that represents the double value stored as a {@code double}. */
	DOUBLE,
}
