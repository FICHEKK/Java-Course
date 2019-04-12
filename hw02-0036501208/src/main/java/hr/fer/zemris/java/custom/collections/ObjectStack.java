package hr.fer.zemris.java.custom.collections;

/**
 * Class that represents stack for objects.
 * It is an adapter design, with underlying implementation
 * of ArrayIndexedCollection as it's data storage.
 * <p>
 * It does not allow storing {@code null} values.
 */
public class ObjectStack {
	/** Stack elements are saved in this array collection. */
	private ArrayIndexedCollection stack;
	
	/**
	 * Constructs the stack with the default size.
	 */
	public ObjectStack() {
		stack = new ArrayIndexedCollection();
	}
	
	/**
	 * Constructs the stack for with the given capacity.
	 * 
	 * @param initialCapacity capacity of this newly created stack
	 */
	public ObjectStack(int initialCapacity) {
		stack = new ArrayIndexedCollection(initialCapacity);
	}
	
	/**
	 * Checks if the stack has zero elements.
	 * 
	 * @return returns {@code true} if empty, otherwise {@code false}
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Returns current number of elements in stack.
	 * 
	 * @return current number of elements in stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Places the given value on top of stack.
	 * Given value should not be {@code null}.
	 * 
	 * @param value value to be pushed
	 */
	public void push(Object value) {
		stack.add(value);
	}
	
	/**
	 * Returns the element that is on top of stack
	 * and removes it from stack.
	 * 
	 * @return value that resides on top of stack
	 * @throws EmptyStackException if the stack was empty
	 */
	public Object pop() {
		if(isEmpty())
			throw new EmptyStackException("Can't get an element from an empty stack!");
		
		int last = stack.size() - 1;
		Object lastObject = stack.get(last);
		stack.remove(last);
		return lastObject;
	}
	
	/**
	 * Returns the element that is on top of stack,
	 * but does not remove it from stack.
	 * 
	 * @return value that resides on top of stack
	 * @throws EmptyStackException if the stack was empty
	 */
	public Object peek() {
		if(isEmpty())
			throw new EmptyStackException("Can't peek at an element from an empty stack!");
		
		return stack.get(stack.size() - 1);
	}
	
	/**
	 * Empties the entire stack.
	 */
	public void clear() {
		stack.clear();
	}
}
