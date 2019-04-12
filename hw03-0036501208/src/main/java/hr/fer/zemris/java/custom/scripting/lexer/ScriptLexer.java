package hr.fer.zemris.java.custom.scripting.lexer;

import java.util.Objects;

import hr.fer.zemris.java.hw03.prob1.LexerException;

/**
 * Simple lexer that processes the given text and returns
 * tokens. It uses lazy approach: it will not generate next
 * token until it is being asked.
 */
public class ScriptLexer {
	/** Char array representation of the text this lexer is currently processing. */
	private char[] data;
	
	/** Last found token of the {@code nextToken} method call. */
	private ScriptToken token;
	
	/** Current index in the {@code data} array. */
	private int currentIndex;
	
	/** State that this lexer currently operates in. */
	private ScriptLexerState state = ScriptLexerState.PROCESSING_TEXT;
	
	/**
	 * Constructs a new lexer for the given text.	
	 * 
	 * @param text text to be tokenized by this lexer
	 * @throws NullPointerException if given text is {@code null}
	 */
	public ScriptLexer(String text) {
		Objects.requireNonNull(text);
		data = text.toCharArray();
	}
	
	/**
	 * Searches the given text for the next token in series.
	 * 
	 * @return next token in the given text
	 * @throws LexerException if the user tries to get next token
	 * 		   after already getting EOF token
	 */
	public ScriptToken nextToken() {
		if(token != null && token.getType() == ScriptTokenType.EOF)
			throw new LexerException("There are no more tokens after EOF.");
		
		if(currentIndex >= data.length)
			return token = new ScriptToken(ScriptTokenType.EOF, null);
		
		if(next2CharsAre('{', '$')) {
			currentIndex += 2;
			state = ScriptLexerState.PROCESSING_TAG;
			return token = new ScriptToken(ScriptTokenType.TAG_OPEN, "{$");
		}
		
		if(next2CharsAre('$', '}')) {
			currentIndex += 2;
			state = ScriptLexerState.PROCESSING_TEXT;
			return token = new ScriptToken(ScriptTokenType.TAG_CLOSE, "$}");
		}
		
		if(state == ScriptLexerState.PROCESSING_TEXT) {
			token = getTextToken();
		} else if(state == ScriptLexerState.PROCESSING_TAG) {
			token = getTagToken();
		} else {
			throw new LexerException("This lexer is not in a valid state.");
		}
		
		return token;
	}
	
	private ScriptToken getTagToken() {
		if(!findNextToken())
			return token = new ScriptToken(ScriptTokenType.EOF, null);
		
		if(next2CharsAre('$', '}')) {
			currentIndex += 2;
			state = ScriptLexerState.PROCESSING_TEXT;
			return token = new ScriptToken(ScriptTokenType.TAG_CLOSE, "$}");
		}
		
		char firstChar = data[currentIndex];
		
		if(Character.isLetter(firstChar)) {
			return token = processIdentifier();
		} else if(Character.isDigit(firstChar)) {
			return token = processNumber(false);
		} else {
			return token = processSymbol();
		}
	}
	
	/**
	 * Processes the symbol and returns the appropriate token.
	 * 
	 * @return STRING token
	 */
	private ScriptToken processSymbol() {
		char symbol = data[currentIndex];
		currentIndex++;
		
		if(currentIndex >= data.length) {
			return new ScriptToken(ScriptTokenType.OPERATOR, String.valueOf(symbol));
		}
		
		if(symbol == '=') {
			return new ScriptToken(ScriptTokenType.IDENTIFIER, String.valueOf(symbol));
		} else if(symbol == '@') {
			return processFunction();
		} else if(symbol == '"') {
			return processString();
		} else if(symbol == '-') {
			char afterMinus = data[currentIndex];
			if(Character.isDigit(afterMinus)) {
				return processNumber(true);
			}
		}
		
		return new ScriptToken(ScriptTokenType.OPERATOR, String.valueOf(symbol));
	}

	/**
	 * Processes the token that starts with quote sign (").
	 * 
	 * @return STRING token
	 */
	private ScriptToken processString() {
		var sb = new StringBuilder();
		
		boolean foundEndingQuote = false;
		boolean foundEscaping = false;
		
		while(currentIndex < data.length) {
			char current = data[currentIndex];
			
			currentIndex++;
			
			if(foundEscaping) {
				if(current == '"' || current == '\\') {
					foundEscaping = false;
					sb.append(current);
					continue;
				} else {
					throw new LexerException("After '\\' must come another '\\' or a '\"' sign. Was: '" + current + "'.");
				}
			}
			
			if(current == '\\') {
				foundEscaping = true;
				continue;
			} else if(current == '"') {
				foundEndingQuote = true;
				break;
			}

			sb.append(current);
		}
		
		if(!foundEndingQuote) {
			throw new LexerException("String '" + sb.toString() + "' was never closed.");
		}
		
		return new ScriptToken(ScriptTokenType.STRING, sb.toString());
	}

