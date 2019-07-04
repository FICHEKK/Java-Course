package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.listeners.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Abstract model of a geometrical object.
 *
 * @author Filip Nemec
 */
public abstract class GeometricalObject {
	
	/** A collection of subscribed listeners. */
	protected List<GeometricalObjectListener> listeners = new LinkedList<>();

	/**
	 * The Visitor Design Pattern's accept method.
	 *
	 * @param v the visitor
	 */
	public abstract void accept(GeometricalObjectVisitor v);
	
	/**
	 * @return the appropriate implementation of the {@link GeometricalObjectEditor}   
	 */
	public abstract GeometricalObjectEditor createGeometricalObjectEditor();
	
	/**
	 * Subscribes the new listener.
	 *
	 * @param l the listener to be added
	 */
	public void addGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.add(l);
	}
	
	/**
	 * Unsubscribes the listener.
	 *
	 * @param l the listener to be removed
	 */
	public void removeGeometricalObjectListener(GeometricalObjectListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all of the subscribed listeners of this object's change.
	 */
	protected void notifyListeners() {
		listeners.forEach(l -> l.geometricalObjectChanged(this));
	}
}
