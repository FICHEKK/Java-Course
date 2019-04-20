package hr.fer.zemris.java.hw05.db.lexer;

import java.util.Objects;

/**
 * A simple query lexer whose job is to extract and return
 * tokens from the provided {@code String}.
 *
 * @author Filip Nemec
 */
public class QueryLexer {
	
	/** The array of characters that this lexer is processing. */
	private char[] data;
	
	/** The last generated token. */
	private QueryToken token;
	
	/** The current index that is used to retrieve characters from the {@link #data}. */
	private int index;
	
	/**
	 * Constructs a new query lexer.
	 *
	 * @param data the data to be processed by this lexer
	 */
	public QueryLexer(String data) {
		Objects.requireNonNull(data, "Given data cannot be null.");
		this.data = data.toCharArray();
	}
	
	/**
	 * Returns the next token, unless all tokens have already
	 * been returned.
	 *
	 * @return the next token if there is one
	 * @throws IllegalStateException if the EOF token was already returned
	 */
	public QueryToken nextToken() {
		if(token != null && token.getType() == QueryTokenType.EOF) 
			throw new IllegalStateException("All tokens have already been returned.");
		
		skipWhitespace();
		
		if(index >= data.length)
			return token = new QueryToken(QueryTokenType.EOF, null);
		
		char current = data[index];
		if(Character.isLetter(current)) {
			return token = getIdentifierToken();
			
		} else if(current == '"') {
			return token = getStringToken();
			
		} else {
			return token = getOperatorToken();
			
		}
	}
	
	/**
	 * Returns the next IDENTIFIER token.
	 *
	 * @return the next IDENTIFIER token
	 */
	private QueryToken getIdentifierToken() {
		var sb = new StringBuilder();
		
		while(index < data.length && Character.isLetter(data[index])) {
			sb.append(data[index]);
			index++;
		}
		
		String identifier = sb.toString();
		if(identifier.equalsIgnoreCase("LIKE")) {
			return new QueryToken(QueryTokenType.OPERATOR, identifier);
		}
		
		return new QueryToken(QueryTokenType.IDENTIFIER, identifier);
	}
	
	/**
	 * Returns the next STRING token.
	 *
	 * @return the next STRING token
	 */
	private QueryToken getStringToken() {
		var sb = new StringBuilder();
		
		boolean quotesClosed = false;
		
		while(++index < data.length) {
			if(data[index] == '"') {
				quotesClosed = true;
				index++;
				break;
			}
			
			sb.append(data[index]);
		}
		
		if(!quotesClosed)
			throw new IllegalArgumentException("Quotes were not closed.");
		
		return new QueryToken(QueryTokenType.STRING, sb.toString());
	}
	
	/**
	 * Returns the next OPERATOR token.
	 *
	 * @return the next OPERATOR token
	 */
	private QueryToken getOperatorToken() {
		var sb = new StringBuilder();
		sb.append(data[index]);
		index++;
		
		if(index < data.length && data[index] == '=') {
			sb.append(data[index]);
			index++;
		}
		
		return new QueryToken(QueryTokenType.OPERATOR, sb.toString());
	}
	
	/**
	 * Gets the last token generated by the {@link #nextToken()} method.
	 * However, if {@link #nextToken()} hasn't been called, this method
	 * will return {@code null}.
	 *
	 * @return lastly generated token. However, if no tokens have been
	 * 		   generated, returns {@code null}.
	 */
	public QueryToken getToken() {
		return token;
	}
	
	/**
	 * Skips the whitespace and puts the {@link #index} to the next
	 * non-whitespace character (if there are any left). If there are
	 * no non-whitespace characters left, this method will place the {@link #index}
	 * to the position following the last spot in the character ({@link #data} array.
	 */
	private void skipWhitespace() {
		while(index < data.length && Character.isWhitespace(data[index])) {
			index++;
		}
	}
}