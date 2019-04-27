package hr.fer.zemris.java.hw07.observer1;

/**
 * A simple observer that tracks the number of changes
 * that were made since this observer was added to the
 * subject.
 *
 * @author Filip Nemec
 */
public class ChangeCounter implements IntegerStorageObserver {
	
	/** The counter of changes. */
	private int counter;

	@Override
	public void valueChanged(IntegerStorage istorage) {
		counter++;
		System.out.println("Number of value changes since tracking: " + counter);
	}
}
