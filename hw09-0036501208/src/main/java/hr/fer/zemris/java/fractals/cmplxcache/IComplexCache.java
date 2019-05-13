package hr.fer.zemris.java.fractals.cmplxcache;

import hr.fer.zemris.math.Complex;

@SuppressWarnings("javadoc")
public interface IComplexCache {
	int size();
	Complex get();
	Complex get(double real, double imaginary);
	Complex get(Complex templete);
	void release(Complex c);
}
