package hr.fer.zemris.java.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Collection of objects that are being stored in an array.
 * Collection allows duplicate elements, however storage
 * of {@code null} elements is not allowed.
 * 
 * @param <T> the type of elements in this list
 * 
 * @author Filip Nemec
 */
public class ArrayIndexedCollection<T> implements List<T> {
	/** Current number of elements in this collection. */
	private int size = 0;
	
	/** Array that stores objects of this collection. */
	private T[] elements;
	
	/** Maximum number of objects this collection can store. */
	private int capacity;
	
	/** Counts the number of modifications that were made. */
	private long modificationCount = 0;
	
	/** If capacity was not provided in a constructor,
	 * this value is used instead. */
	private static final int DEFAULT_CAPACITY = 16;
	
	/** Minimum capacity of this collection. If provided
	 * capacity is smaller than this number, an exception
	 * is thrown. */
	private static final int MINIMUM_CAPACITY = 1;
	
	/** Default constructor of this collection. It sets
	 * the collection capacity to the default value.
	 */
	public ArrayIndexedCollection() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that sets the capacity of this collection
	 * to the given value.
	 * 
	 * @param initialCapacity capacity of this collection
	 * @throws IllegalArgumentException if the given capacity is less than 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		initializeArrayOfSize(initialCapacity);
	}
	
	/**
	 * Constructor which takes other collection and copies it's contents
	 * into this collection.
	 * 
	 * @param other collection which is the source of objects
	 * @throws NullPointerException if other collection is null
	 */
	public ArrayIndexedCollection(Collection<? extends T> other) {
		this(other, other.size());
	}
	
	/**
	 * Constructor which takes other collection and copies it's contents
	 * into this collection, but also specifies this collection's initial capacity.
	 * 
	 * @param other collection which is the source of objects
	 * @param initialCapacity initial capacity of this collection
	 * @throws NullPointerException if other collection is null
	 * @throws IllegalArgumentException if the given capacity is less than 1
	 */
	public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
		Objects.requireNonNull(other, "Other collection must not be null!");
		
		initialCapacity = initialCapacity >= other.size() ? initialCapacity : other.size();
		initializeArrayOfSize(initialCapacity);
		
		addAll(other);
	}
	
	/**
	 * Initializes internal array of given size.
	 * 
	 * @param initialCapacity size of internal array
	 * @throws IllegalArgumentException if the given capacity is less than 1
	 */
	@SuppressWarnings("unchecked")
	private void initializeArrayOfSize(int initialCapacity) {
		if(initialCapacity < MINIMUM_CAPACITY) {
			throw new IllegalArgumentException("Initial capacity should be of atleast size 1!");
		}
		
		capacity = initialCapacity;
		elements = (T[]) new Object[capacity];
	}
	
	/**
	 * Adds given value in the array. If there is no more space left,
	 * doubles the size of the current array.
	 * 
	 * Average complexity of this method is O(1).
	 * 
	 * @throws NullPointerException if given value is {@code null}
	 */
	@Override
	public void add(T value) {
		Objects.requireNonNull(value);
		
		if(size == capacity) {
			doubleTheArrayCapacity();
		}
		
		elements[size] = value;
		size++;
		modificationCount++;
	}
	
	/**
	 * Returns object from array for given index unless the index is out
	 * of range.
	 * 
	 * Average complexity of this method is O(1).
	 * 
	 * @param index index in an array
	 * @return object on position {@code index}
	 * @throws IndexOutOfBoundsException if index is invalid
	 */
	public T get(int index) {
		if(index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Index should have been in range from 0 (inclusive) to " +
				 size + " (exclusive). Given index was " + index + ".");
		}
		
		return elements[index];
	}
	
	/**
	 * Clears the whole array by setting {@code null} value on each
	 * position in the array.
	 */
	@Override
	public void clear() {
		for(int i = 0; i < size - 1; i++) {
			elements[i] = null;
		}
		
		size = 0;
		modificationCount++;
	}
	
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Returns the current number of elements in this collection.
	 */
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Inserts the given value to the given position in an array.
	 * It does not overwrite the value at that position, but moves
	 * all the values to the right to make room for the new value.
	 * 
	 * Average complexity of this method is O(n).
	 * 
	 * @param value value to be inserted
	 * @param position position for the new value
	 * @throws IndexOutOfBoundsException if index is negative or higher than
	 * 									 current element count
	 * @throws NullPointerException if {@code value} is {@code null}
	 */
	public void insert(T value, int position) {
		Objects.requireNonNull(value);
		
		if(position == size) {
			add(value);
			return;
		}
		
		if(position < 0 || position > size - 1) {
			throw new IndexOutOfBoundsException("Index should have been in range from 0 (inclusive) to " +
				 size + " (inclusive). Given index was " + position + ".");
		}
		
		if(size == capacity) {
			doubleTheArrayCapacity();
		}
		
		for(int i = size; i > position; i--) {
			elements[i] = elements[i - 1];
		}
		
		elements[position] = value;
		size++;
		modificationCount++;
	}
	
	/**
	 * Searches for the given value in the collection. Returns the index
	 * in array if found, otherwise returns -1. Also returns -1 for {@code null}.
	 * 
	 * Average complexity of this method is O(n).
	 * 
	 * @param value value that is being searched for
	 * @return index in the array if the value was found, otherwise {@code -1}
	 */
	public int indexOf(Object value) {
		if(value == null)
			return -1;
		
		for(int i = 0; i < size; i++) {
			if(elements[i].equals(value)) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Removes element at specified index from this collection,
	 * unless the index is out of bounds.
	 * 
	 * @param index index of object that is being removed
	 * @throws IndexOutOfBoundsException if the collection is empty
	 * 									 or if index is out of bounds
	 */
	public void remove(int index) {
		if(size == 0) {
			throw new IndexOutOfBoundsException("Can't remove from an empty collection!");
		}
		
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index should have been in range from 0 (inclusive) to " +
				 size + " (inclusive). Given index was " + index + ".");
		}
		
		for(int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		
		elements[size - 1] = null;
		size--;
		modificationCount++;
	}
	
	/**
	 * {@inheritDoc}
	 * Removes the given object if it is present
	 * in this array. It uses {@code equals} method
	 * for determining if the given value is the same
	 * as the element in an array.
	 */
	@Override
	public boolean remove(Object value) {
		int indexOfObject = indexOf(value);
		
		if(indexOfObject < 0) {
			return false;
		} else {
			remove(indexOfObject);
			return true;
		}
	}
	
	/**
	 * Returns the array representation of this collection.
	 * The array contains only non-null values.
	 * 
	 * @return Array of all non-null elements in this collection.
	 */
	@Override
	public Object[] toArray() {
		Object[] nonNullObjects = new Object[size];
		
		for(int i = 0; i < size; i++) {
			nonNullObjects[i] = elements[i];
		}
		
		return nonNullObjects;
	}
	
	/**
	 * Doubles the capacity of internal array.
	 */
	private void doubleTheArrayCapacity() {
		capacity = capacity * 2;
		elements = Arrays.copyOf(elements, capacity);
	}
	
	/**
	 * @return current capacity of this collection
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Prints this collection to the console.
	 */
	public void print() {
		for(int i = 0; i < capacity; i++) {
			if(elements[i] == null)
				System.out.print("null ");
			else 
				System.out.print(elements[i] + " ");
		}
	}
	
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ElementsGetterArrayList<T>(this, modificationCount);
	}
	
	/**
	 * Array indexed collection getter implementation. It retrieves the
	 * elements from the internal array and returns them. If the array
	 * was modified, it will throw exception.W
	 */
	private static class ElementsGetterArrayList<T> implements ElementsGetter<T> {
		/** Index of the current element that this getter is getting. */
		private int currentElementIndex = 0;
		
		/** Reference to the collection. */
		private ArrayIndexedCollection<T> collection;
		
		/** Used to check if the collection was modified. */
		private long savedModificationCount;
		
		private ElementsGetterArrayList(ArrayIndexedCollection<T> collection, long modificationCount) {
			this.collection = collection;
			this.savedModificationCount = modificationCount;
		}
		
		/**
		 * @throws ConcurrentModificationException if the collection was modified
		 */
		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != collection.modificationCount) {
				String message = "This operation is invalid since the original collection was changed!";
				throw new ConcurrentModificationException(message);
			}
			return currentElementIndex < collection.size;
		}
		
		/**
		 * @throws NoSuchElementException if there are no more elements
		 * @throws ConcurrentModificationException if the collection was modified
		 */
		@Override
		public T getNextElement() {
			if(hasNextElement()) {
				T toBeReturned = collection.get(currentElementIndex);
				currentElementIndex++;
				return toBeReturned;
			} else {
				throw new NoSuchElementException("Already returned the last element from an array!");
			}
		}
	}
}