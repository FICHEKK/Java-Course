package hr.fer.zemris.math;

import java.util.Objects;

/**
 * Models complex polynomial functions of the following
 * format:
 * <br>
 * <br> f(z) = zn*z^n + ... + z1*z^1 + z0*z^0.
 * <p>
 * Also offers some useful operations on those polynomial
 * functions, such as deriving and multiplying.
 *
 * @author Filip Nemec
 */
public class ComplexPolynomial {
	
	/** The factors in this polynomial. */
	private Complex[] factors;
	
	/**
	 * Constructs a new complex polynomial with the given factors.
	 * First argument is z0, second z1 and so on. Resulting function
	 * will be of format:
	 * <br>
	 * <br> f(z) = zn*z^n + ... + z1*z^1 + z0*z^0.
	 *
	 * @param factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.factors = Objects.requireNonNull(factors, "Factors should not be null.");
	}
	
	/**
	 * Returns the order of this polynomial.
	 * <br> E.g. (7+2i)z^3+2z^2+5z+1, method returns 3.
	 *
	 * @return the order of this polynomial
	 */
	public short order() {
		for(int i = factors.length - 1; i >= 0; i--) {
			if(factors[i] != null)
				return (short) i;
		}
		return 0;
	}
	
	/**
	 * Computes a new polynomial that is the result of
	 * the multiplication of {@code this} polynomial
	 * and the given polynomial {@code p}.
	 *
	 * @param p the given polynomial
	 * @return the new polynomial that is the result of
	 * 		   the multiplication operation
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int newOrder = this.order() + p.order() + 2;
		Complex[] newFactors = new Complex[newOrder];
		
		for(int i = 0; i < factors.length; i++) {
			if(factors[i] == null) continue;
			
			for(int j = 0; j < p.factors.length; j++) {
				if(p.factors[j] == null) continue;
				
				int order = i + j;
				Complex factor = factors[i].multiply(p.factors[j]);
				
				if(newFactors[order] == null) {
					newFactors[order] = factor;
				} else {
					newFactors[order] = newFactors[order].add(factor);
				}
			}
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Computes first derivative of this polynomial.
	 * <br> E.g. (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5.
	 *
	 * @return a new polynomial that is the first derivative of
	 * 		   this polynomial
	 */
	public ComplexPolynomial derive() {
		Complex[] newFactors = new Complex[factors.length - 1];
		
		for(int n = 1; n < factors.length; n++) {
			if(factors[n] == null) continue;
			newFactors[n-1] = factors[n].multiply(new Complex(n, 0));
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Computes the polynomial value at the given point {@code z}.
	 *
	 * @param z the point of function evaluation
	 * @return the polynomial value at the given point {@code z}
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		
		for(int n = 0; n < factors.length; n++) {
			if(factors[n] == null) continue;
			Complex member = factors[n].multiply(z.power(n));
			result = result.add(member);
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		for(int i = factors.length - 1; i > 0; i--) {
			if(factors[i] == null || factors[i].equals(Complex.ZERO))
				continue;

			sb.append("(" + factors[i] + ")*z^" + i + " + ");
		}
		
		// Append the z0.
		sb.append("(" + (factors[0] == null ? Complex.ZERO : factors[0]) + ")");
		
		return sb.toString();
	}
}
