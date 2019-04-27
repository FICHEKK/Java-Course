package hr.fer.zemris.java.hw07.observer2;

/**
 * A simple observer that retrieves the subject's
 * value and outputs the that value, but doubled.
 * <p>
 * It also provides a self-destruction counter, meaning
 * after the given number of state changes, this
 * observer will remove itself from the subject's
 * list of observers.
 *
 * @author Filip Nemec
 */
public class DoubleValue implements IntegerStorageObserver {
	
	/** The self-removal counter. */
	private int counter;
	
	/**
	 * Constructs a new double value observer.
	 *
	 * @param counter
	 */
	public DoubleValue(int counter) {
		this.counter = counter;
	}

	@Override
	public void valueChanged(IntegerStorageChange change) {
		if(counter > 0) {
			System.out.println("Double value: " + change.getCurrent() * 2);
			counter--;
		} else {
			System.out.println("Double value observer shutting down.");
			change.getStorage().removeObserver(this);
		}
	}

}
