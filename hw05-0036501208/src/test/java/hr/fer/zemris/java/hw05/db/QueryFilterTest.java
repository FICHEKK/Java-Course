package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

class QueryFilterTest {

	@Test
	void nullQueryFilterTest() {
		assertThrows(NullPointerException.class, () -> new QueryFilter(null));
	}
	
	@Test
	void acceptsTest() {
		StudentRecord r1 = new StudentRecord("0001", "Last", "First", 5);
		StudentRecord r2 = new StudentRecord("0002", "Last", "First", 5);
		StudentRecord r3 = new StudentRecord("0003", "Last", "F", 5);
		StudentRecord r4 = new StudentRecord("0004", "L", "First", 5);
		
		var expressions = new LinkedList<ConditionalExpression>();
		expressions.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "First", ComparisonOperators.EQUALS));
		expressions.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "Last", ComparisonOperators.EQUALS));
		
		QueryFilter filter = new QueryFilter(expressions);
		assertTrue(filter.accepts(r1));
		assertTrue(filter.accepts(r2));
		assertFalse(filter.accepts(r3));
		assertFalse(filter.accepts(r4));
	}
}
