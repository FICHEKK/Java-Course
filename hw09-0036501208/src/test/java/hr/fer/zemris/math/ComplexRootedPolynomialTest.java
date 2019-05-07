package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

class ComplexRootedPolynomialTest {

	@Test
	void toComplexPolynomialTest() {
		Complex[] roots = {Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG};
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(Complex.ONE, roots);
		
		ComplexPolynomial cp = crp.toComplexPolynom();
		
		System.out.println(crp);
		System.out.println(cp);
		System.out.println(cp.derive());
	}

}
