package hr.fer.zemris.java.gui.layouts;

/**
 * Models the exceptions that can be thrown during the
 * usage or construction of the calculator.
 *
 * @author Filip Nemec
 */
public class CalcLayoutException extends RuntimeException {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code CalcLayoutException}.
	 */
	public CalcLayoutException() {
		super();
	}
	
	/**
	 * Constructs a new {@code CalcLayoutException}, with the
	 * given message.
	 *
	 * @param message the message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}
}
