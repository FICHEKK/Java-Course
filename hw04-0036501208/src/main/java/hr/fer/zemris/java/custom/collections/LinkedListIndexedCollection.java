package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Collection of objects that are being stored in a linked list.
 * Collection allows duplicate elements, however storage
 * of {@code null} elements is not allowed.
 * 
 * @param <T> elements stored in this list are of this type
 */
public class LinkedListIndexedCollection<T> implements List<T> {
	/** Current number of elements in this list. */
	private int size;
	
	/** Reference to the first element in this list. */
	private ListNode<T> first;
	
	/** Reference to the last element in this list. */
	private ListNode<T> last;
	
	/** Counts the number of modifications that were made. */
	private long modificationCount = 0;
	
	/**
	 * Default constructor for this list.
	 */
	public LinkedListIndexedCollection() {
		first = last = null;
		size = 0;
	}
	
	/**
	 * Constructor which copies other collection's elements
	 * to this list.
	 * 
	 * @param other other collection
	 */
	public LinkedListIndexedCollection(Collection<? extends T> other) {
		Objects.requireNonNull(other, "Other collection must not be null!");
		addAll(other);
	}
	
	/**
	 * Adds the given object to the list at the end.
	 * 
	 * @param value value to be added
	 * @throws NullPointerException if given value is null
	 */
	public void add(T value) {
		Objects.requireNonNull(value);
		
		if(first == null) {
			first = new ListNode<T>(value, null, null);
			last = first;
		} else {
			var newNode = new ListNode<T>(value, null, last);
			last.next = newNode;
			last = newNode;
		}
		
		size++;
		modificationCount++;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	/**
	 * Returns the object for the given index in the linked list.
	 * Method uses a smart way of picking from which side of the list
	 * it will start searching for the wanted element, reducing the
	 * search complexity by half.
	 * 
	 * @param index position in the linked list
	 * @return object for given index
	 * @throws IndexOutOfBoundsException if index is negative or bigger than
	 * 									 the current size of the linked list
	 */
	public T get(int index) {
		requireInBounds(0, index, size - 1);
		return getListNodeAtIndex(index).value;
	}
	
	/**
	 * Clears the whole list by "forgetting" about it - setting
	 * {@code null} values to {@code first} and {@code last ListNode} references.
	 */
	@Override
	public void clear() {
		first = last = null;
		size = 0;
		modificationCount++;
	}
	
	/**
	 * Inserts list node for specified index, unless the given index
	 * is out of valid bounds.
	 * 
	 * Average complexity is O(1).
	 * 
	 * @param value value being inserted
	 * @param position index of the removing element
	 * @throws NullPointerException if given value is {@code null}
	 */
	public void insert(T value, int position) {
		Objects.requireNonNull(value);
		requireInBounds(0, position, size);
		
		if(position == 0) {
			first.prev = new ListNode<T>(value, first, null);
			first = first.prev;
		} else if(position == size) {
			last.next = new ListNode<T>(value, null, last);
			last = last.next;
		} else {
			ListNode<T> node = getListNodeAtIndex(position);
			node.prev.next = new ListNode<T>(value, node, node.prev);
			node.prev = node.prev.next;
		}
		
		size++;
		modificationCount++;
	}
	
	/**
	 * Removes list node for specified index, unless the given index
	 * is out of valid bounds.
	 * 
	 * @param index index of the removing element
	 */
	public void remove(int index) {
		requireInBounds(0, index, size - 1);
		
		if(index == 0) {
			first = first.next;
			first.prev = null;
		} else if(index == size - 1) {
			last = last.prev;
			last.next = null;
		} else {
			ListNode<T> node = getListNodeAtIndex(index);
			node.prev.next = node.next;
			node.next.prev = node.prev;
		}
		
		size--;
		modificationCount++;
	}
	
	@Override
	public boolean remove(Object value) {
		int removingObjectIndex = indexOf(value);
		
		if(removingObjectIndex >= 0) {
			remove(removingObjectIndex);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the array representation of this collection.
	 */
	@Override
	public Object[] toArray() {
		Object[] nonNullObjects = new Object[size];
		
		ListNode<T> currentNode = first;
		int currentIndex = 0;
		
		while(currentNode != null) {
			nonNullObjects[currentIndex] = currentNode.value;
			currentNode = currentNode.next;
			currentIndex++;
		}
		
		return nonNullObjects;
	}
	
	/**
	 * Requires the given value to be within the specified range.
	 * 
	 * @param lowerBound the lower bound
	 * @param value the value
	 * @param upperBound the upper bound
	 * @throws IndexOutOfBoundsException if value is out of range
	 */
	private static void requireInBounds(int lowerBound, int value, int upperBound) {
		if(value < lowerBound || value > upperBound) {
			throw new IndexOutOfBoundsException("Given value is out of range!"
					+ "Should be in this range: [" + lowerBound + ", " + upperBound + "]");
		}
	}
	
	/**
	 * Searches for the given object, and if found, returns it's index.
	 * Uses method {@code equals} for searching.
	 * 
	 * Average complexity is O(n).
	 * 
	 * @param value value that is being searched for
	 * @return index if found, {@code -1} if not found
	 */
	public int indexOf(Object value) {
		if(value == null)
			return -1;
		
		ListNode<T> currentNode = first;
		int currentIndex = 0;
		
		while(currentNode != null) {
			if(currentNode.value.equals(value)) {
				return currentIndex;
			}
			currentNode = currentNode.next;
			currentIndex++;
		}
		
		return -1;
	}
	
	@Override
	public boolean contains(Object value) {
		return indexOf(value) != -1;
	}
	
	/**
	 * Retrieves the node for the given index.
	 * 
	 * @param index index of the node
	 * @return node for the given index
	 */
	private ListNode<T> getListNodeAtIndex(int index) {
		ListNode<T> currentNode = null;
		
		if(index <= size / 2) {
			currentNode = first;
			int currentIndex = 0;
			
			while(currentIndex != index) {
				currentNode = currentNode.next;
				currentIndex++;
			}
		} else {
			currentNode = last;
			int currentIndex = size - 1;
			
			while(currentIndex != index) {
				currentNode = currentNode.prev;
				currentIndex--;
			}
		}
		
		return currentNode;
	}
	
	/**
	 * Helper method that prints this linked list
	 * into the console.
	 */
	public void print() {
		if(first == null) {
			System.out.println("Linked list is empty!");
		} else {
			ListNode<T> temp = first;
			while(temp != null) {
				System.out.print(temp.value + " | NEXT: ");
				
				if(temp.next != null) {
					System.out.print(temp.next.value + " | PREV: ");
				} else {
					System.out.print("NULL | PREV: ");
				}
				
				if(temp.prev != null) {
					System.out.println(temp.prev.value);
				} else {
					System.out.println("NULL");
				}
				
				temp = temp.next;
			}
		}
	}
	
	/**
	 * Inner class that represents a single node
	 * in a linked list.
	 * <p>
	 * It has references to previous and next node, as well
	 * as another reference for the value it stores.
	 */
	private static class ListNode<E> {
		/** Value stored by this node. */
		E value;
		
		/** Reference to the next node in the list. */
		ListNode<E> next;
		
		/** Reference to the previous node in the list. */
		ListNode<E> prev;
		
		/** Constructs the ListNode object */
		private ListNode(E value, ListNode<E> next, ListNode<E> prev) {
			this.value = value;
			this.next = next;
			this.prev = prev;
		}
	}
	
	@Override
	public ElementsGetter<T> createElementsGetter() {
		return new ElementsGetterLinkedList<T>(this, modificationCount);
	}
	
	private static class ElementsGetterLinkedList<E> implements ElementsGetter<E> {
		private ListNode<E> currentListNode;
		private LinkedListIndexedCollection<E> collection;
		private long savedModificationCount;
		
		private ElementsGetterLinkedList(LinkedListIndexedCollection<E> collection, long modificationCount) {
			this.collection = collection;
			this.currentListNode = this.collection.first;
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
			return currentListNode != null;
		}
		
		/**
		 * @throws NoSuchElementException if there are no more elements
		 * @throws ConcurrentModificationException if the collection was modified
		 */
		@Override
		public E getNextElement() {
			if(hasNextElement()) {
				E toBeReturned = currentListNode.value;
				currentListNode = currentListNode.next;
				return toBeReturned;
			} else {
				throw new NoSuchElementException("Already returned the last element from the list!");
			}
		}
	}
}
