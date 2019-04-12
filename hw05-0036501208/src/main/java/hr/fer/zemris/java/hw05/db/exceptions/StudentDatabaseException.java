package hr.fer.zemris.java.hw05.db.exceptions;

/**
 * Models exceptions that can be thrown during
 * the student database creation.
 *
 * @author Filip Nemec
 */
public class StudentDatabaseException extends RuntimeException {
	
	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new student database exception.
	 */
	public StudentDatabaseException() {
		super();
	}
	
	/**
	 * Constructs a new student database exception with
	 * the given message
	 * 
	 * @param message the message that says what caused this exception
	 */
	public StudentDatabaseException(String message) {
		super(message);
	}
}
