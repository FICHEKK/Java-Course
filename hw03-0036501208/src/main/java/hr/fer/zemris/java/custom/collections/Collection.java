package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Class representing a general collection of objects.
 * It offers methods for controlling a collection, such as
 * adding or removing objects from a collection.
 */
public interface Collection {
	/**
	 * Checks whether this collection contains no elements.
	 * 
	 * @return {@code true} if collection contains no elements, otherwise {@code false}
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collection.
	 * 
	 * @return number of currently stored objects in this collection
	 */
	int size();
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object that is being added
	 */
	void add(Object value);
	
	/**
	 * Returns true only if the collection contains given value , as
	 * determined by the {@code equals} method.
	 * <p>
	 * Also, {@code null} is a valid input.
	 * 
	 * @param value object that is being searched
	 * @return {@code true} if the object was found, otherwise {@code false}
	 */
	boolean contains(Object value);
	
	/**
	 * Removes the given object if it is present in the collection.
	 * 
	 * @param value object that is being deleted
	 * @return If the deletion was successful, returns {@code true}, otherwise {@code false}.
	 */
	boolean remove(Object value);
	
	/**
	 * Allocates new array with size equals to the size of this collections,
	 * fills it with collection content and returns the array.
	 * <p>
	 * This method never returns {@code null}.
	 * 
	 * @return Array filled with objects that are currently in this collection.
	 */
	Object[] toArray();
	
	/**
	 * Processes all the objects in this collection. Process operation is
	 * defined by the given Processor.
	 * 
	 * @param processor Processor object that defines the process operation
	 * 		that will be executed on all of the objects in this collection.
	 * @throws NullPointerException if processor is null
	 */
	default void forEach(Processor processor) {
		Objects.requireNonNull(processor);
		
		ElementsGetter getter = createElementsGetter();
		
		while(getter.hasNextElement()) {
			Object currentElement = getter.getNextElement();
			processor.process(currentElement);
		}
	}
	
	/**
	 * Adds all elements from the given collection into the current collection.
	 * Other collection remains unchanged.
	 * 
	 * @param other Collection that is a source of elements for this collection.
	 */
	default void addAll(Collection other) {
		class AddElementsProcessor implements Processor {
			@Override
			public void process(Object value) {
				add(value);
			}
		}
		
		other.forEach(new AddElementsProcessor());
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	void clear();
	
	/**
	 * Creates {@code ElementsGetter} object that can return
	 * elements from this collection.
	 * 
	 * @return 
	 */
	ElementsGetter createElementsGetter();
	
	/**
	 * Adds all the elements, from given collection to this collection,
	 * that passed the specified test.
	 * 
	 * @param col collection that is a source of elements
	 * @param tester object that specifies the test that elements need to "pass"
	 * 		  in order to be added to this collection
	 * @throws NullPointerException if either collection or tester is {@code null}
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		Objects.requireNonNull(col);
		Objects.requireNonNull(tester);
		
		ElementsGetter getter = col.createElementsGetter();
		
		while(getter.hasNextElement()) {
			Object currentElement = getter.getNextElement();
			
			if(tester.test(currentElement)) {
				this.add(currentElement);
			}
		}
	}
}
