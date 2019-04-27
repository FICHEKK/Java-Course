package hr.fer.zemris.java.hw07.observer1;

/**
 * A simple observer that retrieves the subject's
 * value and outputs that value, but squared.
 *
 * @author Filip Nemec
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorage istorage) {
		int value = istorage.getValue();
		int squared = value * value;
		System.out.println("Provided new value: " + value +", square is " + squared);
	}

}
