package hr.fer.zemris.java.hw07.observer1;

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
	 * @param istorage the subject
	 */
	public void valueChanged(IntegerStorage istorage);
}
