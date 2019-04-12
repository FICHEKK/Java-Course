package hr.fer.zemris.java.hw03.prob1;

import java.util.Objects;

/**
 * Lexer class that tokenizes the given text. It has 4 token types
 * it will divide the given text into: WORD, NUMBER, SYMBOL and EOF
 * (end-of-file).
 * 
 * User can get token by token by calling API methods and also can
 * choose regime lexer works in: BASIC or EXTENDED.
 */
public class Lexer {
	/** Char array representation of the text this lexer is currently processing. */
	private char[] data;
	
	/** Last found token of the {@code nextToken} method call. */
	private Token token;
	
	/** Current index in the {@code data} array. */
	private int currentIndex;
	
	/** State that this lexer currently operates in. */
	private LexerState state = LexerState.BASIC;
	
	/**
	 * Constructs a new lexer for the given text.
	 * 
	 * @param text text to be tokenized by this lexer
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
	}
	
	/**
	 * Searches the given text for the next token in series.
	 * 
	 * @return next token in the given text
	 * @throws LexerException if the user tries to get next token
	 * 		   that does not exist
	 */
	public Token nextToken() {
		if(token != null && token.getType() == TokenType.EOF)
			throw new LexerException("There are no more tokens after EOF.");
		
		if(!findNextToken())
			return token = new Token(TokenType.EOF, null);
		
		char currentChar = data[currentIndex];
		if(state == LexerState.BASIC) {
			if(Character.isLetter(currentChar) || currentChar == '\\') {
				return token = getWordTokenBasic();
			} else if(Character.isDigit(currentChar)) {
				return token = getNumberTokenBasic();
			} else {
				return token = getSymbolTokenBasic();
			}
		} else {
			if(currentChar == '#') {
				return token = getSymbolTokenBasic();
			} else {
				return token = getWordTokenExtended();
			}
		}
	}
	
	/**
	 * Retrieves the WORD token starting from the
	 * {@code currentIndex}. It uses EXTENDED lexer state.
	 * 
	 * @return the next WORD token
	 */
	private Token getWordTokenExtended() {
		StringBuilder sb = new StringBuilder();
		
		while(currentIndex < data.length) {
			char currentChar = data[currentIndex];
			
			if(!Character.isWhitespace(currentChar) && currentChar != '#') {
				sb.append(data[currentIndex]);
				currentIndex++;
			} else {
				break;
			}
		}
		
		return new Token(TokenType.WORD, sb.toString());
	}
	
	/**
	 * Retrieves the WORD token starting from the
	 * {@code currentIndex}. It uses BASIC lexer state.
	 * 
	 * @return the next WORD token
	 */
	private Token getWordTokenBasic() {
		StringBuilder sb = new StringBuilder();
		
		// Iterate forward if we haven't reached the end and if the element is a letter.
		while(currentIndex < data.length) {
			char currentChar = data[currentIndex];
			
			if(Character.isLetter(currentChar)) {
				sb.append(data[currentIndex]);
				currentIndex++;
			} else if(currentChar == '\\') {
				if(currentIndex + 1 >= data.length)
					throw new LexerException("Char '\\' can't be the last element of an array.");

				// Next symbol needs to be \ or a number. Otherwise throw exception.
				char nextChar = data[currentIndex + 1];
				if(nextChar == '\\' || Character.isDigit(nextChar)) {
					sb.append(nextChar);
					currentIndex += 2; // Skip the first \ and the following char.
				} else {
					throw new LexerException("After '\\' should come another '\\' or a digit.");
				}
			} else {
				break;
			}
		}
		
		return new Token(TokenType.WORD, sb.toString());
	}
	
	/**
	 * Retrieves the NUMBER token starting from the
	 * {@code currentIndex}. It uses BASIC lexer state.
	 * 
	 * @return the next NUMBER token
	 */
	private Token getNumberTokenBasic() {
		StringBuilder sb = new StringBuilder();
		
		// Iterate forward if we haven't reached the end and if the element is a letter.
		while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}
		
		String numberString = sb.toString();
		
		try {
			Long tokenValue = Long.parseLong(numberString);
			return new Token(TokenType.NUMBER, tokenValue);
		} catch(NumberFormatException ex) {
			throw new LexerException("Token '" + numberString + "' can't be converted to Long.");
		}
	}
	
	/**
	 * Retrieves the SYMBOL token starting from the
	 * {@code currentIndex}. It uses BASIC lexer state.
	 * 
	 * @return the next SYMBOL token
	 */
	private Token getSymbolTokenBasic() {
		Token symbolToken = new Token(TokenType.SYMBOL, Character.valueOf(data[currentIndex]));
		currentIndex++;
		return symbolToken;
	}
	
	/**
	 * Skips the whitespace and sets the {@code currentIndex} to
	 * the start of the next token if there is one. If it finds the next token,
	 * it will return {@code true}, otherwise if it reaches the end,
	 * it will return {@code false.}
	 * 
	 * @return {@code true} if found next token, {@code false} if not
	 * 		   (reached EOF)
	 */
	private boolean findNextToken() {
		// If index is out of data range, we reached the end.
		if(currentIndex >= data.length)
			return false;
		
		// Skips the whitespace and gets to actual words/numbers,
		// or returns EOF if reaches end before finding any more words.
		while(Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
			
			// If we reached the end, return EOF token.
			if(currentIndex >= data.length)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Sets the state for this lexer. It can be
	 * BASIC or EXTENDED.
	 * 
	 * @param state state to be set for this lexer
	 */
	public void setState(LexerState state) {
		Objects.requireNonNull(state);
		this.state = state;
	}
	
	/**
	 * Gets the token that was found by the previous {@code nextToken}
	 * method call.
	 * 
	 * @return last found token in the given text
	 */
	public Token getToken() {
		return token;
	}
}
