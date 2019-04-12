package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElementsGetterTest {
	private ArrayIndexedCollection collection = new ArrayIndexedCollection();
	private ElementsGetter getter;
	
	@BeforeEach
	void initializeCollection() {
		collection.add(1);
		collection.add(2);
		
		getter = collection.createElementsGetter();
	}

	@Test
	void getNextTest() {
		assertEquals(1, getter.getNextElement());
		assertEquals(2, getter.getNextElement());
	}
	
	@Test
	void getNextOutOfBoundsTest() {
		getter.getNextElement();
		getter.getNextElement();
		assertThrows(NoSuchElementException.class, () -> getter.getNextElement());
	}
	
	@Test
	void getNextFromModifiedCollectionTest() {
		collection.remove(0);
		assertThrows(ConcurrentModificationException.class, () -> getter.getNextElement());
	}
	
	@Test
	void hasNextTest() {
		assertTrue(getter.hasNextElement());
		getter.getNextElement();
		assertTrue(getter.hasNextElement());
		getter.getNextElement();
		assertFalse(getter.hasNextElement());
	}
}
