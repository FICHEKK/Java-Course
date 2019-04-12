package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Models a simple representation of a dictionary data structure.
 * In dictionary, entries with key-value pairs are stored and 
 * can be easily accessed and added, mostly in O(1) complexity.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * 
 * @author Filip Nemec
 */
public class Dictionary<K, V> {
	
	/** Array storing all the dictionary entries. */
	private ArrayIndexedCollection<Entry<K, V>> entries;
	
	/**
	 * Constructs a new dictionary with the default capacity.
	 */
	public Dictionary() {
		entries = new ArrayIndexedCollection<Entry<K, V>>();
	}
	
	/**
	 * Constructs a new dictionary with the given initial
	 * capacity.
	 * 
	 * @param initialCapacity size of the internal storage for
	 * 						  this dictionary
	 */
	public Dictionary(int initialCapacity) {
		entries = new ArrayIndexedCollection<Entry<K, V>>(initialCapacity);
	}
	
	/**
	 * Checks whether this dictionary is empty (has zero elements).
	 * 
	 * @return {@code true} if empty, {@code false} otherwise
	 */
	public boolean isEmpty() {
		return entries.isEmpty();
	}
	
	/**
	 * Returns the number of entries in this dictionary.
	 * 
	 * @return number of entries in this dictionary
	 */
	public int size() {
		return entries.size();
	}
	
	/**
	 * Clears the entire dictionary. There are 0 elements in
	 * dictionary afterwards.
	 */
	public void clear() {
		entries.clear();
	}
	
	/**
	 * Puts the given key-value entry into this dictionary.
	 * 
	 * @param key key of the entry
	 * @param value value of the entry
	 * @throws NullPointerException if given {@code key} is {@code null}
	 */
	public void put(K key, V value) {
		Objects.requireNonNull(key, "Can't put entry with a null key.");
		
		int size = entries.size();
		for(int i = 0; i < size; i++) {
			Entry<K, V> entry = entries.get(i);
			if(entry.key.equals(key)) {
				entry.setValue(value);
				return;
			}
		}
		
		entries.add(new Entry<>(key, value));
	}
	
	/**
	 * Returns the value of an entry with the given key.
	 * Searches the collection for the entry with selected key.
	 * If it is found, returns the value of that entry, otherwise
	 * returns {@code null}.
	 * 
	 * @param key key of the entry
	 * @return value of an entry with the given key if found, {@code null} otherwise
	 */
	public V get(Object key) {
		Objects.requireNonNull(key, "Key should not be a null value.");
		
		ElementsGetter<Entry<K, V>> getter = entries.createElementsGetter();
		
		while(getter.hasNextElement()) {
			Entry<K, V> entry = getter.getNextElement();
			
			if(entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		entries.forEach(value -> sb.append("[" + value + "] "));
		return sb.toString();
	}
	
	/**
	 * Representation of a single dictionary entry with
	 * key-value pair.
	 *
	 * @param <K> key of the entry
	 * @param <V> value of the entry
	 */
	private static class Entry<K, V> {
		
		/** Key of this entry. */
		private K key;
		
		/** Value of this entry. */
		private V value;
		
		/**
		 * Constructs a new entry with the given key and value.
		 * 
		 * @param key key of the new entry
		 * @param value value of the new entry
		 */
		private Entry(K key, V value) {
			this.key = Objects.requireNonNull(key, "Key should not be null.");
			this.value = value;
		}
		
		/**
		 * Returns the key for this entry.
		 * @return key of this entry
		 */
		public K getKey() {
			return key;
		}
		
		/**
		 * Returns the value for this entry.
		 * @return value of this entry
		 */
		public V getValue() {
			return value;
		}
		
		/**
		 * Overrides the current {@code value} with the given one.
		 * @param value new value
		 */
		public void setValue(V value) {
			this.value = value;
		}
		@Override
		public int hashCode() {
			return Objects.hash(key);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Entry))
				return false;
			Entry<K,V> other = (Entry<K,V>) obj;
			return Objects.equals(key, other.key);
		}

		@Override
		public String toString() {
			return key + " -> " + value;
		}
	}
}
