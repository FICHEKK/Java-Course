package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComplexPolynomialTest {

	@Test
	void orderTest() {
		ComplexPolynomial cp1 = new ComplexPolynomial(Complex.ONE, null, Complex.ONE);
		ComplexPolynomial cp2 = new ComplexPolynomial(null, null);
		
		assertEquals(2, cp1.order());
		assertEquals(0, cp2.order());
	}
}
