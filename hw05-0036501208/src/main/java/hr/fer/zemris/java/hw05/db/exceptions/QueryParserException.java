package hr.fer.zemris.java.hw05.db.exceptions;

/**
 * Models a type of exception that can be thrown
 * if a problem occurs during the parsing of a query.
 *
 * @author Filip Nemec
 */
public class QueryParserException extends RuntimeException {
	
	/** A serial version UID used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new query parser exception.
	 */
	public QueryParserException() {
		super();
	}
	
	/**
	 * Constructs a new query parser exception with
	 * the given message.
	 * 
	 * @param message the message that says what caused this exception
	 */
	public QueryParserException(String message) {
		super(message);
	}
}
