package hr.fer.zemris.java.custom.collections;

/**
 * Enables the user to get elements
 * from a collection that supports object of
 * this ({@code ElementsGetter}) type.
 */
public interface ElementsGetter {
	/**
	 * Checks whether there is next element that can be returned
	 * from the collection.
	 * 
	 * @return {@code true} if there is next element, otherwise {@code false}
	 */
	boolean hasNextElement();
	
	/**
	 * Gets the next element from the collection.
	 * 
	 * @return Next element in the collection.
	 */
	Object getNextElement();
	
	/**
	 * Processes the remaining elements in the collection, defined by
	 * the given {@code Processor} instance.
	 * 
	 * @param p processor that defines how the remaining elements should be processed
	 */
	default void processRemaining(Processor p) {
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
