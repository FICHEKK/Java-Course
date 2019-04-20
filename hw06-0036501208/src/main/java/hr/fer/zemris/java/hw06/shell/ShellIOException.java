package hr.fer.zemris.java.hw06.shell;

/**
 * Defines an exception which can be thrown during
 * the usage of shell.
 *
 * @author Filip Nemec
 */
public class ShellIOException extends RuntimeException {
	
	/** Serial UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new shell exception.
	 */
	public ShellIOException() {
		super();
	}
	
	/**
	 * Constructs a new shell exception, with specified
	 * message.
	 *
	 * @param message the message
	 */
	public ShellIOException(String message) {
		super(message);
	}
}
