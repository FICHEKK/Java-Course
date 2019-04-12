package hr.fer.zemris.java.custom.collections;

/**
 * Models the exception that can be will thrown
 * once the user tries to get element from an
 * empty stack.
 */
public class EmptyStackException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an exception that is usually thrown if the
	 * user tries to get elements from an empty stack.
	 */
	public EmptyStackException() {
		super();
	}

	/**
	 * Constructs an exception with message that is usually thrown if
	 * the user tries to get elements from an empty stack.
	 * 
	 * @param message the message
	 */
    public EmptyStackException(String message) {
        super(message);
    }
}
