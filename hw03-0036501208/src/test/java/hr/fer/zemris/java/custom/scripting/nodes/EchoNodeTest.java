package hr.fer.zemris.java.custom.scripting.nodes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

class EchoNodeTest {

	@Test
	void equalsTest() {
		Element[] elements1 = {new ElementVariable("="),
							   new ElementString("abc"),
							   new ElementConstantDouble(1.44),
							   new ElementOperator("+"),
							   new ElementConstantInteger(10),
							   new ElementFunction("exp"),
		};
		
		Element[] elements2 = {new ElementVariable("="),
							   new ElementString("abc"),
							   new ElementConstantDouble(1.44),
							   new ElementOperator("+"),
							   new ElementConstantInteger(10),
							   new ElementFunction("exp"),
		};
		
		EchoNode en1 = new EchoNode(elements1);
		EchoNode en2 = new EchoNode(elements2);
		
		assertTrue(en1.equals(en2));
	}
	
	@Test
	void notEqualTest() {
		Element[] elements1 = {new ElementVariable("="),
							   new ElementString("abc"),
							   new ElementConstantDouble(1.44),
							   new ElementOperator("+"),
							   new ElementConstantInteger(10),
							   new ElementFunction("exp"),
		};
		
		Element[] elements2 = {new ElementVariable("="),
							   new ElementString("abc"),
							   new ElementConstantDouble(1.44),
							   new ElementOperator("-"), 		// Changed to minus.
							   new ElementConstantInteger(10),
							   new ElementFunction("exp"),
		};
		
		Element[] elements3 = {new ElementVariable("="),
							   new ElementString("abc"),
							   new ElementOperator("-"),		// Swapped to position 2 from 3.
							   new ElementConstantDouble(1.44),
							   new ElementConstantInteger(10),
							   new ElementFunction("exp"),
		};
		
		EchoNode en1 = new EchoNode(elements1);
		EchoNode en2 = new EchoNode(elements2);
		EchoNode en3 = new EchoNode(elements3);
		
		assertFalse(en1.equals(en2));
		assertFalse(en1.equals(en3));
		assertFalse(en2.equals(en3));
	}
	
	@Test
	void notEqualBecauseDifferentLengthTest() {
		Element[] elements1 = {new ElementVariable("=")};
		Element[] elements2 = {new ElementVariable("="),
				  			   new ElementString("abc")};
		
		EchoNode en1 = new EchoNode(elements1);
		EchoNode en2 = new EchoNode(elements2);
		
		assertFalse(en1.equals(en2));
	}
}
