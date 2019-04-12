package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QueryParserTest {
	
	private static final QueryParser direct = new QueryParser("jmbag=\"0001\"");
	private static final QueryParser nonDirect = new QueryParser("firstName > \"A\" and lastName < \"B\"");

	@Test
	void isDirectQueryTest() {
		assertTrue(direct.isDirectQuery());
		assertFalse(nonDirect.isDirectQuery());
	}
	
	@Test
	void getQueriedJMBAGTest() {
		assertEquals("0001", direct.getQueriedJMBAG());
	}
	
	@Test
	void getQueriedJmbagFromNonDirectQueryTest() {
		assertThrows(IllegalStateException.class, () -> nonDirect.getQueriedJMBAG());
	}

	@Test
	void directQueryConditionalExpressionsSizeTest() {
		assertEquals(1, direct.getQuery().size());
	}
	
	@Test
	void nullQueryParserTest() {
		assertThrows(NullPointerException.class, () -> new QueryParser(null));
	}
	
	@Test
	void conditionalExpressionsSizeTest() {
		assertEquals(2, nonDirect.getQuery().size());
	}
}
