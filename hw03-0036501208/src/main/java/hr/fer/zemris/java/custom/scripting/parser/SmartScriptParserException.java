package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Models the runtime exception for the lexer.
 */
public class SmartScriptParserException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the new parser exception.
	 */
	public SmartScriptParserException() {
		super();
	}
	
	/**
	 * Constructs the new parser exception with the
	 * appropriate message.
	 * 
	 * @param message the message
	 */
	public SmartScriptParserException(String message) {
		super(message);
	}
}