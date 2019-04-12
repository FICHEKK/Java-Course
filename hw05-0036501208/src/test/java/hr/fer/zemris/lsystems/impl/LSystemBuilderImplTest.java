package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;

class LSystemBuilderImplTest {

	@Test
	void generateTest() {
		LSystem system = new LSystemBuilderImpl()
				.registerProduction('F', "F+F--F+F")
				.setAxiom("F")
				.build();
		
		assertEquals("F", system.generate(0));
		assertEquals("F+F--F+F", system.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", system.generate(2));
	}
}
