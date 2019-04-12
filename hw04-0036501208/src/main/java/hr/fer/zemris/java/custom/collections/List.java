package hr.fer.zemris.java.custom.collections;

/**
 * Represents the list data structure and its
 * required methods.
 * 
 * @param <T> the type of elements in this list
 */
public interface List<T> extends Collection<T> {
	/**
	 * Returns the element for the given index.
	 * 
	 * @param index position in list
	 * @return object at position {@code index}
	 */
	T get(int index);
	
	/**
	 * Inserts the given value to the given position
	 * in this list.
	 * 
	 * @param value value to be inserted
	 * @param position given value will be found at
	 * 		  this position
	 */
	void insert(T value, int position);
	
	/**
	 * Searches the list for the given value and
	 * returns its index if found.
	 * 
	 * @param value
	 * @return index if the given value was found,
	 * 		   otherwise {@code -1}
	 */
	int indexOf(Object value);
	
	/**
	 * Removes the object at given index.
	 * 
	 * @param index position of the removing object
	 */
	void remove(int index);
}
