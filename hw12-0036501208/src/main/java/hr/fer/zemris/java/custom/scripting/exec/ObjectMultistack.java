package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple data structure that models a map of
 * stacks. It maps the given stack key-name to
 * its corresponding stack.
 * <p>
 * Stacks are realized using the singly linked
 * list approach.
 *
 * @author Filip Nemec
 */
public class ObjectMultistack {
	
	/** The map that maps the {@code String} to the appropriate stack. */
	private Map<String, MultistackEntry> map = new HashMap<>();
	
	/**
	 * Pushes the given value to the stack with the given key-name
	 * value.
	 *
	 * @param keyName the key-name of the required stack
	 * @param valueWrapper the value being pushed
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		var newEntry = new MultistackEntry(valueWrapper);
		newEntry.next = map.get(keyName);
		map.put(keyName, newEntry);
	}
	
	/**
	 * Returns and then removes the value at the peak of the stack
	 * with the given key-name.
	 *
	 * @param keyName the key-name of the required stack
	 * @return the value at the peak of required stack
	 * @throws IllegalStateException if the stack for the given key-name
	 * 								 is empty
	 */
	public ValueWrapper pop(String keyName) {
		requireNotEmpty(keyName, "Cannot pop from an empty stack.");
		
		var entry = map.get(keyName);
		map.put(keyName, entry.next);
		return entry.getValueWrapper();
	}
	
	/**
	 * Peeks at the stack with the given key-name.
	 *
	 * @param keyName the stack key-name
	 * @return the value at the peak of the stack
	 * @throws IllegalStateException if the stack for the given key-name
	 * 								 is empty
	 */
	public ValueWrapper peek(String keyName) {
		requireNotEmpty(keyName, "Cannot peek at an empty stack.");
		return map.get(keyName).getValueWrapper();
	}
	
	/**
	 * Requires that the stack with the given key-name is not empty.
	 *
	 * @param keyName the stack key-name
	 * @throws IllegalStateException if the stack for the given key-name
	 * 								 is empty
	 */
	private void requireNotEmpty(String keyName, String message) {
		if(isEmpty(keyName))
			throw new IllegalStateException(message);
	}

	/**
	 * Checks if the stack with the given key is empty.
	 *
	 * @param keyName the stack key
	 * @return {@code true} if it is empty, {@code false} otherwise
	 */
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
	}
	
	/**
	 * Prints the stack with the specified key-name.
	 * 
	 * @param keyName the key-name for specifying the stack
	 */
	public void printStack(String keyName) {
		System.out.println("Stack '" + keyName + "':");
		System.out.println();
		
		var peak = map.get(keyName);
		
		if(peak == null) {
			System.out.println("This stack is empty.");
		} else {
			while(peak != null) {
				System.out.println(String.format("| %10s |", peak.getValueWrapper().getValue()));
				System.out.println("-".repeat(14));
				peak = peak.next;
			}
		}
	}
	
	/**
	 * Prints all the stacks that current reside in this multi-stack instance.
	 */
	public void printAllStacks() {
		System.out.println("=".repeat(30));
		
		for(Map.Entry<String, MultistackEntry> entry : map.entrySet()) {
			printStack(entry.getKey());
			System.out.println("=".repeat(30));
		}
	}
	
	/**
	 * Simple class that models an entry for the
	 * multistack object.
	 *
	 * @author Filip Nemec
	 */
	private static class MultistackEntry {
		
		/** Reference to the next entry. */
		private MultistackEntry next;
		
		/** Value wrapper that this entry holds. */
		private ValueWrapper valueWrapper;
		
		/**
		 * Constructs a new entry that holds the given value.
		 * 
		 * @param valueWrapper the value of this entry
		 */
		public MultistackEntry(ValueWrapper valueWrapper) {
			this.valueWrapper = valueWrapper;
		}
		
		/**
		 * @return the value that this entry holds
		 */
		public ValueWrapper getValueWrapper() {
			return valueWrapper;
		}
	}
}
