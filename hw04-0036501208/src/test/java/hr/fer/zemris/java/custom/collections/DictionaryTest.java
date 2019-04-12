package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DictionaryTest {

	@Test
	void isEmptyTest() {
		var dict2 = new Dictionary<String, Integer>(5);
		assertTrue(dict2.isEmpty());
		dict2.put("A", 1);
		assertFalse(dict2.isEmpty());
		dict2.clear();
		assertTrue(dict2.isEmpty());
	}
	
	@Test
	void clearTest() {
		var dict = new Dictionary<String, Integer>(5);
		
		dict.put("A", 1);
		dict.put("B", 2);
		dict.put("C", 3);
		dict.put("D", 4);
		dict.put("E", 5);
		
		assertFalse(dict.isEmpty());
		assertEquals(5, dict.size());
		
		dict.clear();
		
		assertTrue(dict.isEmpty());
		assertEquals(0, dict.size());
	}
	
	@Test
	void sizeTest() {
		var dict = new Dictionary<String, Integer>(5);
		
		assertEquals(0, dict.size());
		
		dict.put("A", 1);
		dict.put("B", 2);
		dict.put("C", 3);
		
		assertEquals(3, dict.size());
		
		dict.put("D", 4);
		dict.put("E", 5);

		assertEquals(5, dict.size());
	}
	
	
	@Test
	void putTest() {
		var dict = new Dictionary<String, Integer>(5);
		
		dict.put("A", 1);
		dict.put("B", 2);
		dict.put("C", 3);

		assertEquals(1, dict.get("A"));
		assertEquals(2, dict.get("B"));
		assertEquals(3, dict.get("C"));
		
		dict.put("A", 5);
		
		assertEquals(5, dict.get("A"));
	}	
	
	@Test
	void putNullKeyTest() {
		var dict = new Dictionary<String, Integer>(5);
		assertThrows(NullPointerException.class, () -> dict.put(null, 5));
	}

	
	@Test
	void getTest() {
		var dict = new Dictionary<String, Integer>(5);
		
		assertEquals(null, dict.get("sir"));
		
		dict.put("A", 1);
		dict.put("B", 2);
		dict.put("C", 3);
		
		assertEquals(1, dict.get("A"));
		assertEquals(2, dict.get("B"));
		assertEquals(3, dict.get("C"));
	}
	
	@Test
	void getNullTest() {
		var dict = new Dictionary<String, Integer>(5);
		assertThrows(NullPointerException.class, () -> dict.get(null));
	}
}
