package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.model.BinaryCalculatorOperations;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
import hr.fer.zemris.java.gui.calc.model.UnaryCalculatorOperations;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * A class that models the standard Windows calculator. It offers
 * standard calculation operations such as addition and multiplication,
 * but also more advanced forms of calculation: trigonometric
 * functions, logarithms, square roots and such.
 * <p>
 * In addition to
 * functionality mentioned above, it also offers a stack for saving
 * the results and some utility methods for clearing the display.
 *
 * @author Filip Nemec
 */
public class Calculator extends JFrame {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

	/** 
	 * Reference to the calculator model, which is essentially
	 * the engine of this calculator.
	 */
	private CalcModelImpl calculator = new CalcModelImpl();
	
	/** The stack used by this calculator. */
	private Stack<Double> stack = new Stack<>();
	
	/** Reference to the content pane of this frame. */
	private Container contentPane = getContentPane();
	
	/** The default font used by the components. */
	private final Font DEFAULT_FONT = new Font(getContentPane().getFont().getName(), Font.PLAIN, 32);
	
	/**
	 * Constructs a new calculator which will automatically
	 * initialize the graphical user interface.
	 */
	public Calculator() {
		initGUI();
	}
	
	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		contentPane.setLayout(new CalcLayout(5));
		
		// First row
		initDisplay();
		initEqualsButton();
		
		// Input
		initDigitButtons();
		initSwapSignButton();
		initDecimalPointButton();
		
		// Operations
		initUnaryOperationButtons();
		initBinaryOperationButtons();
		
		// Utility
		initClearButton();
		initResetButton();
		initStackButtons();
		
