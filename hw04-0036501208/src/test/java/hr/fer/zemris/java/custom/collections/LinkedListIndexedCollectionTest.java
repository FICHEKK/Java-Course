package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedListIndexedCollectionTest {
	
	private LinkedListIndexedCollection<Object> list = new LinkedListIndexedCollection<>();
	
	@BeforeEach
	void initialize() {
		list.add(10);
		list.add("text");
		list.add(1.16);
		list.add(77);
	}

	@Test
	void defaultConstructorTest() {
		var testList = new LinkedListIndexedCollection<>();
		assertEquals(0, testList.size());
	}
	
	@Test
	void otherCollectionConstructorTest() {
		var testList = new LinkedListIndexedCollection<>(list);
		assertEquals(10, testList.get(0));
		assertEquals("text", testList.get(1));
	}
	
	@Test
	void addTest() {
		list.add(1000);
		assertEquals(1000, list.get(4));
	}
	
	@Test
	void addNullTest() {
		assertThrows(NullPointerException.class, () -> list.add(null));
	}
	
	@Test
	void forEachTest() {
		var integers = new LinkedListIndexedCollection<Integer>();
		
		class AddAllIntegersProcessor implements Processor<Object> {
			@Override
			public void process(Object o) {
				if(o instanceof Integer) {
					integers.add((Integer)o);
				}
			}
		}
		
		var col2 = new LinkedListIndexedCollection<>();
		col2.add(1);
		col2.add("Tekst");
		col2.add(2);
		col2.forEach(new AddAllIntegersProcessor());
		
		assertEquals(2, integers.size());
	}
	
	@Test
	void getTest() {
		assertEquals(77, list.get(3));
	}
	
	@Test
	void clearTest() {
		list.clear();
		assertEquals(0, list.size());
	}
	
	@Test
	void toArrayTest() {
		Object[] array = list.toArray();
		assertEquals(4, array.length);
		assertEquals("text", array[1]);
	}
	
	@Test
	void insertTest() {
		list.insert(5000, 3);
		assertEquals(5000, list.get(3));
		list.insert(100, 0);
		assertEquals(100, list.get(0));
	}
	
	@Test
	void containsTest() {
		assertTrue(list.contains("text"));
		assertFalse(list.contains("text123"));
	}
	
	@Test
	void containsNullTest() {
		assertFalse(list.contains(null));
	}
	
	@Test
	void insertNullTest() {
		assertThrows(NullPointerException.class, () -> list.insert(null, 0));
	}
	
	@Test
	void removeWithIndexTest() {
		var testList = new LinkedListIndexedCollection<>(list);
		testList.remove(0);
		assertEquals("text", testList.get(0));
	}
	
	@Test
	void removeWithObjectTest() {
		var testList = new LinkedListIndexedCollection<>(list);
		assertTrue(testList.remove("text"));
		assertFalse(testList.remove("asdf"));
		assertFalse(testList.remove(null));
	}
	
	@Test
	void removeOutOfBoundTest() {
		assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
	}
	
	@Test
	void indexOfTest() {
		assertEquals(0, list.indexOf(10));
	}
	
	@Test
	void indexOfNullTest() {
		assertEquals(-1, list.indexOf(null));
	}
}
