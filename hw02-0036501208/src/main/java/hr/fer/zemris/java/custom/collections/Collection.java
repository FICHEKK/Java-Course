package hr.fer.zemris.java.custom.collections;

/**
 * Class representing a general collection of objects.
 * It offers methods for controlling a collection, such as
 * adding or removing objects from a collection.
 */
public class Collection {
	/**
	 * Default constructor of this class.
	 */
	protected Collection() {
		
	}
	
	/**
	 * Checks whether this collection contains no elements.
	 * 
	 * @return {@code true} if collection contains no elements, otherwise {@code false}
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * Returns the number of currently stored objects in this collection.
	 * 
	 * @return number of currently stored objects in this collection
	 */
	public int size() {
		return 0;
	}
	
	/**
	 * Adds the given object into this collection.
	 * 
	 * @param value object that is being added
	 */
	public void add(Object value) {
		
	}
	
	/**
	 * Returns true only if the collection contains given value , as
	 * determined by the {@code equals} method.
	 * <p>
	 * Also, {@code null} is a valid input.
	 * 
	 * @param value object that is being searched
	 * @return {@code true} if the object was found, otherwise {@code false}
	 */
	public boolean contains(Object value) {
		return false;
	}
	
	/**
	 * Removes the given object if it is present in the collection.
	 * 
	 * @param value object that is being deleted
	 * @return If the deletion was successful, returns {@code true}, otherwise {@code false}.
	 */
	public boolean remove(Object value) {
		return false;
	}
	
	/**
	 * Allocates new array with size equals to the size of this collections,
	 * fills it with collection content and returns the array.
	 * <p>
	 * This method never returns {@code null}.
	 * 
	 * @return Array filled with objects that are currently in this collection.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Processes all the objects in this collection. Process operation is
	 * defined by the given Processor.
	 * 
	 * @param processor Processor object that defines the process operation
	 * 		that will be executed on all of the objects in this collection.
	 * 		 
	 */
	public void forEach(Processor processor) {

	}
	
	/**
	 * Adds all elements from the given collection into the current collection.
	 * Other collection remains unchanged.
	 * 
	 * @param other Collection that is a source of elements for this collection.
	 */
	public void addAll(Collection other) {
		class AddElementsProcessor extends Processor {
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
	public void clear() {
		
	}
}