		// Configuration
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation(300, 200);
		setSize(800, 500);
		setVisible(true);
	}
	
	private void initEqualsButton() {
		ActionListener listener = event -> {
			DoubleBinaryOperator op = calculator.getPendingBinaryOperation();
			
			if(!calculator.isActiveOperandSet()) {
				JOptionPane.showMessageDialog(this, "Operand not yet set.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(op == null) {
				JOptionPane.showMessageDialog(this, "Operation not yet set.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			calculator.setValue(op.applyAsDouble(calculator.getActiveOperand(), calculator.getValue()));
			calculator.clearActiveOperand();
		};
		
		createButton("=", "1,6", listener);
	}

	private void initDecimalPointButton() {
		ActionListener listener = event -> {
			try {
				calculator.insertDecimalPoint();
			} catch(CalculatorInputException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		};
		
		createButton(".", "5,5", listener);
	}
	
	private void initSwapSignButton() {
		ActionListener listener = event -> {
			try {
				calculator.swapSign();
			} catch(CalculatorInputException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		};
		
		createButton("+/-", "5,4", listener);
	}
	
	//-------------------------------------------------------------------------
	//						  CLEARING OPERATIONS
	//-------------------------------------------------------------------------
	
	private void initClearButton() {
		ActionListener listener = event -> calculator.clear();
		createButton("clr", "1,7", listener);
	}
	
	private void initResetButton() {
		ActionListener listener = event -> calculator.clearAll();
		createButton("reset", "2,7", listener);
	}
	
	//-------------------------------------------------------------------------
	//							STACK OPERATIONS
	//-------------------------------------------------------------------------
	
	private void initStackButtons() {
		createButton("push", "3,7", event -> stack.push(calculator.getValue()));
		createButton("pop", "4,7", event -> {
			if(stack.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Stack is empty.", "Error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			calculator.setValue(stack.pop());
		});
	}

	//-------------------------------------------------------------------------
	//							UNARY OPERATIONS
	//-------------------------------------------------------------------------
	
	private void initUnaryOperationButtons() {
		createUnaryOperationButton("1/x", "2,1", UnaryCalculatorOperations.INVERSE);
		createUnaryOperationButton("log", "3,1", UnaryCalculatorOperations.LOG);
		createUnaryOperationButton("ln", "4,1", UnaryCalculatorOperations.LN);
		
		createUnaryOperationButton("sin", "2,2", UnaryCalculatorOperations.SIN);
		createUnaryOperationButton("cos", "3,2", UnaryCalculatorOperations.COS);
		createUnaryOperationButton("tan", "4,2", UnaryCalculatorOperations.TAN);
		createUnaryOperationButton("ctg", "5,2", UnaryCalculatorOperations.CTG);
	}
	
	private void createUnaryOperationButton(String text, String position, DoubleUnaryOperator operator) {
		ActionListener listener = event -> calculator.setValue(operator.applyAsDouble(calculator.getValue()));
		createButton(text, position, listener);
	}
	
	//-------------------------------------------------------------------------
	//							BINARY OPERATIONS
	//-------------------------------------------------------------------------
	
	private void initBinaryOperationButtons() {
		createBinaryOperationButton("/", "2,6", BinaryCalculatorOperations.DIV);
		createBinaryOperationButton("*", "3,6", BinaryCalculatorOperations.MUL);
		createBinaryOperationButton("-", "4,6", BinaryCalculatorOperations.SUB);
		createBinaryOperationButton("+", "5,6", BinaryCalculatorOperations.ADD);
		createBinaryOperationButton("x^n", "5,1", BinaryCalculatorOperations.X_POWER_N);
	}
	
	private void createBinaryOperationButton(String text, String position, DoubleBinaryOperator operator) {
		ActionListener listener = event -> {
			double onScreenValue = calculator.getValue();
			
			if(calculator.isActiveOperandSet()) {
				double savedValue = calculator.getActiveOperand();
				DoubleBinaryOperator op = calculator.getPendingBinaryOperation();
				calculator.setActiveOperand(op.applyAsDouble(savedValue, onScreenValue));
				
			} else {
				calculator.setActiveOperand(onScreenValue);
				
			}
			
			calculator.clear();
			calculator.setPendingBinaryOperation(operator);
		};
		
		createButton(text, position, listener);
	}
	
	//-------------------------------------------------------------------------
	//							DIGIT BUTTONS 
	//-------------------------------------------------------------------------
	
	private void initDigitButtons() {
		ActionListener listener = event -> {
			if(!calculator.isEditable()) {
				JOptionPane.showMessageDialog(this, "Not editable at the moment.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JButton button = (JButton) event.getSource();
			int digit = button.getText().charAt(0) - '0';
			calculator.insertDigit(digit);
		};
		
		createButton("0", "5,3", listener);
		createButton("1", "4,3", listener);
		createButton("2", "4,4", listener);
		createButton("3", "4,5", listener);
		createButton("4", "3,3", listener);
		createButton("5", "3,4", listener);
		createButton("6", "3,5", listener);
		createButton("7", "2,3", listener);
		createButton("8", "2,4", listener);
		createButton("9", "2,5", listener);
		
//		int buttonNumber = 1;
//		for(int row = 4; row >= 2; row--) {
//			for(int column = 3; column <= 5; column++) {
//				JButton button = new JButton(String.valueOf(buttonNumber));
//				contentPane.add(button, new RCPosition(row, column));
//				button.setFont(DEFAULT_FONT);
//				
//				buttonNumber++;
//			}
//		}
	}
	
	//-------------------------------------------------------------------------
	//							HELPER METHODS 
	//-------------------------------------------------------------------------
	
	/**
	 * Creates a button with the given text, position and action to be performed
	 * upon clicking.
	 *
	 * @param text the text
	 * @param position the position
	 * @param listener the action to be performed by this button
	 */
	private void createButton(String text, String position, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		contentPane.add(button, position);
	}
	
	//-------------------------------------------------------------------------
	//								DISPLAY 
	//-------------------------------------------------------------------------

	private void initDisplay() {
		Display display = new Display("0", SwingConstants.RIGHT);
		display.setFont(DEFAULT_FONT);
		display.setBackground(Color.YELLOW);
		display.setOpaque(true);
		
		contentPane.add(display, "1,1");
		calculator.addCalcValueListener(display);
	}

	//-------------------------------------------------------------------------
	//								main 
	//-------------------------------------------------------------------------
	
	/**
	 * A very simple program that simply creates a new {@code Calculator}
	 * instance in the EDT (event dispatching thread).
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator();
		});
	}
}
