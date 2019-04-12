package hr.fer.zemris.java.hw02.demo;

import hr.fer.zemris.java.hw02.ComplexNumber;

public class Main {
	
	public static void main(String[] args) {
		// ----------------------------------------------
		// Some quick tests, please ignore!
		// ----------------------------------------------
		ComplexNumber cn = new ComplexNumber(1, 0.01);
		System.out.println(cn.getAngle());
		System.out.println(cn);
		
		System.out.println(new ComplexNumber(10, 20));
		
		ComplexNumber c1 = new ComplexNumber(1.2, -0.4);
		ComplexNumber c2 = new ComplexNumber(-5, 2);
		System.out.println(c1.add(c2));
		System.out.println(c1.sub(c2));
		System.out.println(c1.mul(c2));
		System.out.println(c1.div(c2));
		
		System.out.println(c1.power(3));
		
		ComplexNumber[] solutions = new ComplexNumber(1, 1).root(5);
		for(ComplexNumber c : solutions) {
			System.out.println(c);
		}
	}
}
