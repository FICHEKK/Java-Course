package hr.fer.zemris.java.hw17.jvdraw.listeners;

import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * Models observers which track the geometrical object
 * changes.
 *
 * @author Filip Nemec
 */
public interface GeometricalObjectListener {
	
	/**
	 * Defines action upon geometrical object change.
	 *
	 * @param o the subject; the object that changed
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}
