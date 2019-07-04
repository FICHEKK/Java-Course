package hr.fer.zemris.java.hw17.jvdraw;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * Models objects that represent the canvas of geometrical
 * objects. These objects offer basic object manipulation
 * and information.
 *
 * @author Filip Nemec
 */
public interface DrawingModel {
	
	/**
	 * @return the number of objects on the drawing mode
	 */
	public int getSize();

	/**
	 * @param index the index of the specified {@code GeometricalObject}
	 * @return the {@code GeometricalObject} at the specified index
	 */
	public GeometricalObject getObject(int index);

	/**
	 * Adds a new {@code GeometricalObject} to this drawing model.
	 *
	 * @param object the object to be added
	 */
	public void add(GeometricalObject object);

	/**
	 * Removes the specified {@code GeometricalObject} from this drawing model.
	 *
	 * @param object the object to be removed
	 */
	public void remove(GeometricalObject object);

	/**
	 * Changes the order of the specified {@code GeometricalObject}.
	 *
	 * @param object the object whose order is being changed
	 * @param offset the offset
	 */
	public void changeOrder(GeometricalObject object, int offset);

	/**
	 * Returns the index of the specified {@code GeometricalObject}. 
	 *
	 * @param object the object being searched for
	 * @return the index of the specified object
	 */
	public int indexOf(GeometricalObject object);

	/**
	 * Clears all of the {@code GeometricalObject} from this drawing model
	 */
	public void clear();

	/**
	 * Sets the {@code modified} flag to {@code false}.
	 */
	public void clearModifiedFlag();

	/**
	 * @return the flag indicating whether the drawing model has been modified
	 */
	public boolean isModified();

	/**
	 * Subscribes a new {@code DrawingModelListener} to this drawing model.
	 *
	 * @param l the listener to be added
	 */
	public void addDrawingModelListener(DrawingModelListener l);

	/**
	 * Removes the specified {@code DrawingModelListener} from this drawing model.
	 *
	 * @param l the listener to be removed
	 */
	public void removeDrawingModelListener(DrawingModelListener l);
}
