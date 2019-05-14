package hr.fer.zemris.java.gui.calc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
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
	
	/** Check-box used to invert the operations in this calculator. */
	private JCheckBox checkBox = new JCheckBox("Inv");
	
	/** The default font size used by the components. */
	private static final float FONT_SIZE = 30f;
	
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
		
		// Inverse operation check-box.
		contentPane.add(checkBox, "5,7");
		checkBox.setFont(checkBox.getFont().deriveFont(FONT_SIZE));
		
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
		
		// Positioning in the middle of the screen,
		// independent of the screen resolution.
		Dimension pane = contentPane.getPreferredSize();
		setSize((int)(pane.width * 1.25), (int)(pane.height * 1.25));
		
		Dimension frame = getSize();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - frame.width) / 2,
					(screen.height - frame.height) / 2);
		
		// Configuration
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Calculator");
	}

	private void initEqualsButton() {
		ActionListener listener = event -> {
			DoubleBinaryOperator op = calculator.getPendingBinaryOperation();
			
			if(!calculator.isActiveOperandSet()) {
				JOptionPane.showMessageDialog(this, "Operand has not been set.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(op == null) {
				JOptionPane.showMessageDialog(this, "Operation has not been set.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			calculator.setValue(op.applyAsDouble(calculator.getActiveOperand(), calculator.getValue()));
			calculator.clearActiveOperand();
		};
		
		createButton("=", "1,6", listener);
	}
	
	//-------------------------------------------------------------------------
	//						  CLEARING OPERATIONS
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes the clear button.
	 */
	private void initClearButton() {
		ActionListener listener = event -> calculator.clear();
		createButton("clr", "1,7", listener);
	}
	
	/**
	 * Initializes the reset button.
	 */
	private void initResetButton() {
		ActionListener listener = event -> calculator.clearAll();
		createButton("reset", "2,7", listener);
	}
	
	//-------------------------------------------------------------------------
	//							STACK OPERATIONS
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes the stack buttons.
	 */
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
	
	/**
	 * Initializes the unary operation buttons.
	 */
	private void initUnaryOperationButtons() {
		createUnaryOperationButton("1/x", x -> 1/x, "2,1");
		createInverseUnaryOperationButton("log", Math::log10, "10^x", x -> Math.pow(10, x), "3,1");
		createInverseUnaryOperationButton("ln", Math::log, "e^x", x -> Math.pow(Math.E, x), "4,1");
		
		createInverseUnaryOperationButton("sin", Math::sin, "arcsin", Math::asin, "2,2");
		createInverseUnaryOperationButton("cos", Math::cos, "arccos", Math::acos, "3,2");
		createInverseUnaryOperationButton("tan", Math::tan, "arctan", Math::atan, "4,2");
		createInverseUnaryOperationButton("ctg", x -> 1/Math.tan(x), "arcctg", x -> Math.atan(1/x), "5,2");
	}
	
	/**
	 * Helper factory method that creates the unary operation button
	 * from the specified parameters.
	 *
	 * @param title the title of the button 
	 * @param operator the operation this button performs
	 * @param position the layout position
	 */
	private void createUnaryOperationButton(String title, DoubleUnaryOperator operator, String position) {
		ActionListener listener = event -> calculator.setValue(operator.applyAsDouble(calculator.getValue()));
		createButton(title, position, listener);
	}
	
	/**
	 * Helper factory method that creates an inverse unary operation button.
	 *
	 * @param operationTitle the operation title
	 * @param operation the operation
	 * @param inverseTitle the inverse operation title
	 * @param inverse the inverse operation
	 * @param position the layout position
	 */
	private void createInverseUnaryOperationButton(String operationTitle, DoubleUnaryOperator operation,
												   String inverseTitle, DoubleUnaryOperator inverse,
												   String position) {
		ActionListener performOperation = event -> calculator.setValue(operation.applyAsDouble(calculator.getValue()));
		ActionListener performInverse   = event -> calculator.setValue(inverse.applyAsDouble(calculator.getValue()));
		
		createInverseOperationButton(operationTitle, performOperation, inverseTitle, performInverse, position);
	}
	
	//-------------------------------------------------------------------------
	//							BINARY OPERATIONS
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes the binary operation buttons.
	 */
	private void initBinaryOperationButtons() {
		createBinaryOperationButton("/", (d1, d2) -> d1 / d2, "2,6");
		createBinaryOperationButton("*", (d1, d2) -> d1 * d2, "3,6");
		createBinaryOperationButton("-", (d1, d2) -> d1 - d2, "4,6");
		createBinaryOperationButton("+", (d1, d2) -> d1 + d2, "5,6");
		
		createInverseBinaryOperationButton("x^n", (x, n) -> Math.pow(x, n), "x^(1/n)", (x, n) -> Math.pow(x, 1/n), "5,1");
	}
	
	/**
	 * Helper factory method that creates the binary operation button
	 * from the specified parameters.
	 *
	 * @param title the title of the button 
	 * @param operator the operation this button performs
	 * @param position the layout position
	 */
	private void createBinaryOperationButton(String title, DoubleBinaryOperator operator, String position) {
		ActionListener listener = event -> onBinaryOperatorClick(operator);
		createButton(title, position, listener);
	}
	
	/**
	 * Helper factory method that creates an inverse binary operation button.
	 *
	 * @param operationTitle the operation title
	 * @param operation the operation
	 * @param inverseTitle the inverse operation title
	 * @param inverse the inverse operation
	 * @param position the layout position
	 */
	private void createInverseBinaryOperationButton(String operationTitle, DoubleBinaryOperator operation,
													String inverseTitle, DoubleBinaryOperator inverse,
												    String position) {
		ActionListener performOperation = event -> onBinaryOperatorClick(operation);
		ActionListener performInverse = event -> onBinaryOperatorClick(inverse);
		
		createInverseOperationButton(operationTitle, performOperation, inverseTitle, performInverse, position);
	}

	//-------------------------------------------------------------------------
	//							INPUT BUTTONS 
	//-------------------------------------------------------------------------
	
	/**
	 * Initializes the calculator digit buttons.
	 */
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
	}
	
	/**
	 * Initializes the calculator decimal point button.
	 */
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
	
	/**
	 * Initializes the calculator swap sign button.
	 */
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
	//							FACTORY METHODS 
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
		JButton button = createButton(text, FONT_SIZE);
		button.addActionListener(listener);
		contentPane.add(button, position);
	}
	
	/**
	 * Creates a button with the specified text and font size.
	 *
	 * @param text the text
	 * @param fontSize the font size
	 * @return a button with the specified text and font size
	 */
	private JButton createButton(String text, float fontSize) {
		JButton button = new JButton(text);
		button.setFont(button.getFont().deriveFont(fontSize));
		return button;
	}
	
	/**
	 * Creates a button which will change its functionality once the check-box
	 * is clicked.
	 *
	 * @param operationTitle the operation title
	 * @param performOperation the operation functionality
	 * @param inverseTitle the inverse operation title
	 * @param performInverse the inverse operation functionality
	 * @param position the layout position
	 */
	private void createInverseOperationButton(String operationTitle, ActionListener performOperation,
											  String inverseTitle, ActionListener performInverse,
											  String position) {
		var swappable = new SwappableButton(operationTitle, inverseTitle, performOperation, performInverse); 
		checkBox.addItemListener(swappable);
		contentPane.add(swappable.getButton(), position);
	}
	
	//-------------------------------------------------------------------------
	//								DISPLAY 
	//-------------------------------------------------------------------------

	/**
	 * Initializes the calculator display.
	 */
	private void initDisplay() {
		Display display = new Display("0", SwingConstants.RIGHT);
		display.setFont(display.getFont().deriveFont(FONT_SIZE * 2));
		display.setBackground(Color.YELLOW);
		display.setOpaque(true);
		
		contentPane.add(display, "1,1");
		calculator.addCalcValueListener(display);
	}
	
	//-------------------------------------------------------------------------
	//							HELPER METHODS 
	//-------------------------------------------------------------------------
	
	/**
	 * A helper method which sets resolves the binary operator click. It works by checking
	 * if there already is an active operand set and decides the following action:
	 * <br>
	 * <br> a) If not set, simply set the current on screen value to the active operand.
	 * <br> b) If set, calculate the new value based on the given operator and save the 
	 * 		   result to as the active operand.
	 * <p>
	 * After the operator click, clears the screen and changes the pending operator
	 *
	 * @param operator the operator strategy which holds the operation
	 */
	private void onBinaryOperatorClick(DoubleBinaryOperator operator) {
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
			new Calculator().setVisible(true);
		});
	}
	
	//-------------------------------------------------------------------------
	//							STATIC CLASSES 
	//-------------------------------------------------------------------------
	
	/**
	 * Models a button which has 2 functionalities which will
	 * be swapped once the subject's event triggers.
	 *
	 * @author Filip Nemec
	 */
	private class SwappableButton implements ItemListener {
		
		/** The button. */
		private JButton button;
		
		/** The first title which represents the first functionality. */
		private String title1;
		
		/** The second title which represents the second functionality. */
		private String title2;
		
		/** The first functionality. */
		private ActionListener action1;
		
		/** The second functionality. */
		private ActionListener action2;
		
		/**
		 * Constructs a new swappable button.
		 *
		 * @param title1 the title which represents the first functionality
		 * @param title2 the title which represents the second functionality
		 * @param action1 the first functionality
		 * @param action2 the second functionality
		 */
		public SwappableButton(String title1, String title2,
							   ActionListener action1, ActionListener action2) {
			this.title1 = title1;
			this.title2 = title2;
			this.action1 = action1;
			this.action2 = action2;
			
			button = createButton(title1, FONT_SIZE);
			button.addActionListener(action1);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				button.setText(title2);
				button.addActionListener(action2);
				button.removeActionListener(action1);
				
			} else {
				button.setText(title1);
				button.addActionListener(action1);
				button.removeActionListener(action2);
				
			}
		}
		
		/**
		 * Returns the {@code JButton} this swappable button encapsulates.
		 *
		 * @return the {@code JButton} this swappable button encapsulates
		 */
		public JButton getButton() {
			return button;
		}
	}
	
	/**
	 * Models the display used by the {@link Calculator}.
	 *
	 * @author Filip Nemec
	 */
	private static class Display extends JLabel implements CalcValueListener {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Constructs a new display with the given initial
		 * text and the specified horizontal alignment.
		 *
		 * @param text the initial text
		 * @param horizontalAlignment the horizontal alignment
		 */
		public Display(String text, int horizontalAlignment) {
			super(text, horizontalAlignment);
		}

		@Override
		public void valueChanged(CalcModel model) {
			SwingUtilities.invokeLater(() -> setText(model.toString()));
		}
	}
}
