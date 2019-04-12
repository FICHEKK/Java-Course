package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * A simple hash table implementation that uses a linked
 * list approach when it comes to collision.
 *
 * @param <K> key type
 * @param <V> value type
 * 
 * @author Filip Nemec
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
	
	/** Default capacity if no capacity was provided in a constructor. */
	private final static int DEFAULT_CAPACITY = 16;
	
	/** Minimal capacity. Can't be lower than this number */
	private final static int MIN_CAPACITY = 1;
	
	/** Hash table can be this full in percentage before its size doubles. */
	private static final double MAXIMUM_ALLOWED_FILL_PERCENTAGE = 0.75;
	
	/** Maximum hash table capacity that can be allocated. */
	private static final int CAPACITY_LIMIT = Integer.MAX_VALUE >> 7; // 16777216
	
	/** Hash table that stores the elements. */
	private TableEntry<K, V>[] table;
	
	/** Current number of elements in this hash table. */
	private int size;
	
	/** Counts the number of slots that are filled with atleast 1 element. */
	private int filledSlots;
	
	/** Counts the number of modification that were made to this hash table. */
	private long modificationCount;
	
	/**
	 * The default constructor that sets the array size to {@value #DEFAULT_CAPACITY}
	 */
	public SimpleHashtable() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructs the hash table with the given capacity.
	 * Provided capacity should not be lower than {@value #MIN_CAPACITY}
	 * @param capacity
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if(capacity < MIN_CAPACITY) {
			throw new IllegalArgumentException(capacity + " should not be lower than " + MIN_CAPACITY);
		}
		
		int slotCount = firstPowerOf2BiggerOrEqualThan(capacity);
		table = new TableEntry[slotCount];	
	}
	
	/**
	 * Puts the given key-value pair in this hash table.
	 * If the given key is already in the hash table, value
	 * will just get updated. Otherwise, a brand new entry will
	 * be added to the hash table.
	 * 
	 * @param key the key
	 * @param value the value
	 * @throws NullPointerException if given key is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key);
		
		int index = getIndex(key, table.length);
		TableEntry<K, V> entry = table[index];
		
		if(entry == null) {
			table[index] = new TableEntry<K, V>(key, value, null);
			increaseFilledSlotsCount();
		} else {
			while(true) {
				// If our key already exists, just update the value.
				if(entry.key.equals(key)) {
					entry.value = value;
					return;
				}
				
				// If next entry exists, lets check it, otherwise break.
				if(entry.next != null) {
					entry = entry.next;
				} else {
					break;
				}
			}
			
			// Our key was not found, add new entry to the end of the list.
			entry.next = new TableEntry<K, V>(key, value, null);
		}
		
		size++;
		modificationCount++;
	}
	
	/**
	 * Returns the value paired by the given key.
	 * It does not delete the value from the hash table.
	 * 
	 * @param key the key of the wanted value
	 * @return value if found, {@code null} otherwise
	 */
	public V get(Object key) {
		var entry = getTableEntry(key);
		return entry == null ? null : entry.value;
	}
	
	/**
	 * Returns the current number of elements in this
	 * hash table.
	 * 
	 * @return current number of elements
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Checks if the given key currently resides in
	 * this hash table.
	 * 
	 * Complexity of this operation is O(1) for well
	 * dispersed hash table.
	 * 
	 * @param key key being searched for
	 * @return {@code true} if the key was found, {@code false} otherwise
	 */
	public boolean containsKey(Object key) {
		return getTableEntry(key) != null;
	}
	
	/**
	 * Returns the table entry with the given key.
	 * 
	 * @param key key of the entry
	 * @return entry with the given key if found, {@code null} otherwise
	 */
	private TableEntry<K, V> getTableEntry(Object key) {
		if(key == null) return null;
		
		int index = getIndex(key, table.length);
		TableEntry<K, V> entry = table[index];
		
		while(entry != null) {
			if(entry.key.equals(key)) {
				return entry;
			}
			entry = entry.next;
		}
		
		return null;
	}
	
	/**
	 * Checks if the given value currently resides in
	 * this hash table.
	 * 
	 * Complexity of this operation is O(n) for well
	 * dispersed hash table.
	 * 
	 * @param value value being searched for
	 * @return {@code true} if the value was found, {@code false} otherwise
	 */
	public boolean containsValue(Object value) {
		for(TableEntry<K, V> entry : table) {
			while(entry != null) {
				if(entry.value.equals(value)) {
					return true;
				}
				entry = entry.next;
			}
		}
		return false;
	}
	
	/**
	 * Removes the entry with the given key value.
	 * 
	 * @param key key of the entry that will be removed
	 */
	public void remove(Object key) {
		if(key == null) return;
		
		int index = getIndex(key, table.length);
		TableEntry<K, V> entry = table[index];
		
		// This slot is empty, therefore it does not
		// contain element with the given key.
		if(entry == null) return;
		
		// If it is first element.
		if(entry.key.equals(key)) {
			table[index] = entry.next;
			
			// If this slot is now empty.
			if(table[index] == null) {
				filledSlots--;
			}
			
			size--;
			modificationCount++;
			return;
		}
		
		while(true) {
			// save current entry because our list is not 
			// doubly linked so we can't go back
			var previous = entry;
			entry = entry.next; // [previous] -> [entry] -> [entry.next] -> ...
			
			if(entry == null) return;
			
			if(entry.key.equals(key)) {
				previous.next = entry.next;
				size--;
				modificationCount++;
				return;
			}
		}
	}
	
	/**
	 * Clears all the elements from the collection,
	 * but does <b>not</b> resize it to the default
	 * capacity.
	 */
	public void clear() {
		for(int i = 0; i < table.length; i++) {
			table[i] = null;
		}
		size = 0;
		filledSlots = 0;
		modificationCount++;
	}
	
	/**
	 * Checks if this hash table has no elements.
	 * 
	 * @return {@code true} if it is empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns the first 2<sup>n</sup> {@code int} value bigger or equal than the given value.
	 * 
	 * @param value the value
	 * @return first 2<sup>n</sup> {@code int} value bigger or equal than the given value
	 */
	private static int firstPowerOf2BiggerOrEqualThan(int value) {
		int powerOfTwo = 1;
		for(; value > powerOfTwo && powerOfTwo < CAPACITY_LIMIT; powerOfTwo *= 2);
		return powerOfTwo;
	}
	
	/**
	 * Generates the hash table index for the given key.
	 * 
	 * @param key key used in hashing
	 * @param modulo the modulo which limits the output
	 * @return hash table index for the given key
	 */
	private int getIndex(Object key, int modulo) {
		// Clear the first bit in case the hash code is negative.
		return (key.hashCode() & 0x7fff_ffff) % modulo;
	}
	
	/**
	 * Increases the fill slot count and checks if this
	 * hash table should be resizes in order to keep
	 * efficient dispersion.
	 */
	private void increaseFilledSlotsCount() {
		filledSlots++;
		modificationCount++;
		
		// Cast to double because of integer rounding.
		if((double)filledSlots / table.length >= MAXIMUM_ALLOWED_FILL_PERCENTAGE) {
			doubleTheCapacity();
		}
	}
	
	/**
	 * Doubles the capacity of this hash table, and re-adds
	 * all the elements from the previous table
	 */
	@SuppressWarnings("unchecked")
	private void doubleTheCapacity() {
		var doubled = new TableEntry[table.length * 2];
		
		for(int i = 0; i < table.length; i++) {
			
			for(var entry = table[i]; entry != null; entry = entry.next) {
				int newIndex = getIndex(entry.key, doubled.length);
				var entryCopy = new TableEntry<K, V>(entry.key, entry.value, null);
				
				var entryInDoubled = doubled[newIndex];
				if(entryInDoubled == null) {
					doubled[newIndex] = entryCopy;
				} else {
					for(; entryInDoubled.next != null; entryInDoubled = entryInDoubled.next);
					entryInDoubled.next = entryCopy;
				}
			}
			
		}
		
		table = doubled;
		modificationCount++;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		int appendedElements = 0;
		
		sb.append("[");
		for(TableEntry<K, V> entry : table) {
			while(entry != null) {
				sb.append(entry);
				
				if(++appendedElements < size) {
					sb.append(", ");
				}
				
				entry = entry.next;
			}
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * Prints this hash-table to the console, slot by slot.
	 */
	public void printHashtableState() {
		var sb = new StringBuilder();

		for (TableEntry<K, V> entry : table) {
			sb.append("[");
			while (entry != null) {
				sb.append(entry);

				entry = entry.next;
				if (entry == null) break;

				sb.append(", ");
			}
			sb.append("]\n");
		}

		System.out.println(sb.toString());
	}
	
	/**
	 * Simple entry class with a key-value pair.
	 *
	 * @param <K> key type for this entry
	 * @param <V> value type for this entry
	 */
	public static class TableEntry<K, V> {
		
		/** Key of this entry. */
		private K key;
		
		/** Value of this entry. */
		private V value;
		
		/** Reference to the next entry in the list. */
		private TableEntry<K, V> next;
		
		/**
		 * Constructs a new key-value entry.
		 * 
		 * @param key the key of this entry
		 * @param value the value of this entry
		 * @param next reference to the next entry in the list
		 */
		public TableEntry(K key, V value, TableEntry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		
		/**
		 * Returns the value of this entry.
		 * @return the value of this entry
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Sets the value of this entry.
		 * @param value new value for this entry
		 */
		public void setValue(V value) {
			this.value = value;
		}
		
		/**
		 * Returns the key of this entry.
		 * @return the key of this entry
		 */
		public K getKey() {
			return key;
		}
		
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new SimpleHashtableIterator();
	}
	
	private class SimpleHashtableIterator implements Iterator<TableEntry<K, V>> {
		
		/** Current index of the slot we are searching for next entries. */
		private int index;
		
		/** Reference to the last searched table entry. */
		private TableEntry<K, V> entry;
		
		/** Counts the number of returned entries. */
		private int returnedEntries;
		
		/** If the user is allowed to call {@link #remove()} method. */
		private boolean canCallRemove = false;
		
		/** Hash table modification count when this iterator was constructed. */
		private long savedModificationCount;
		
		/**
		 * Constructs a new iterator and saves the hash table modification count.
		 */
		private SimpleHashtableIterator() {
			this.savedModificationCount = modificationCount;
		}

		@Override
		public boolean hasNext() {
			if(savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hash table was modified while iterating.");
			}
			
			return returnedEntries < size;
		}

		@Override
		public TableEntry<K, V> next() {
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more elements.");
			}
			
			returnedEntries++;
			canCallRemove = true;
			
			if(entry == null || (entry != null && entry.next == null)) {
				for(; index < table.length; index++) {
					if(table[index] != null) {
						return entry = table[index++];
					}
				}
			}

			return entry = entry.next;
		}
		
		@Override
		public void remove() {
			if(savedModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Hash table was modified while iterating.");
			}
			
			if(!canCallRemove) {
				String msg = "next() was either not yet called, or remove() called twice in a row.";
				throw new IllegalStateException(msg);
			}
			
			SimpleHashtable.this.remove(entry.key);
			
			returnedEntries--; // Since the size is decreasing, so does this need to.
			savedModificationCount++;
			canCallRemove = false;
		}
	}
}
