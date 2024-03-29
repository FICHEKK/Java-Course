package hr.fer.zemris.java.gui.calc;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

/**
 * Models the implementation of a simple calculator.
 *
 * @author Filip Nemec
 */
public class CalcModelImpl implements CalcModel {
	
	/** A list of all the subscribed listeners. */
	private List<CalcValueListener> listeners = new LinkedList<>();
	
	/** The default display value. */
	private static final double DEFAULT_VALUE = 0;
	
	/** The default display value represented as a {@code String}. */
	private static final String DEFAULT_VALUE_STRING = "";
	
	/** If this calculator is currently editable. */
	private boolean isEditable = true;
	
	/** If the display value is currently positive. */
	private boolean isPositive = true;
	
	/** The current value stored by this calculator. */
	private double value = DEFAULT_VALUE;
	
	/** The current value stored by this calculator represented as a {@code String}. */
	private String valueString = DEFAULT_VALUE_STRING;
	
	/** The operand stored by the previous operation, or is waiting to be operated on. */
	private Double activeOperand = null;
	
	/** The operation currently used by this calculator. */
	private DoubleBinaryOperator pendingOperation = null;
	


	@Override
	public void swapSign() throws CalculatorInputException {
		if(!isEditable)
			throw new CalculatorInputException("Cannot swap sign as the state is not editable.");
		
		isPositive = !isPositive;
		
		notifyListeners();
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!isEditable)
			throw new CalculatorInputException("Cannot insert decimal point as the state is not editable.");
		
		if(valueString.contains("."))
			throw new CalculatorInputException("Decimal point already exists in the current number.");
		
		if(valueString.isEmpty())
			throw new CalculatorInputException("Input is empty, please insert a number first.");
		
		valueString += ".";
		notifyListeners();
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!isEditable)
			throw new CalculatorInputException("Cannot insert digit as the state is not editable.");
		
		if(digit < 0 || digit > 9)
			throw new IllegalArgumentException("Expected digit in range from 0 to 9. Was: " + digit);
		
		if(digit == 0 && !valueString.contains(".") && Math.abs(value - 0.0) < 1E-7 && !valueString.isEmpty())
			return;
		
		if(valueString.equals("0")) {
			valueString = String.valueOf(digit);
			value = digit;
			notifyListeners();
			return;
		}
		
		try {
			valueString += String.valueOf(digit);
			value = Double.parseDouble(valueString);
			
			if(Double.isInfinite(value))
				throw new NumberFormatException();
			
			notifyListeners();
			
		} catch(NumberFormatException e) {
			throw new CalculatorInputException("Could not convert to a numeric value. Digit was not inserted.");
			
		}
	}
	
	//-------------------------------------------------------------------
	//							GETTERS
	//-------------------------------------------------------------------
	
	@Override
	public double getValue() {
		return isPositive ? value : -value;
	}
	
	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(activeOperand == null)
			throw new IllegalStateException("Active operand has not been set.");
		
		return activeOperand;
	}
	
	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return pendingOperation;
	}
	
	@Override
	public boolean isEditable() {
		return isEditable;
	}
	
	@Override
	public boolean isActiveOperandSet() {
		return activeOperand != null;
	}
	
	//-------------------------------------------------------------------
	//							SETTERS
	//-------------------------------------------------------------------
	
	@Override
	public void setValue(double value) {
		isPositive = value >= 0 ? true : false;
		
		this.value = Math.abs(value);
		valueString = String.valueOf(this.value);
		isEditable = false;
		
		notifyListeners();
	}
	
	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
	}
	
	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
		this.pendingOperation = op;
	}
	
	//-------------------------------------------------------------------
	//						CLEARING METHODS
	//-------------------------------------------------------------------
	
	@Override
	public void clearActiveOperand() {
		activeOperand = null;
	}
	
	@Override
	public void clear() {
		value = DEFAULT_VALUE;
		valueString = DEFAULT_VALUE_STRING;
		isEditable = true;
		isPositive = true;
		
		notifyListeners();
	}

	@Override
	public void clearAll() {
		activeOperand = null;
		pendingOperation = null;
		
		clear();
	}
	
	//-------------------------------------------------------------------
	//						LISTENER METHODS
	//-------------------------------------------------------------------

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		Objects.requireNonNull(l, "Given listener must not be null.");
		listeners.add(l);
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		Objects.requireNonNull(l, "Given listener must not be null.");
		listeners.remove(l);
	}
	
	/**
	 * Notifies all of the active listeners once the change
	 * has been made.
	 */
	private void notifyListeners() {
		for(CalcValueListener listener : listeners) {
			listener.valueChanged(this);
		}
	}
	
	//-------------------------------------------------------------------
	//							toString
	//-------------------------------------------------------------------
	
	@Override
	public String toString() {
		if(valueString.isEmpty())
			return isPositive ? "0" : "-0";
		
		return isPositive ? valueString : "-" + valueString;
	}
}
