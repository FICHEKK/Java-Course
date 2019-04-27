package hr.fer.zemris.java.hw07.observer2;

/**
 * Models the observers for the integer storage
 * subject.
 *
 * @author Filip Nemec
 */
public interface IntegerStorageObserver {
	
	/**
	 * The method called every time the subject's
	 * value is being changed.
	 *
	 * @param change the reference that holds all the changes that were made
	 */
	public void valueChanged(IntegerStorageChange change);
}
