package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SimpleHashtableTest {
	private SimpleHashtable<String, Integer> hashtable;
	
	@BeforeEach
	void fillWithData() {
		hashtable = new SimpleHashtable<String, Integer>(4);
		hashtable.put("Ivan", 5);
		hashtable.put("Marko", 7);
	}

	@Test
	void putTest() {
		hashtable.put("Luka", 1);
		assertEquals(3, hashtable.size());
	}
	
	@Test
	void putAlreadyExistsTest() {
		hashtable.put("Ivan", 10);
		
		assertEquals(10, hashtable.get("Ivan"));
		assertEquals(2, hashtable.size());
	}
	
	@Test
	void putNullTest() {
		assertThrows(NullPointerException.class, () -> hashtable.put(null, 5));
	}
	
	@Test
	void clearTest() {
		hashtable.clear();
		
		assertEquals(0, hashtable.size());
		assertEquals(null, hashtable.get("Ivan"));
	}
	
	@Test
	void containsKeyTest() {
		assertTrue(hashtable.containsKey("Marko"));
	}
	
	@Test
	void containsNullKeyTest() {
		assertFalse(hashtable.containsKey(null));
	}
	
	@Test
	void containsValueTest() {
		assertTrue(hashtable.containsValue(5));
		assertTrue(hashtable.containsValue(7));
		assertFalse(hashtable.containsValue(300));
	}
	
	@Test
	void isEmptyTest() {
		assertFalse(hashtable.isEmpty());
		hashtable.clear();
		assertTrue(hashtable.isEmpty());
	}
	
	@Test
	void iteratorTest() {
		var iterator = hashtable.iterator();
		
		assertTrue(iterator.hasNext());
		Objects.requireNonNull(iterator.next());

		assertTrue(iterator.hasNext());
		Objects.requireNonNull(iterator.next());
		
		assertFalse(iterator.hasNext());
		assertThrows(NoSuchElementException.class, () -> iterator.next());
	}
	
	@Test
	void iteratorRemoveTest() {
		var iterator = hashtable.iterator();
		
		assertEquals(2, hashtable.size());
		iterator.next();
		iterator.remove();
		assertEquals(1, hashtable.size());
		iterator.next();
		iterator.remove();
		assertEquals(0, hashtable.size());
		
		assertThrows(IllegalStateException.class, () -> iterator.remove());
	}
	
	@Test
	void iteratorInvalidRemoveTest() {
		var iterator = hashtable.iterator();
		
		assertThrows(IllegalStateException.class, () -> iterator.remove()); // can't remove before calling next()
		
		iterator.next();
		iterator.remove();
		assertThrows(IllegalStateException.class, () -> iterator.remove()); // can't remove twice in a row
	}
	
	@Test
	void removeTest() {
		int size = hashtable.size();
		hashtable.remove("Ivan");
		assertEquals(size - 1, hashtable.size());
		hashtable.remove("Marko");
		assertEquals(size - 2, hashtable.size());
	}
	
	@Test
	void removeNullTest() {
		int size = hashtable.size();
		hashtable.remove(null);
		assertEquals(size, hashtable.size());
	}
}
