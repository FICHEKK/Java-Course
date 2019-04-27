package hr.fer.zemris.java.hw07.observer2;

/**
 * A simple observer that retrieves the subject's
 * value and outputs that value, but squared.
 *
 * @author Filip Nemec
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange change) {
		int previous = change.getPrevious();
		int current = change.getCurrent();
		int squared = current * current;
		System.out.println("Value was " + previous + ". Provided new value: " + current +", square is " + squared);
	}

}
