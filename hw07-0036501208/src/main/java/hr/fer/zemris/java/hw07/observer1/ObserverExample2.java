package hr.fer.zemris.java.hw07.observer1;

/**
 * A simple demo program that shows the workings
 * of observer design pattern.
 *
 * @author Filip Nemec
 */
public class ObserverExample2 {
	
	/**
	 * The program starts from here.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		
		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		
		istorage.removeObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(1));
		istorage.addObserver(new DoubleValue(2));
		istorage.addObserver(new DoubleValue(2));
		
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
	}
}
