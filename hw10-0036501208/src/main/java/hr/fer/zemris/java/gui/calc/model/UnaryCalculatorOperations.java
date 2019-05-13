package hr.fer.zemris.java.gui.calc.model;

import java.util.function.DoubleUnaryOperator;

/**
 * Class which offers basic unary operations.
 *
 * @author Filip Nemec
 */
public class UnaryCalculatorOperations {
	
	// Singleton, since we don't want instances of this class.
	private UnaryCalculatorOperations() {}
	
	/** The inverse value 1/x. */
	public static final DoubleUnaryOperator INVERSE = x -> 1/x;
	
	/** Base 10 logarithm. */
	public static final DoubleUnaryOperator LOG = x -> Math.log10(x);
	
	/** Natural logarithm. */
	public static final DoubleUnaryOperator LN = x -> Math.log(x);
	
	/** Sine of x. */
	public static final DoubleUnaryOperator SIN = x -> Math.sin(x);
	
	/** Cosine of x. */
	public static final DoubleUnaryOperator COS = x -> Math.cos(x);
	
	/** Tangent of x. */
	public static final DoubleUnaryOperator TAN = x -> Math.tan(x);
	
	/** Cotangent of x. */
	public static final DoubleUnaryOperator CTG = x -> 1 / Math.tan(x);
	
	//----------------------------------------------------------------
	//						INVERSE OPERATIONS
	//----------------------------------------------------------------
	
	/** Calculates 10^x. */
	public static final DoubleUnaryOperator TEN_POWER_X = x -> Math.pow(10, x);
	
	/** Calculates e^x. */
	public static final DoubleUnaryOperator E_POWER_X = x -> Math.pow(Math.E, x);
	
	/** Arc-sine of x. */
	public static final DoubleUnaryOperator ARCSIN = x -> Math.asin(x);
	
	/** Arc-cosine of x. */
	public static final DoubleUnaryOperator ARCCOS = x -> Math.acos(x);
	
	/** Arc-tangent of x. */
	public static final DoubleUnaryOperator ARCTAN = x -> Math.atan(x);
	
	/** Arc-cotangent of x. */
	public static final DoubleUnaryOperator ARCCTG = x -> Math.atan(1/x);
}
