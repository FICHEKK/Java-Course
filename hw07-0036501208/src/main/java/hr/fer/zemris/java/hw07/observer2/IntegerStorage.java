package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw07.observer2.IntegerStorageObserver;

/**
 * A simple class that models the subject in the
 * observer design pattern.
 *
 * @author Filip Nemec
 */
public class IntegerStorage {
	
	/** Value this subject holds. */
	private int value;
	
	/** Internal list of all the observers attached to this subject. */
	private List<IntegerStorageObserver> observers;

	/**
	 * Constructs a new subject.
	 *
	 * @param initialValue the initial value of this subject.
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Adds a new observer to this subject.
	 *
	 * @param observer the observer to be added
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if(observers == null) {
			observers = new ArrayList<IntegerStorageObserver>();
		}
		
		observers.add(observer);
	}

	/**
	 * Removes the given observer.
	 *
	 * @param observer the observer to be removed
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		if(observers == null) return;
		observers.remove(observer);
	}

	/**
	 * Removes all the observers from this subject.
	 */
	public void clearObservers() {
		if(observers == null) return;
		observers.clear();
	}

	/**
	 * @return this subject's current value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Changes the state of this subject and notifies
	 * all the attached observers about the change.
	 *
	 * @param value this subject's new value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			if (observers != null) {
				var change = new IntegerStorageChange(this, this.value, value);
				var copy = new ArrayList<IntegerStorageObserver>(observers);
				
				for (IntegerStorageObserver observer : copy) {
					observer.valueChanged(change);
				}
			}
			
			this.value = value;
		}
	}
}
