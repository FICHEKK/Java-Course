package hr.fer.zemris.java.hw02;

import java.util.Objects;

/**
 * Class that offers functionality for working with
 * complex numbers.
 */
public class ComplexNumber {
	/** Real part of this imaginary number. */
	private final double real;
	
	/** Imaginary part of this imaginary number. */
	private final double imaginary;
	
	/**
	 * Constructs a new complex number for given real
	 * and imaginary part.
	 * 
	 * @param real Real part of complex the number.
	 * @param imaginary Imaginary part of complex the number.
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Constructs a new complex number from given real part.
	 * 
	 * @param real real part
	 * @return new complex number with value of {@code real + 0*i}
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Constructs a new complex number from given imaginary part.
	 * 
	 * @param imaginary imaginary part
	 * @return new complex number with value of {@code 0 + imaginary*i}
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	/**
	 * Constructs a new complex number from given magnitude and angle.
	 * 
	 * @param magnitude the magnitude
	 * @param angle the angle
	 * @return new complex number from with given magnitude and angle
	 * @throws IllegalArgumentException if magnitude is negative
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		if(magnitude < 0) {
			throw new IllegalArgumentException("Magnitude should be a non-negative number.");
		}
		double real = magnitude * Math.cos(angle);
		double im = magnitude * Math.sin(angle);
		return new ComplexNumber(real, im);
	}
	
	/**
	 * Converts given {@code String} value to {@code ComplexNumber} if possible.
	 * 
	 * @param string string to be parsed
	 * @return {@code ComplexNumber} represented by the given string
	 * @throws NullPointerExceptionif the string is null
	 * @throws NumberFormatException if the string does not contain a parsable ComplexNumber
	 * @throws NullPointerException if given string was {@code null}
	 */
	public static ComplexNumber parse(String string) {
		Objects.requireNonNull(string, "Given string must not be null!");
		
		// Replace white space with no space for easier parsing.
		string = string.replaceAll("\\s", "");
		
		 // Handle special cases first.
		if(string.isEmpty()) {
			throw new NumberFormatException("Given string must not be empty!");
		}
		
		if(string.equals("i")) {
			return new ComplexNumber(0, 1);
		}
		
		if(string.endsWith("-i") || string.endsWith("+i")) {
			// If given string was "-3+i", this would return "-3", or the real part.
			String realPart = string.substring(0, string.length() - 2);
			
			// If there is no real part, then it should be 0, otherwise
			// simply parse that part.
			double real = realPart.isEmpty() ? 0 : Double.parseDouble(realPart);
			
			// If plus is before the "i", imaginary part is 1, if minus then -1.
			double imaginary = string.charAt(string.length() - 1) == '+' ? 1 : -1;
	
			return new ComplexNumber(real, imaginary);
		}
		
		// Parsing will be easier with an char array.
		char[] chars = string.toCharArray();
		char last = chars[chars.length - 1];
		
		if(last == 'i') {
			// We know a number is now imaginary, so there are two cases:
			// 1. it has real part: 2.13-3.14i
			// 2. it has no real part: -5.55i
			
			// Case 1: there should be a plus or minus between the real and imaginary.
			for(int i = 1; i < chars.length; i++) {
				 // Now from start until this sign should be the real part.
				if(chars[i] == '-' || chars[i] == '+') {
					String realSubstring = string.substring(0, i);
					String imSubstring = string.substring(i, string.length() - 1);
					
					// Use parseDouble functionality to parse real and imaginary part.
					double real = Double.parseDouble(realSubstring);
					double imaginary = Double.parseDouble(imSubstring);

					return new ComplexNumber(real, imaginary);
				}
			}
			
			// Case 2: we only exclude the "i".
			String imaginarySubstring = string.substring(0, string.length() - 1);
			
			double real = 0;
			double imaginary = Double.parseDouble(imaginarySubstring);
			
			return new ComplexNumber(real, imaginary);
		} else if(Character.isDigit(last)) {
			double real = Double.parseDouble(string);
			return new ComplexNumber(real, 0);
		} else {
			String message = "String " + string + " could not be parsed. " +
							 "Last char should be 'i' or a digit!";
			throw new NumberFormatException(message);
		}
	}
	
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
	
