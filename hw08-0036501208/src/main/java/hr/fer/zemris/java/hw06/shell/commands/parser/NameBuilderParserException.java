package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * Models exceptional situations that can happen 
 * while parsing the name.
 *
 * @author Filip Nemec
 */
public class NameBuilderParserException extends RuntimeException {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new name-builder parser exception.
	 */
	public NameBuilderParserException() {
		super();
	}
	
	/**
	 * Constructs a new name-builder parser exception,
	 * with the provided message.
	 *
	 * @param message the message
	 */
	public NameBuilderParserException(String message) {
		super(message);
	}

}
