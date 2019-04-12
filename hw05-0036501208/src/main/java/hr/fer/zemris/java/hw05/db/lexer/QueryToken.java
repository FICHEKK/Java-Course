package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Represents the tokens that lexer returns when
 * processing the given input.
 * 
 * @author Filip Nemec
 */
public class QueryToken {
	
	/** Type of this token. */
	private QueryTokenType type;
	
	/** Value that this token stores. */
	private String value;
	
	/**
	 * Constructs the new token.
	 * 
	 * @param type token of newly constructed token
	 * @param value value that this token should store
	 */
	public QueryToken(QueryTokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * 
	 * @return this token's value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this token.
	 * 
	 * @return this token's type
	 */
	public QueryTokenType getType() {
		return type;
	}
	
	/**
	 * Returns the string representation of this
	 * token.
	 * 
	 * @return string of this format: (tokenType, tokenValue)
	 */
	@Override
	public String toString() {
		return "(" + type + ", " + value + ")";
	}
}
