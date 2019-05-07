package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Class that offers functionality for working with
 * complex numbers.
 */
public class Complex {
	
	/** Represents the complex number 0 + 0i. */
	public static final Complex ZERO = new Complex(0,0);
	
	/** Represents the complex number 1 + 0i. */
	public static final Complex ONE = new Complex(1,0);
	
	/** Represents the complex number -1 + 0i. */
	public static final Complex ONE_NEG = new Complex(-1,0);
	
	/** Represents the complex number 0 + 1i. */
	public static final Complex IM = new Complex(0,1);
	
	/** Represents the complex number 0 - 1i. */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/** Real part of this imaginary number. */
	private final double real;
	
	/** Imaginary part of this imaginary number. */
	private final double imaginary;
	
	/**
	 * Constructs a complex number 0 + 0i.
	 */
	public Complex() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new complex number for given real
	 * and imaginary part.
	 * 
	 * @param real Real part of complex the number.
	 * @param imaginary Imaginary part of complex the number.
	 */
	public Complex(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Calculates the module of this complex number.
	 * 
	 * @return the module of this complex number
	 */
	public double module() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Adds this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that will add to this number
	 * @return new complex number that is the result of addition
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(real + c.real, imaginary + c.imaginary);
	}
	
	/**
	 * Subtracts this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that will subtract from this number
	 * @return new complex number that is the result of subtraction
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c);
		return new Complex(real - c.real, imaginary - c.imaginary);
	}
	
	/**
	 * Multiplies this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that multiplies this number
	 * @return new complex number that is the result of multiplication
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c);
		double re = real * c.real - imaginary * c.imaginary;
		double im = real * c.imaginary + imaginary * c.real;
		return new Complex(re, im);
	}
	
	/**
	 * Divides this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that divides this number
	 * @return new complex number that is the result of division
	 * @throws NullPointerException if {@code null} was given as an argument
	 * @throws ArithmeticException if dividing by zero - given complex number was 0+0i
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c);
		
		if(c.real == 0 && c.imaginary == 0) {
			throw new ArithmeticException("Dividing by zero!");
		}
		
		Complex numerator = this.multiply(c.conjugate());
		double denominator = c.real * c.real + c.imaginary * c.imaginary;
		
		double re = numerator.real / denominator;
		double im = numerator.imaginary / denominator;
				
		return new Complex(re, im);
	}
	
	/**
	 * Returns n-th power of this complex number as a new complex number.
	 * 
	 * @param n the exponent
	 * @return n-th power of this complex number
	 * @throws IllegalArgumentException if {@code n} is negative
	 */
	public Complex power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("Power should be non-negative number!");
		}
		
		double magnitudeToPowerN = Math.pow(module(), n);
		double angleTimesN = getAngle() * n;
		
		double real = magnitudeToPowerN * Math.cos(angleTimesN);
		double im = magnitudeToPowerN * Math.sin(angleTimesN);
		
		return new Complex(real, im);
	}
	
	/**
	 * Returns all the solutions of n-th root of this complex number
	 * as an array of complex numbers.
	 * 
	 * @param n the root
	 * @return array of complex numbers that are solutions to the n-th root
	 * @throws IllegalArgumentException if {@code n} is not positive
	 */
	public List<Complex> root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("Root should be a positive number!");
		}
		
		double NthRootOfMagnitude = Math.pow(module(), 1.0 / n);
		double angle = getAngle();
		
		List<Complex> roots = new LinkedList<>();
		
		for(int k = 0; k < n; k++) {
			double solutionAngle = (angle + 2 * k * Math.PI) / n;
			
			double re = NthRootOfMagnitude * Math.cos(solutionAngle);
			double im = NthRootOfMagnitude * Math.sin(solutionAngle);
			
			roots.add(new Complex(re, im));
		}
		
		return roots;
	}
	
	/**
	 * Negates this complex number and returns the result as a new
	 * instance of {@code Complex}. For example, negating <i>1 + 1i</i> will
	 * result in <i>-1 - 1i</i>.
	 *
	 * @return the result of negating this complex number
	 */
	public Complex negate() {
		return new Complex(-real, -imaginary);
	}
	
	//----------------------------------------------------------------------
	//					hashCode, equals, toString
	//----------------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return Objects.hash(imaginary, real);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;
		return Double.doubleToLongBits(imaginary) == Double.doubleToLongBits(other.imaginary)
				&& Double.doubleToLongBits(real) == Double.doubleToLongBits(other.real);
	}

	/**
	 * Transforms complex number into a string representation.
	 */
	@Override
	public String toString() {
		String sign = imaginary >= 0 ? "+" : "-";
		return real + sign + "i" + Math.abs(imaginary);
	}
	
	//----------------------------------------------------------------------
	//							HELPER METHODS
	//----------------------------------------------------------------------
	/**
	 * @return conjugate of this complex number.
	 */
	private Complex conjugate() {
		return new Complex(real, -imaginary);
	}
	
	/**
	 * Returns the angle that line from this complex number to
	 * point (0, 0) makes in relation to positive x coordinate in radians.
	 * 
	 * @return angle in radians, from 0 to 2 pi.
	 */
	private double getAngle() {
		double radians = Math.atan2(imaginary, real);
		return radians >= 0 ? radians : radians + 2 * Math.PI;
	}
	
	//----------------------------------------------------------------------
	//								GETTERS
	//----------------------------------------------------------------------
	
	/**
	 * @return Real part of this complex number.
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * @return Imaginary part of this complex number.
	 */
	public double getImaginary() {
		return imaginary;
	}
}

