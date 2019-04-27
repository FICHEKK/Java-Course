package hr.fer.zemris.java.custom.scripting.exec;

/**
 * A simple wrapper class that holds a value. It also
 * offers some basic arithmetic and comparison operations for
 * the storing value, but only if the storing value is {@code null}
 * or one of the following types: {@code String}, {@code Integer}
 * or {@code Double}.
 *
 * @author Filip Nemec
 */
public class ValueWrapper {
	
	/** The value that this wrapper stores. */
	private Object value;
	
	/**
	 * Construcs a new wrapper for the given value.
	 *
	 * @param value the value to be wrapped
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}
	
	/**
	 * Increases the current value by the given one.
	 *
	 * @param incValue the incrementing value
	 */
	public void add(Object incValue) {
		calculate(incValue, Operations.ADD);
	}
	
	/**
	 * Subtracts the current value by the given one.
	 *
	 * @param decValue the subtracting value
	 */
	public void subtract(Object decValue) {
		calculate(decValue, Operations.SUB);
	}
	
	/**
	 * Multiplies the current value by the given one.
	 *
	 * @param mulValue the multiplying value
	 */
	public void multiply(Object mulValue) {
		calculate(mulValue, Operations.MUL);
	}
	
	/**
	 * Divides the current value by the given one.
	 *
	 * @param divValue the dividing value
	 */
	public void divide(Object divValue) {
		calculate(divValue, Operations.DIV);
	}
	
	/**
	 * Comapres the {@link #value} with the given one.
	 *
	 * @param withValue the value that will be compared against the {@link #value}
	 * @return {@code 0} if values are equal, {@code 1} if {@link #value} is greater,
	 * 		   {@code -1} otherwise
	 */
	public int numCompare(Object withValue) {
		Number n1 = toNumber(this.value);
		Number n2 = toNumber(withValue);
		
		if(n1 instanceof Integer && n2 instanceof Integer) {
			return ((Integer) n1).compareTo((Integer) n2); 
		} else {
			return ((Double) n1).compareTo((Double) n2); 
		}
	}
	
	/**
	 * Performs the given operation on the {@link #value}.
	 *
	 * @param other the other operand
	 * @param op the operation
	 * @throws IllegalArgumentException if the given input is not {@code null}, 
	 * 									{@code Integer}, {@code Double} or {@code String}
	 * @throws NumberFormatException if the given value was {@code String} and it could not be
	 * 								 converted to {@code Number}
	 */
	private void calculate(Object other, Operation op) {
		Number n1 = toNumber(this.value);
		Number n2 = toNumber(other);
		
		if(n1 instanceof Integer && n2 instanceof Integer) {
			this.value = op.asInt((Integer) n1, (Integer) n2);
		} else {
			// Since Integer can't be directly cast to Double.
			double d1 = n1 instanceof Integer ? ((Integer) n1).intValue() : (Double) n1;
			double d2 = n2 instanceof Integer ? ((Integer) n2).intValue() : (Double) n2;
			this.value = op.asDouble(d1, d2);
		}
	}
	
	/**
	 * Checks if the given {@code Object} instance is of valid type,
	 * and if it is, returns the appropriate {@code Number} value.
	 * If the input is not valid, exception will be thrown.
	 *
	 * @param o the {@code Object} instance that will be converted
	 * @return {@code Number} representation of the given {@code Object}
	 * @throws IllegalArgumentException if the given input is not {@code null}, 
	 * 									{@code Integer}, {@code Double} or {@code String}
	 * @throws NumberFormatException if the given value was {@code String} and it could not be
	 * 								 converted to {@code Number}
	 */
	private static Number toNumber(Object o) {
		if(o == null) return Integer.valueOf(0);
		if(o instanceof Integer) return (Integer) o;
		if(o instanceof Double) return (Double) o;
		
		if(o instanceof String) {
			String number = (String) o;
			if(number.indexOf('.') >= 0 || number.indexOf('E') >= 0) {
				return Double.parseDouble(number);
			} else {
				return Integer.parseInt(number);
			}
		}
	
		throw new IllegalArgumentException("Given object could not be converted to instance of class Number.");
	}
	
	/**
	 * Sets the {@link #value} to the given one.
	 *
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * Returns the value that this wrapper stores.
	 *
	 * @return the value that this wrapper stores
	 */
	public Object getValue() {
		return value;
	}
}
