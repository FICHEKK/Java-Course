package hr.fer.zemris.java.custom.scripting.nodes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DocumentNodeTest {

	@Test
	void equalsTest() {
		TextNode tn1 = new TextNode(" bla bla bla ");
		TextNode tn2 = new TextNode("blablabla");
		
		DocumentNode dn1 = new DocumentNode();
		dn1. addChildNode(tn1);
		
		DocumentNode dn2 = new DocumentNode();
		dn2.addChildNode(tn2);
		
		assertTrue(dn1.equals(dn2));
	}
	
	@Test
	void notEqualBecauseDifferentLengthTest() {
		TextNode tn1 = new TextNode(" bla bla bla ");
		TextNode tn2 = new TextNode("blablabla");
		TextNode tn3 = new TextNode("blablabla");
		
		DocumentNode dn1 = new DocumentNode();
		dn1.addChildNode(tn1);
		
		DocumentNode dn2 = new DocumentNode();
		dn2.addChildNode(tn2);
		dn2.addChildNode(tn3);
		
		assertFalse(dn1.equals(dn2));
	}
	
	@Test
	void notEqualBecauseDifferentChildrenTest() {
		TextNode tn1 = new TextNode(" bla bla bla ");
		TextNode tn2 = new TextNode("different text");
		
		DocumentNode dn1 = new DocumentNode();
		dn1.addChildNode(tn1);
		
		DocumentNode dn2 = new DocumentNode();
		dn2.addChildNode(tn2);
		
		assertFalse(dn1.equals(dn2));
	}
}
