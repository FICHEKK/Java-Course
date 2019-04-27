package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectMultistackTest {
	
	private ObjectMultistack stack;
	
	@BeforeEach
	void init() {
		stack = new ObjectMultistack();
	}

	@Test
	void pushTest() {
		assertTrue(stack.isEmpty("A"));
		stack.push("A", new ValueWrapper(1));
		assertFalse(stack.isEmpty("A"));
		
		assertTrue(stack.isEmpty("B"));
		stack.push("B", new ValueWrapper(1));
		assertFalse(stack.isEmpty("B"));
	}
	
	@Test
	void peekTest() {
		stack.push("A", new ValueWrapper(2));
		assertEquals(2, stack.peek("A").getValue());
		
		stack.push("A", new ValueWrapper(5));
		assertEquals(5, stack.peek("A").getValue());
		
		stack.push("A", new ValueWrapper(11));
		assertEquals(11, stack.peek("A").getValue());
	}
	
	@Test
	void popTest() {
		stack.push("A", new ValueWrapper(1));
		
		assertFalse(stack.isEmpty("A"));
		assertEquals(1, stack.pop("A").getValue());
		assertTrue(stack.isEmpty("A"));
	}
	
	@Test
	void peekEmptyTest() {
		assertThrows(IllegalStateException.class, () -> stack.peek("A"));
	}
	@Test
	
	void popEmptyTest() {
		assertThrows(IllegalStateException.class, () -> stack.pop("A"));
	}
	
	@Test
	void isEmptyTest() {
		assertTrue(stack.isEmpty("A"));
		
		stack.push("A", new ValueWrapper(1));
		assertFalse(stack.isEmpty("A"));
		
		stack.pop("A");
		assertTrue(stack.isEmpty("A"));
	}
}
