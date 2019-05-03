package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * Parses the given expression and creates a machine
 * that arranges the name.
 *
 * @author Filip Nemec
 */
public class NameBuilderParser {
	
	/** An empty name-builder object. */
	private NameBuilder composite = DefaultNameBuilders.blank();
	
	/** The lexer used by this parser to generate name builder. */
	private NameBuilderLexer lexer;
	
	/**
	 * Constructs a new name builder parser for the
	 * given expression.
	 *
	 * @param expression
	 */
	public NameBuilderParser(String expression) {
		lexer = new NameBuilderLexer(expression);
		processTokens();
	}
	
	/**
	 * Processes the tokens recieved from the lexer
	 * until the EOF token is reached.
	 */
	private void processTokens() {
		NameBuilderToken token = null;
		
		do {
			token = lexer.nextToken();
			NameBuilderTokenType type = token.getType();
			
			switch (type) {
				case TEXT:
					processTextToken(token);
					break;
					
				case GROUP_OPEN:
					processGroupTag();
					break;
					
				case EOF:
					break;
					
				default:
					throw new NameBuilderParserException("Invalid type " + type);
			}
			
		} while(token.getType() != NameBuilderTokenType.EOF);
	}
	
	/**
	 * Processes the whole group tag.
	 */
	private void processGroupTag() {
		lexer.setState(NameBuilderLexerState.PROCESSING_GROUP_TAG);
		
		// Expected token 1: a valid integer value
		NameBuilderToken token = lexer.nextToken();
		
		if(token.getType() != NameBuilderTokenType.GROUP_STRING)
			throw new NameBuilderParserException("Expected token GROUP_STRING, was: " + token.getType());
		
		int groupIndex = parseToInt((String) token.getValue());
		
		if(groupIndex < 0)
			throw new NameBuilderParserException("Group index cannot be a negative number. Was: " + groupIndex);
		
		
		// Expected token 2: a closing tag or a comma
		token = lexer.nextToken();
		
		if(token.getType() == NameBuilderTokenType.GROUP_CLOSE) {
			composite = composite.then(DefaultNameBuilders.group(groupIndex));
			
		} else if(token.getType() == NameBuilderTokenType.GROUP_COMMA) {
			// Expected token 3: a string that contains padding format
			token = lexer.nextToken();
			
			if(token.getType() != NameBuilderTokenType.GROUP_STRING)
				throw new NameBuilderParserException("Expected token GROUP_STRING, was: " + token.getType());
			
			String explanation = (String) token.getValue();
			if(explanation.charAt(0) == '0') {
				int minWidth = parseToInt(explanation.substring(1));
				composite = composite.then(DefaultNameBuilders.group(groupIndex, '0', minWidth));
				
			} else {
				int minWidth = parseToInt(explanation);
				composite = composite.then(DefaultNameBuilders.group(groupIndex, ' ', minWidth));
				
			}
			
			// Expected token 4: a closing tag
			token = lexer.nextToken();
			
			if(token.getType() != NameBuilderTokenType.GROUP_CLOSE)
				throw new NameBuilderParserException("Expected token GROUP_CLOSE, was: " + token.getType());
			
			
		} else {
			throw new NameBuilderParserException("Expected token GROUP_CLOSE or COMMA, was: " + token.getType());
			
		}
		
		lexer.setState(NameBuilderLexerState.PROCESSING_TEXT);
	}
	
	/**
	 * Helper method that parses the given {@code String} to
	 * an {@code int} value and wraps the exception if one
	 * does occur.
	 *
	 * @param expression the expression to be parsed
	 * @return the parsed {@code int} value
	 * @throws NameBuilderParserException if it could not be parsed
	 */
	private int parseToInt(String expression) {
		try {
			return Integer.parseInt(expression);
			
		} catch(NumberFormatException e) {
			throw new NameBuilderParserException("'" + expression + "' could not be parsed to a number.");
		}
	}
	
	/**
	 * Processes the text token.
	 *
	 * @param token the token to be processed
	 */
	private void processTextToken(NameBuilderToken token) {
		String text = (String) token.getValue();
		composite = composite.then(DefaultNameBuilders.text(text));
	}
	
	/**
	 * Returns the finished name-builder.
	 *
	 * @return the finished name-builder
	 */
	public NameBuilder getNameBuilder() {
		 return composite;
	}
}
