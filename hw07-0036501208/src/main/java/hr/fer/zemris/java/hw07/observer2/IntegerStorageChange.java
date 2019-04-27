package hr.fer.zemris.java.hw07.observer2;

/**
 * Models a simple object that encapsulates
 * all the changes that were made between
 * the previous and current state of the
 * given subject.
 *
 * @author Filip Nemec
 */
public class IntegerStorageChange {
	
	/** The subject. */
	private IntegerStorage storage;
	
	/** Previous value of the subject. */
	private int previous;
	
	/** Current value of the subject. */
	private int current;
	
	/**
	 * Constructs a new change of the subject.
	 *
	 * @param storage the subject
	 * @param previous subject's previous value
	 * @param current subject's current value
	 */
	public IntegerStorageChange(IntegerStorage storage, int previous, int current) {
		this.storage = storage;
		this.previous = previous;
		this.current = current;
	}
	
	/**
	 * @return returns the subject's previous value
	 */
	public int getPrevious() {
		return previous;
	}
	
	/**
	 * @return returns the subject's current value
	 */
	public int getCurrent() {
		return current;
	}
	
	/**
	 * @return returns the subject
	 */
	public IntegerStorage getStorage() {
		return storage;
	}
}
