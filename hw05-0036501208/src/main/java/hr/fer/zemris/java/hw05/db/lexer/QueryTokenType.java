package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Simple enumeration that represents all the
 * different query token types.
 * 
 * @author Filip Nemec
 */
public enum QueryTokenType {
	
	/** End of file token */
	EOF,
	
	/** Operator token. */
	OPERATOR,
	
	/** Identifier token. */
	IDENTIFIER,
	
	/** String token. */
	STRING
}
