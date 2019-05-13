package hr.fer.zemris.math;

import java.util.Objects;

import hr.fer.zemris.java.fractals.cmplxcache.ComplexCache;
import hr.fer.zemris.java.fractals.cmplxcache.IComplexCache;

/**
 * Models a function in the complex domain of the following
 * format:
 * <br>
 * <br> f(z) = z0 * (z-z1) * (z-z2) * ... * (z-zn)
 * <br> z0 is a constant
 * <br> zi are roots, where 1 <= i <= n
 *
 * @author Filip Nemec
 */
public class ComplexRootedPolynomial {
	
	/** The constant. */
	private Complex constant;
	
	/** The roots of this rooted polynomial function. */
	private Complex[] roots;
	
	/**
	 * Constructs a new complex rooted polynomial with the given
	 * constant and roots.
	 * <p>
	 * Constructed polynomial will be a function in the complex
	 * domain of the following format:
	 * <br>
	 * <br> f(z) = z0 * (z-z1) * (z-z2) * ... * (z-zn)
	 * <br> z0 is a constant
	 * <br> zi are roots, where 1 <= i <= n
	 *
	 * @param constant the constant
	 * @param roots the roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = Objects.requireNonNull(constant, "Constant should not be null.");
		this.roots = roots;
	}
	
	/**
	 * Computes the polynomial value at the given point {@code z}.
	 *
	 * @param z the point of function evaluation
	 * @return the polynomial value at the given point {@code z}
	 */
	public Complex apply(Complex z) {		
//		for(Complex zi : roots) {
//			if(zi == null) continue;
//			Complex parenthesis = z.sub(zi);
//			result = result.multiply(parenthesis);
//		}
		
		IComplexCache cache = ComplexCache.getCache();
		Complex result = cache.get(constant);
		
		for(int i = 0; i < roots.length; i++) {
			if(roots[i] == null) continue;
			Complex parenthesis = cache.get(z);
			
			parenthesis.modifySub(roots[i]);
			result.modifyMultiply(parenthesis);
			
			cache.release(parenthesis);
		}
		
		return result;
	}
	
	/**
	 * Converts this rooted polynomial to the "standard" polynomial
	 * format.
	 * <p>
	 * z0*(z-z1) => z0*z^1 - z0*z1
	 * 
	 *
	 * @return this rooted polynomial, but in "standard" form
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial polynomial = new ComplexPolynomial(constant);
		
		for(int i = 0; i < roots.length; i++) {
			Complex root = roots[i] == null ? Complex.ZERO : roots[i];
			polynomial = polynomial.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		
		return polynomial;
	}
	
	/**
	 * Finds index of closest root for given complex number {@code z}
	 * that is within threshold. If there is no such root, returns
	 * {@code -1}.
	 *
	 * @param z the complex number that is used as the point of reference
	 * 			for the distance
	 * @param threshold the minimal distance a root needs to be from
	 * 		  the given complex number in order to be considered as
	 * 		  the result
	 * @return index of the closest root within threshold, if such is
	 * 		   found, otherwise {@code -1}
	 */
	public int indexOfClosestRootFor(Complex z, double threshold) {
		IComplexCache cache = ComplexCache.getCache();
		
		double minDistance = threshold;
		int indexOfClosestRoot = -1;
		
		for(int i = 0; i < roots.length; i++) {
			if(roots[i] == null) continue;
			
			Complex zSubtracted = cache.get(z);
			zSubtracted.modifySub(roots[i]);
			
			double distance = zSubtracted.module();
			
			if(distance < minDistance) {
				minDistance = distance;
				indexOfClosestRoot = i;
			}
			
			cache.release(zSubtracted);
		}
		
		return indexOfClosestRoot;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		if(!constant.equals(Complex.ZERO)) {
			sb.append("(" + constant + ")");
		}
		
		for(Complex root : roots) {
			if(root == null || root.equals(Complex.ZERO))
				continue;
			
			sb.append(" * (z-(" + root + "))");
		}
		
		return sb.toString();
	}
}
