package hr.fer.zemris.java.p12.dao;

/**
 * Models errors during the data accessing.
 *
 * @author Filip Nemec
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public DAOException() {
	}

	/**
	 * Constructs an exception with the message, cause and
	 * flags for enabling suppression and writable stack trace.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression is suppression enabled
	 * @param writableStackTrace is stack trace writable 
	 */
	public DAOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs an exception with the given message and cause.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs an exception with the given message.
	 *
	 * @param message the message
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs an exception with the given cause.
	 *
	 * @param cause the cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}
}