	/**
	 * Processes the token that starts with a digit.
	 * 
	 * @param isNegative if this number should return a negative value
	 * @return INTEGER or a DOUBLE token, depending
	 * 		   on the rest of the data array
	 */
	private ScriptToken processNumber(boolean isNegative) {
		int sign = isNegative ? -1 : 1;
			
		Number number = getNumber();
		
		if(number instanceof Integer) {
			Integer i = (Integer)number;
			return new ScriptToken(ScriptTokenType.INTEGER, i * sign);
		} else {
			Double d = (Double)number;
			return new ScriptToken(ScriptTokenType.DOUBLE, d * sign);
		}
	}
	
	/**
	 * Returns the INTEGER or DOUBLE value, based on the given character
	 * sequence.
	 * 
	 * @return INTEGER or DOUBLE value
	 */
	private Number getNumber() {
		var sb = new StringBuilder();
		
		boolean decimalPointFound = false;
		
		while(currentIndex < data.length) {
			char current = data[currentIndex];
			
			if(current == '.') {
				if(decimalPointFound) break;
				decimalPointFound = true;
				
				sb.append(current);
				
				// Check if the next char is a digit.
				currentIndex++;
				if(currentIndex < data.length) {
					char afterDecimalPoint = data[currentIndex];
					
					if(Character.isDigit(afterDecimalPoint)) {
						sb.append(afterDecimalPoint);
						currentIndex++;
						continue;
					} else {
						String message = "'" + afterDecimalPoint + "' is not a valid character after decimal point. Digit was expected.";
						throw new LexerException(message);
					}
				} else {
					throw new LexerException("Decimal point can't be the last character.");
				}
			} else if(Character.isDigit(current)) {
				sb.append(current);
				currentIndex++;
			} else {
				break;
			}
		}
		
		if(decimalPointFound) {
			return Double.parseDouble(sb.toString());
		} else {
			return Integer.valueOf(sb.toString());
		}
	}

	/**
	 * Processes the token that starts with '@' and checks if
	 * it is a valid function token.
	 * 
	 * @return FUNCTION token if valid
	 * @throws LexerException if this token is not a function
	 */
	private ScriptToken processFunction() {
		var sb = new StringBuilder();
		//sb.append('@');
		
		if(currentIndex < data.length && Character.isLetter(data[currentIndex])) { // second char must be a letter
			do {
				sb.append(data[currentIndex]);
				currentIndex++;
			} while(currentIndex < data.length && isLetterDigitUnderscore(data[currentIndex]));
			
			return new ScriptToken(ScriptTokenType.FUNCTION, sb.toString());
		} else {									 // if not, it is a single @ char
			throw new LexerException("'@' is not a valid operator.");
		}
	}
	
	/**
	 * Generates the identifier token starting from {@code currentIndex}.
	 * 
	 * @return IDENTIFIER token
	 */
	private ScriptToken processIdentifier() {
		var sb = new StringBuilder();
		
		while(currentIndex < data.length && isLetterDigitUnderscore(data[currentIndex])) {
			sb.append(data[currentIndex]);
			currentIndex++;
		}
		
		return new ScriptToken(ScriptTokenType.IDENTIFIER, sb.toString());
	}
	
	/**
	 * Checks if the given {@code char} is either letter, digit or an underscore.
	 * 
	 * @param c character to be checked
	 * @return {@code true} if it is, {@code false} otherwise
	 */
	private static boolean isLetterDigitUnderscore(char c) {
		return Character.isLetter(c) || Character.isDigit(c) || c == '_';
	}

	/**
	 * Checks if the next two chars are the given chars.
	 * 
	 * @param first first char
	 * @param second second char
	 * @return {@code true} if they are, {@code false} otherwise
	 */
	private boolean next2CharsAre(char first, char second) {
		int i = currentIndex;
		
		if(i < data.length && data[i] == first && (i + 1) < data.length && data[i + 1] == second)
			return true;
		
		return false;
	}

	/**
	 * Generates next TEXT token.
	 * 
	 * @return next TEXT token
	 */
	private ScriptToken getTextToken() {
		var sb = new StringBuilder();
		
		boolean foundEscaping = false;
		
		while(currentIndex < data.length) {
			char current = data[currentIndex];
			
			currentIndex++;
			
			if(foundEscaping) {
				if(current == '{' || current == '\\') {
					foundEscaping = false;
				} else {
					throw new LexerException("After '\\' must come another '\\' or a '{' sign. Was: '" + current + "'.");
				}
			} else if(current == '\\') {
				foundEscaping = true;
				continue;
			}
			
			sb.append(current);
			if(next2CharsAre('{', '$')) break;
		}
		
		return new ScriptToken(ScriptTokenType.TEXT, sb.toString());
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
	 * Sets the state for this lexer.
	 * 
	 * @param state state to be set for this lexer
	 */
	public void setState(ScriptLexerState state) {
		Objects.requireNonNull(state);
		this.state = state;
	}
	
	/**
	 * Gets the token that was found by the previous {@code nextToken}
	 * method call.
	 * 
	 * @return last found token in the given text
	 */
	public ScriptToken getToken() {
		return token;
	}
}
