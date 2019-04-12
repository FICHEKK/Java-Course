package hr.fer.zemris.java.custom.scripting.nodes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TextNodeTest {

	@Test
	void emptyEqualsTest() {
		TextNode tn1 = new TextNode("     ");
		TextNode tn2 = new TextNode("");
		assertTrue(tn1.equals(tn2));
	}
	
	@Test
	void nullEqualsTest() {
		TextNode tn1 = new TextNode("abc");
		TextNode tn2 = null;
		assertFalse(tn1.equals(tn2));
	}
	
	@Test
	void usualEqualsTest() {
		TextNode tn1 = new TextNode("abc");
		TextNode tn2 = new TextNode("abc");
		assertTrue(tn1.equals(tn2));
	}
	
	@Test
	void usualWithSpacesEqualsTest() {
		TextNode tn1 = new TextNode(" a b c ");
		TextNode tn2 = new TextNode("abc");
		assertTrue(tn1.equals(tn2));
	}
	
	@Test
	void newLineNotEqualsTest() {
		TextNode tn1 = new TextNode("abc\n");
		TextNode tn2 = new TextNode("abc");
		assertFalse(tn1.equals(tn2));
	}
	
	@Test
	void toStringTest() {
		TextNode tn1 = new TextNode("{John\\");
		String s = tn1.toString();
		assertTrue(s.equals("\\{John\\\\"));
	}
}
