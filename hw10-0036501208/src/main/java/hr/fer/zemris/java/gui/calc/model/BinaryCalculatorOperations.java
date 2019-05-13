package hr.fer.zemris.java.gui.calc.model;

import java.util.function.DoubleBinaryOperator;

/**
 * A class that offers some basic binary operations.
 * Binary meaning that 2 operands are needed to execute
 * the operation.
 *
 * @author Filip Nemec
 */
public class BinaryCalculatorOperations {
	
	// Singleton, since we don't want instances of this class.
	private BinaryCalculatorOperations() {}
	
	/** Returns the result of adding 2 {@code double} values. */
	public static final DoubleBinaryOperator ADD = (a, b) -> a + b;
	
	/** Returns the result of subtracting 2 {@code double} values. */
	public static final DoubleBinaryOperator SUB = (a, b) -> a - b;
	
	/** Returns the result of multiplying 2 {@code double} values. */
	public static final DoubleBinaryOperator MUL = (a, b) -> a * b;
	
	/** Returns the result of dividing 2 {@code double} values. */
	public static final DoubleBinaryOperator DIV = (a, b) -> a / b;
	
	/** Returns the result of powering {@code double} value {@code x} to the n-th power. */
	public static final DoubleBinaryOperator X_POWER_N = (x, n) -> Math.pow(x, n);
	
	/** Returns the n-th square of the {@code double} value {@code x}. */
	public static final DoubleBinaryOperator N_SQUARE_X = (x, n) -> Math.pow(x, 1/n);
}
