package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * A simple lexer that tokenizes the given expression.
 *
 * @author Filip Nemec
 */
public class NameBuilderLexer {
	
	/** Lastly generated token. */
	private NameBuilderToken token;
	
	/** The current index in the data array. */
	private int index;
	
	/** Data being processed by this lexer. */
	private char[] data;
	
	/** The current operating state of this lexer. */
	private NameBuilderLexerState state = NameBuilderLexerState.PROCESSING_TEXT;
	
	/**
	 * Constructs a new lexer for the given expression.
	 *
	 * @param expression
	 */
	public NameBuilderLexer(String expression) {
		this.data = expression.toCharArray();
	}
	
	/**
	 * Returns the next token if there is one.
	 *
	 * @return
	 */
	public NameBuilderToken nextToken() {
		if(token != null && token.getType() == NameBuilderTokenType.EOF) 
			throw new IllegalStateException("All tokens have already been returned.");
		
		skipWhitespace();
		
		if(index >= data.length)
			return token = new NameBuilderToken(NameBuilderTokenType.EOF, null);
		
		if(next2CharsAre('$', '{')) {
			index += 2;
			return token = new NameBuilderToken(NameBuilderTokenType.GROUP_OPEN, null);
		}
		
		if(state == NameBuilderLexerState.PROCESSING_TEXT) {
			return token = processText();
			
		} else {
			return token = processGroupTag();
			
		}
	}
	
	private NameBuilderToken processGroupTag() {
		if(data[index] == '}') {
			index++;
			return new NameBuilderToken(NameBuilderTokenType.GROUP_CLOSE, null);
		}
		
		if(data[index] == ',') {
			index++;
			return new NameBuilderToken(NameBuilderTokenType.GROUP_COMMA, null);
		}
		
		var sb = new StringBuilder();
		
		while(index < data.length) {
			if(data[index] == ',' || data[index] == '}')
				break;
			
			if(!Character.isWhitespace(data[index]))
				sb.append(data[index]);
			
			index++;
		}
		
		return new NameBuilderToken(NameBuilderTokenType.GROUP_STRING, sb.toString());
	}

	private NameBuilderToken processText() {
		var sb = new StringBuilder();
		
		while(index < data.length) {
			if(data[index] == '$') {
				if(index + 1 < data.length && data[index + 1] == '{') {
					break;
				}
			}
			
			sb.append(data[index]);
			index++;
		}
		
		return new NameBuilderToken(NameBuilderTokenType.TEXT, sb.toString());
	}

	/**
	 * Changes the current state of operation for this
	 * lexer.
	 *
	 * @param state the next state
	 */
	public void setState(NameBuilderLexerState state) {
		this.state = state;
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
	
	/**
	 * Checks if the next two chars are the given chars.
	 * 
	 * @param first first char
	 * @param second second char
	 * @return {@code true} if they are, {@code false} otherwise
	 */
	private boolean next2CharsAre(char first, char second) {
		int i = index;
		
		if(i < data.length && data[i] == first && (i + 1) < data.length && data[i + 1] == second)
			return true;
		
		return false;
	}
	
	/**
	 * Gets the token that was found by the previous {@code nextToken}
	 * method call.
	 * 
	 * @return last found token in the given expression
	 */
	public NameBuilderToken getToken() {
		return token;
	}
}
