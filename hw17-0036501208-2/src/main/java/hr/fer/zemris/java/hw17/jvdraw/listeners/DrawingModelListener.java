package hr.fer.zemris.java.hw17.jvdraw.listeners;

import hr.fer.zemris.java.hw17.jvdraw.DrawingModel;

/**
 * Models the listeners of the {@code DrawingModel} object changes.
 *
 * @author Filip Nemec
 */
public interface DrawingModelListener {
	
	/**
	 * Defines the listener's response to the addition of new objects.
	 *
	 * @param source the subject
	 * @param index0 the starting index of the objects added
	 * @param index1 the ending index of the objects added (inclusive)
	 */
	public void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Defines the listener's response to the removal of objects.
	 *
	 * @param source the subject
	 * @param index0 the starting index of the objects added
	 * @param index1 the ending index of the objects added (inclusive)
	 */
	public void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Defines the listener's response to the change of objects.
	 *
	 * @param source the subject
	 * @param index0 the starting index of the objects added
	 * @param index1 the ending index of the objects added (inclusive)
	 */
	public void objectsChanged(DrawingModel source, int index0, int index1);
}