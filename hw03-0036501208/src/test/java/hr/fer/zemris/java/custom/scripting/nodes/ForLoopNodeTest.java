package hr.fer.zemris.java.custom.scripting.nodes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

class ForLoopNodeTest {

	@Test
	void equalsTest() {
		ForLoopNode fln1 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   new ElementConstantInteger(2));
		
		ForLoopNode fln2 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   new ElementConstantInteger(2));
		
		assertTrue(fln1.equals(fln2));
	}
	
	@Test
	void equalsWithStepNullTest() {
		ForLoopNode fln1 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   null);
		
		ForLoopNode fln2 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   null);
		
		assertTrue(fln1.equals(fln2));
	}
	
	@Test
	void notEqualBecauseStepTest() {
		ForLoopNode fln1 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   new ElementConstantInteger(2));
		
		ForLoopNode fln2 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   null);
		
		assertFalse(fln1.equals(fln2));
	}
	
	@Test
	void notEqualBecauseChildrenLengthDiffersTest() {
		ForLoopNode fln1 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   null);
		fln1.addChildNode(new TextNode("a child"));
		
		ForLoopNode fln2 = new ForLoopNode(new ElementVariable("i"), 
										   new ElementConstantInteger(1),
										   new ElementConstantInteger(10),
										   null);
		
		assertFalse(fln1.equals(fln2));
	}
	
	@Test
	void nullArgumentTest() {
		ElementVariable var = new ElementVariable("i");
		ElementConstantInteger start = new ElementConstantInteger(1);
		ElementConstantInteger end = new ElementConstantInteger(10);
		ElementConstantInteger step = new ElementConstantInteger(2);
		
		assertThrows(NullPointerException.class, () -> new ForLoopNode(null, start, end, step));
		assertThrows(NullPointerException.class, () -> new ForLoopNode(var, null, end, step));
		assertThrows(NullPointerException.class, () -> new ForLoopNode(var, start, null, step));
	}
}
