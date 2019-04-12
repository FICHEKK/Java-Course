package hr.fer.zemris.java.custom.collections;

/**
 * Represents a processor that processes a given value in
 * a certain way.
 */
@FunctionalInterface
public interface Processor {
	/**
	 * Processes the given value in a specified way.
	 * 
	 * @param value value to be processed
	 */
	void process(Object value);
}
