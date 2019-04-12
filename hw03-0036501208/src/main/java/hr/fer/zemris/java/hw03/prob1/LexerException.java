package hr.fer.zemris.java.hw03.prob1;

/**
 * Models the runtime exception for the lexer.
 */
public class LexerException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the new lexer exception.
	 */
	public LexerException() {
		super();
	}
	
	/**
	 * Constructs the new lexer exception with the
	 * appropriate message.
	 * 
	 * @param message the message
	 */
	public LexerException(String message) {
		super(message);
	}
}
