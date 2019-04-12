package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that let's user input whole numbers which results in factorial of those
 * numbers being displayed in the console.
 * 
 * In case of an invalid input, such as number out of range or invalid
 * data type, user is notified with a message and can input another number.
 * 
 * User can finish the program at any time using the keyword 'kraj'.
 * 
 */
public class Factorial {
	/** 
	 *  Maximum input for a type long factorial method. Any input bigger 
	 *  than this number would result in an overflow.
	 */
	private static final int MAX_LONG_FACTORIAL_INPUT = 20;
	
	/**
	 * Method that gathers a value, then displays the factorial of that value
	 * in the console.
	 * 
	 * @param args none of the arguments will be used in the calculation.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Unesite broj > ");
			
			String input = scanner.next();
			
			try {
				long number = Long.parseLong(input);
				
				if(number < 3 || number > 20) {
					System.out.println("'" + number + "' nije u dozvoljenom rasponu.");
					continue;
				}
				
				System.out.println(number + "! = " + factorial(number));
			} catch(NumberFormatException ex) {
				if(input.equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				}
				
				System.out.println("'" + input + "' nije cijeli broj.");
			}
		}
		
		scanner.close();
	}
	
	/**
	 * Calculates factorial of a given number.
	 * 
	 * @param number number of which factorial is being calculated.
	 * 		  Should be value between 0 and 20, inclusive.
	 * @return calculated factorial of a given number.
	 * @throws IllegalArgumentException in case that input is too big or negative.
	 */
	public static long factorial(long number) {
		if(number > MAX_LONG_FACTORIAL_INPUT) {
			throw new IllegalArgumentException("Input too big! Maximum value of parameter is "
												+ MAX_LONG_FACTORIAL_INPUT);
		}
		
		if(number < 0) {
			throw new IllegalArgumentException("Input should be a positive value!");
		}
		
		if(number == 1 || number == 0) {
			return 1;
		}
		
		long result = 1;
		while(number > 1) {
			result *= number;
			number--;
		}
		
		return result;
	}
}
