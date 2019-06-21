package hr.fer.zemris.java.hw15.dao;

/**
 * The DAO (Data Access Object) exceptions.
 *
 * @author Filip Nemec
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code DAOException} with the
	 * given message and cause.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new {@code DAOException} with the
	 * given message.
	 *
	 * @param message the message
	 */
	public DAOException(String message) {
		super(message);
	}
}