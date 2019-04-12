package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Simple console application which performs
 * simple calculation for given expression. It
 * uses postfix representation and stack to do so.
 * 
 * Said expression should be passed via the console
 * and should be exactly 1 string put in quotes.
 */
public class StackDemo {
	/** Stack should be of this size after expression. */
	private static final int VALID_STACK_SIZE_AFTER_EXPRESSION = 1;
	
	/**
	 * Starts executing code from here.
	 * 
	 * @param args The expression that is being evaluated.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Number of arguments should be 1! Closing...");
			return;
		}
		
		String expression = args[0];
		String[] elements = expression.split("\\s");
		
		ObjectStack expressionStack = new ObjectStack();
		
		try {
			for(String elem : elements) {
				try {
					Integer number = Integer.parseInt(elem);
					expressionStack.push(number);
				} catch(NumberFormatException nfe) {
					//This part gets the operands.
					int element1, element2;
					
					try {
						element2 = (Integer)expressionStack.pop();
						element1 = (Integer)expressionStack.pop();
					} catch(EmptyStackException ese) {
						System.err.println("Invalid expression! Tried to get elements from an empty stack.");
						return;
					}
					
					//This part gets the result for given operation.
					try {
						int result = performOperation(element1, element2, elem);
						expressionStack.push(result);
					} catch(ArithmeticException ae) {
						System.err.println("Invalid expression! Dividing by zero occured!");
						return;
					} catch (IllegalArgumentException iae) {
						System.err.println("Invalid expression! '" + elem + "' is an invalid operation!");
						return;
					}
				}
			}
		} catch(ArithmeticException ex) {
	        System.out.println("You tried to divide by zero! Closing...");
	        return;
	    }
		
		if(expressionStack.size() != VALID_STACK_SIZE_AFTER_EXPRESSION) {
			System.err.println("Invalid expression! Closing...");
			return;
		} else {
			System.out.println("Expression evaluates to " + expressionStack.pop() + ".");
		}
		
		// -------------------------------------
		// Simple stack tests, please ignore.
		// -------------------------------------
		ObjectStack stack = new ObjectStack();
		
		System.out.println(stack.size()); // 0
		
		stack.push(5);
		stack.push(3);
		
		System.out.println(stack.peek()); // 3
		
		Object popped = stack.pop();
		
		System.out.println(popped); // 3
		System.out.println(stack.size()); // 1
		
		Object popped2 = stack.pop();
		
		System.out.println(popped2); // 5
		System.out.println(stack.size()); // 0
		
		stack.push(1);
		stack.push(2);
		stack.push(3);
		
		System.out.println(stack.size()); // 3
		
		stack.clear();
		
		System.out.println(stack.size()); // 0
		
		//stack.pop(); // empty stack exception
		
		// -------------------------------------
		// -------------------------------------
	}
	
	
	/**
	 * Performs arithmetic operation on 2 integer values based on
	 * the given operation string.
	 * 
	 * @throws ArithmeticException if trying to divide by zero
	 * @throws IllegalArgumentException if given operation is invalid
	 */
	private static int performOperation(int element1, int element2, String operation) {
		switch (operation) {
		case "+":
			return element1 + element2;
			
		case "-":
			return element1 - element2;
			
		case "/":
			if(element2 == 0) {
				throw new ArithmeticException("Dividing by zero.");
			}
			return element1 / element2;
			
		case "*":
			return element1 * element2;
			
		case "%":
			return element1 % element2;

		default:
			throw new IllegalArgumentException("Given operation is invalid!");
		}
	}
}
