package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ArrayIndexedCollectionTest {
	private static ArrayIndexedCollection col = new ArrayIndexedCollection();
	
	@BeforeAll
	static void initialialize() {
		for(int i = 0; i < 5; i++) {
			col.add(i);
		}
	}

	@Test
	void defaultConstructorTest() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		assertEquals(16, col.getCapacity());
		assertEquals(0, col.size());
	}
	
	@Test
	void capacityConstructorTest() {
		ArrayIndexedCollection col = new ArrayIndexedCollection(1);
		assertEquals(1, col.getCapacity());
		
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
	}
	
	@Test
	void otherCollectionConstructorTest() {
		ArrayIndexedCollection other = new ArrayIndexedCollection();
		other.add(5);
		
		ArrayIndexedCollection col = new ArrayIndexedCollection(other);
		assertEquals(5, col.get(0));
		
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
	
	@Test
	void otherCollectionAndCapacityConstructorTest() {
		assertEquals(34, new ArrayIndexedCollection(new ArrayIndexedCollection(), 34).getCapacity());
		assertThrows(NullPointerException.class,  () -> new ArrayIndexedCollection(null, 10));
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(new ArrayIndexedCollection(), 0));
	}
	
	@Test
	void addTest() {
		assertThrows(NullPointerException.class, () -> col.add(null));
	}
	
	@Test
	void clearTest() {
		ArrayIndexedCollection col2 = new ArrayIndexedCollection();
		assertEquals(0, col2.size());
		col2.add(1);
		col2.add(2);
		assertEquals(2, col2.size());
		col2.clear();
		assertEquals(0, col2.size());
	}
	
	@Test
	void containsTest() {
		assertTrue(col.contains(1));
		assertFalse(col.contains(1000));
	}
	
	@Test
	void containsNullTest() {
		assertFalse(col.contains(null));
	}
	
	@Test
	void forEachTest() {
		ArrayIndexedCollection integers = new ArrayIndexedCollection();
		
		class AddAllIntegersProcessor extends Processor {
			@Override
			public void process(Object o) {
				if(o instanceof Integer) {
					integers.add(o);
				}
			}
		}
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection();
		col2.add(1);
		col2.add("Tekst");
		col2.add(2);
		col2.forEach(new AddAllIntegersProcessor());
		
		assertEquals(2, integers.size());
	}
	
	@Test
	void toArrayTest() {
		ArrayIndexedCollection col2 = new ArrayIndexedCollection();
		col2.add(1);
		assertEquals(16, col2.getCapacity());
		
		Object[] nonNullObjects = col2.toArray();
		assertEquals(1, nonNullObjects.length);
	}
	
	@Test
	void getTest() {
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(-1));
		assertThrows(IndexOutOfBoundsException.class, () -> col.get(1000));
	}
	
	@Test
	void insertTest() {
		assertThrows(NullPointerException.class, () -> col.insert(null, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> col.insert(1000, -1));
	}
	
	@Test
	void indexOfTest() {
		assertEquals(-1, col.indexOf(null));
		assertEquals(4, col.indexOf(4));
		assertEquals(-1, col.indexOf(1000));
	}
	
	@Test
	void removeTest() {
		assertThrows(IndexOutOfBoundsException.class, () -> col.remove(-1));
		
		ArrayIndexedCollection temp = new ArrayIndexedCollection();
		assertThrows(IndexOutOfBoundsException.class, () -> temp.remove(0));
	}
	
	@Test
	void removeObjectTest() {
		ArrayIndexedCollection test = new ArrayIndexedCollection();
		test.add(1);
		test.add(2);
		
		assertTrue(true);
		
		assertTrue(test.remove((Object)1));
		assertFalse(test.remove((Object)7));
		
		assertEquals(2, test.get(0));
	}
}
