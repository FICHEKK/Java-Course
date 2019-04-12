package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComparisonOperatorsTest {
	
	private static final IComparisonOperator less = ComparisonOperators.LESS;
	private static final IComparisonOperator lessOrEquals = ComparisonOperators.LESS_OR_EQUALS;
	private static final IComparisonOperator greater = ComparisonOperators.GREATER;
	private static final IComparisonOperator greaterOrEquals = ComparisonOperators.GREATER_OR_EQUALS;
	private static final IComparisonOperator equals = ComparisonOperators.EQUALS;
	private static final IComparisonOperator notEquals = ComparisonOperators.NOT_EQUALS;
	private static final IComparisonOperator like = ComparisonOperators.LIKE;
	
	@Test
	void likeOperatorTest() {
		assertTrue(like.satisfied("Štefica", "Štefica"));
		assertTrue(like.satisfied("Štefica", "Š*"));
		assertTrue(like.satisfied("Štefica", "*a"));
		assertTrue(like.satisfied("Štefica", "*fica"));
		assertTrue(like.satisfied("Štefica", "Šte*fica"));
		assertTrue(like.satisfied("Štefica", "Š*a"));
		assertTrue(like.satisfied("Štefica", "*Štefica"));
		assertTrue(like.satisfied("Štefica", "Štefica*"));
		assertTrue(like.satisfied("Štefica", "*tefica"));
		assertTrue(like.satisfied("Štefica", "*"));
		
		assertFalse(like.satisfied("AAA", "AA*AA"));
		assertFalse(like.satisfied("AAA", "AAAA"));
		assertFalse(like.satisfied("AAA", "*AAAA"));
		assertFalse(like.satisfied("AAA", "AAAA*"));
		assertFalse(like.satisfied("AAA", "B*"));
		assertFalse(like.satisfied("AAA", "*B"));
		assertFalse(like.satisfied("AAA", "A*B"));
	}

	@Test
	void lessOperatorTest() {
		assertTrue(less.satisfied("A", "Z"));
		assertTrue(less.satisfied("BRZ", "BRZI"));
		
		assertFalse(less.satisfied("EEE", "EEE"));
		assertFalse(less.satisfied("EEEZ", "EEE"));
	}
	
	@Test
	void lessOrEqualsOperatorTest() {
		assertTrue(lessOrEquals.satisfied("A", "Z"));
		assertTrue(lessOrEquals.satisfied("A", "A"));
		
		assertFalse(lessOrEquals.satisfied("D", "C"));
		assertFalse(lessOrEquals.satisfied("EEEZ", "EEE"));
	}
	
	@Test
	void greaterOperatorTest() {
		assertTrue(greater.satisfied("Z", "A"));
		assertTrue(greater.satisfied("ZA", "Z"));
		
		assertFalse(greater.satisfied("A", "A"));
		assertFalse(greater.satisfied("A", "AA"));
	}
	
	@Test
	void greaterOrEqualsOperatorTest() {
		assertTrue(greaterOrEquals.satisfied("Z", "A"));
		assertTrue(greaterOrEquals.satisfied("Z", "Z"));
		
		assertFalse(greaterOrEquals.satisfied("C", "D"));
		assertFalse(greaterOrEquals.satisfied("C", "CC"));
	}
	
	@Test
	void equalsOperatorTest() {
		assertTrue(equals.satisfied("A", "A"));
		assertTrue(equals.satisfied("text", "text"));
		
		assertFalse(equals.satisfied("text", "Text"));
		assertFalse(equals.satisfied("a", "A"));
	}
	
	@Test
	void notEqualsOperatorTest() {
		assertTrue(notEquals.satisfied("A", "a"));
		assertTrue(notEquals.satisfied("abc", "def"));
		
		assertFalse(notEquals.satisfied("text", "text"));
		assertFalse(notEquals.satisfied("A", "A"));
	}
}