	/**
	 * Calculates the magnitude of this complex number.
	 * 
	 * @return the magnitude of this complex number
	 */
	public double getMagnitude() {
		return Math.sqrt(real * real + imaginary * imaginary);
	}
	
	/**
	 * Returns the angle that line from this complex number to
	 * point (0, 0) makes in relation to positive x coordinate in radians.
	 * 
	 * @return angle in radians, from 0 to 2 pi.
	 */
	public double getAngle() {
		double radians = Math.atan2(imaginary, real);
		return radians >= 0 ? radians : radians + 2 * Math.PI;
	}
	
	/**
	 * Adds this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that will add to this number
	 * @return new complex number that is the result of addition
	 */
	public ComplexNumber add(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(real + c.getReal(), imaginary + c.getImaginary());
	}
	
	/**
	 * Subtracts this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that will subtract from this number
	 * @return new complex number that is the result of subtraction
	 */
	public ComplexNumber sub(ComplexNumber c) {
		Objects.requireNonNull(c);
		return new ComplexNumber(real - c.getReal(), imaginary - c.getImaginary());
	}
	
	/**
	 * Multiplies this complex number with the given one, and
	 * returns a new complex number as a result.
	 * 
	 * @param c complex number that multiplies this number
	 * @return new complex number that is the result of multiplication
	 */
	public ComplexNumber mul(ComplexNumber c) {
		Objects.requireNonNull(c);
		double real = this.real * c.getReal() - this.imaginary * c.getImaginary();
		double im = this.real * c.getImaginary() + this.imaginary * c.getReal();
		return new ComplexNumber(real, im);
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
	public ComplexNumber div(ComplexNumber c) {
		Objects.requireNonNull(c);
		
		if(c.getReal() == 0 && c.getImaginary() == 0) {
			throw new ArithmeticException("Dividing by zero!");
		}
		
		ComplexNumber numerator = this.mul(c.conjugate());
		double denominator = c.getReal() * c.getReal() + c.getImaginary() * c.getImaginary();
		
		double real = numerator.getReal() / denominator;
		double im = numerator.getImaginary() / denominator;
				
		return new ComplexNumber(real, im);
	}
	
	/**
	 * Returns n-th power of this complex number as a new complex number.
	 * 
	 * @param n the exponent
	 * @return n-th power of this complex number
	 * @throws IllegalArgumentException if {@code n} is negative
	 */
	public ComplexNumber power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("Power should be non-negative number!");
		}
		
		double magnitudeToPowerN = Math.pow(getMagnitude(), n);
		double angleTimesN = getAngle() * n;
		
		double real = magnitudeToPowerN * Math.cos(angleTimesN);
		double im = magnitudeToPowerN * Math.sin(angleTimesN);
		
		return new ComplexNumber(real, im);
	}
	
	/**
	 * Returns all the solutions of n-th root of this complex number
	 * as an array of complex numbers.
	 * 
	 * @param n the root
	 * @return array of complex numbers that are solutions to the n-th root
	 * @throws IllegalArgumentException if {@code n} is not positive
	 */
	public ComplexNumber[] root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("Root should be a positive number!");
		}
		
		double NthRootOfMagnitude = Math.pow(getMagnitude(), 1.0 / n);
		double angle = getAngle();
		
		ComplexNumber[] roots = new ComplexNumber[n];
		
		for(int k = 0; k < n; k++) {
			double solutionAngle = (angle + 2 * k * Math.PI) / n;
			
			double real = NthRootOfMagnitude * Math.cos(solutionAngle);
			double im = NthRootOfMagnitude * Math.sin(solutionAngle);
			
			roots[k] = new ComplexNumber(real, im);
		}
		
		return roots;
	}

	/**
	 * @return conjugate of this complex number.
	 */
	public ComplexNumber conjugate() {
		return new ComplexNumber(real, -imaginary);
	}
	
	/**
	 * Transforms complex number into a string representation.
	 */
	@Override
	public String toString() {
		String sign = imaginary >= 0 ? "+" : "-";
		return real + sign + Math.abs(imaginary) + "i";
	}
}
