package hr.fer.zemris.java.hw01;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FactorialTest {

	@Test
	void simpleFactorialTest() {
		assertEquals(120, Factorial.factorial(5));
		assertEquals(1, Factorial.factorial(0));
		assertEquals(1, Factorial.factorial(1));
		assertEquals(2432902008176640000L, Factorial.factorial(20));
	}
	
	@Test
	void negativeFactorialTest() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(-5));
	}
	
	@Test
	void argumentTooBigTest() {
		assertThrows(IllegalArgumentException.class, () -> Factorial.factorial(21));
	}
}
