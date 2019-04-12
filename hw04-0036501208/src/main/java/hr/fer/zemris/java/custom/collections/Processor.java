package hr.fer.zemris.java.custom.collections;

/**
 * Represents a processor that processes a given value in
 * a certain way.
 * 
 * @param <T> the type of elements that this processor will be processing
 */
@FunctionalInterface
public interface Processor<T> {
	/**
	 * Processes the given value in a specified way.
	 * 
	 * @param value value to be processed
	 */
	void process(T value);
}
