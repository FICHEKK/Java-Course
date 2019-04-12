package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Program that calculates the area and perimeter of a rectangle and displays
 * it in the console.
 * 
 * Width and height parameters can be given through main method arguments
 * or through the console.
 */
public class Rectangle {
	/**
	 * Method that gathers rectangle width and height, then displays
	 * it in the console.
	 * 
	 * @param args width and height of a rectangle.
	 */
	public static void main(String[] args) {
		if(args.length != 0 && args.length != 2) {
			System.err.println("Broj argumenata nije valjan! Izlazim...");
			return;
		}
		
		double width, height;
		
		if(args.length == 2) {			
			try {
				width = Double.parseDouble(args[0]);
				height = Double.parseDouble(args[1]);
			} catch(NumberFormatException ex) {
				System.err.println("Dani argumenti nisu tipa double! Izlazim...");
				return;
			}
		} else {
			Scanner scanner = new Scanner(System.in);
			
			width = getPositiveDoubleValue(scanner, "širinu");
			height = getPositiveDoubleValue(scanner, "visinu");
			
			scanner.close();
		}
		
		printAreaAndPerimeter(width, height);
	}
	
	/**
	 * Method which asks the user to enter a valid positive double value.
	 * 
	 * @param valueName String representation of desired user input.
	 * @return positive double value.
	 */
	private static double getPositiveDoubleValue(Scanner scanner, String valueName) {
		double value;
		
		while(true) {
			System.out.println("Unesite " + valueName +" > ");
			
			String input = scanner.next();
			
			try {
				value = Double.parseDouble(input);
				
				if(value < 0) {
					System.out.println("Unijeli ste negativnu vrijednost.");
					continue;
				} else {
					break;
				}
			} catch(NumberFormatException ex) {
				System.out.println("'" + input + "' se ne može protumačiti kao broj.");
				continue;
			}
		}
		
		return value;
	}

	/**
	 * Calculates the area of a rectangle of specified width and height.
	 * 
	 * @param width of a rectangle.
	 * @param height of a rectangle.
	 * @return area of a rectangle of given width and height.
	 * @throws IllegalArgumentException if width or height is not positive.
	 */
	public static double area(double width, double height) {
		if(width <= 0) {
			throw new IllegalArgumentException("Width must be a positive value!");
		}
		
		if(height <= 0) {
			throw new IllegalArgumentException("Height must be a positive value!");
		}
		
		return width * height;
	}
	
	
	/**
	 * Calculates the perimeter of a rectangle of specified width and height.
	 * 
	 * @param width of a rectangle.
	 * @param height of a rectangle.
	 * @return perimeter of a rectangle of given width and height.
	 * @throws IllegalArgumentException if width or height is not positive.
	 */
	public static double perimeter(double width, double height) {
		if(width <= 0) {
			throw new IllegalArgumentException("Width must be a positive value!");
		}
		
		if(height <= 0) {
			throw new IllegalArgumentException("Height must be a positive value!");
		}
		
		return 2 * (width + height);
	}
	
	/**
	 * Prints area and perimeter of a rectangle of given width and height in
	 * the console.
	 * 
	 * @param width of a rectangle
	 * @param height of a rectangle
	 */
	public static void printAreaAndPerimeter(double width, double height) {
		System.out.println("Pravokutnik širine " + width + " i visine " 
				+ height + " ima površinu " + area(width, height)
				+ " te opseg " + perimeter(width, height) + ".");
	}
}
