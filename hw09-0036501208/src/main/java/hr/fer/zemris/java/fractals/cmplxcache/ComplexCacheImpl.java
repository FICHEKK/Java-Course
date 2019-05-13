package hr.fer.zemris.java.fractals.cmplxcache;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.math.Complex;


@SuppressWarnings("javadoc")
public class ComplexCacheImpl implements IComplexCache {
	
	private List<Complex> list = new ArrayList<>();
	
	public ComplexCacheImpl() {
	}

	@Override
	public Complex get() {
		if(list.isEmpty()) {
			return new Complex();
		}
		return list.remove(list.size()-1);
	}
	
	@Override
	public void release(Complex c) {
		list.add(c);
	}

	@Override
	public Complex get(double real, double imaginary) {
		Complex c = get();
		c.setReal(real);
		c.setImaginary(imaginary);
		return c;
	}

	@Override
	public Complex get(Complex templete) {
		Complex c = get();
		c.setReal(templete.getReal());
		c.setImaginary(templete.getImaginary());
		return c;
	}

	@Override
	public int size() {
		return list.size();
	}
}
