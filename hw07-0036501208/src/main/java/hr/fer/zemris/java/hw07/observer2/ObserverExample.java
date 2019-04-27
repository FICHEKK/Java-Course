package hr.fer.zemris.java.hw07.observer2;

/**
 * A simple demo program that shows the workings
 * of observer design pattern.
 *
 * @author Filip Nemec
 */
public class ObserverExample {
	
	/**
	 * The program starts from here.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		
		istorage.addObserver(new SquareValue());
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(2));
		
		changeValue(istorage, 5);
		changeValue(istorage, 2);
		changeValue(istorage, 25);
		changeValue(istorage, 7);
		changeValue(istorage, 10);
	}
	
	/**
	 * Changes the given subject's value and prints a new line
	 * for visibility.
	 *
	 * @param istorage
	 * @param value
	 */
	private static void changeValue(IntegerStorage istorage, int value) {
		istorage.setValue(value);
		System.out.println();
	}
}
