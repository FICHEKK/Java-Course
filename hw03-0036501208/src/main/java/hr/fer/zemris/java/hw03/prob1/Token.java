package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents the tokens that lexer returns when
 * processing the given input.
 */
public class Token {
	/** Type of this token. */
	private TokenType type;
	
	/** Value that this token stores. */
	private Object value;
	
	/**
	 * Constructs the new token.
	 * 
	 * @param type token of newly constructed token
	 * @param value value that this token should store
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Returns the value of this token.
	 * 
	 * @return this token's value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the type of this token.
	 * 
	 * @return this token's type
	 */
	public TokenType getType() {
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
