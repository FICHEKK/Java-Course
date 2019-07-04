package hr.fer.zemris.java.hw17.jvdraw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw17.jvdraw.listeners.DrawingModelListener;
import hr.fer.zemris.java.hw17.jvdraw.listeners.GeometricalObjectListener;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;

/**
 * The implementation of the {@code DrawingModel}.
 *
 * @author Filip Nemec
 */
public class DrawingModelImpl implements DrawingModel, GeometricalObjectListener {
	
	/** Flag indicating if this drawing canvas has been modified. */
	private boolean modified;
	
	/** A list of listeners for this drawing canvas. */
	private List<DrawingModelListener> listeners = new LinkedList<>();
	
	/** A collection of geometrical objects. */
	private List<GeometricalObject> objects = new ArrayList<>(16);
	
	//-------------------------------------------------------------
	//						Public API
	//-------------------------------------------------------------
	
	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		if(object == null) return;
		if(objects.contains(object)) return;
		
		object.addGeometricalObjectListener(this);
		objects.add(object);
		modified = true;
		
		int i = objects.size() - 1;
		notifyListenersObjectsAdded(i, i);
	}

	@Override
	public void remove(GeometricalObject object) {
		if(object == null) return;
		
		int i = objects.indexOf(object);
		if(i < 0) return;
		
		object.removeGeometricalObjectListener(this);
		objects.remove(object);
		modified = true;
		
		notifyListenersObjectsRemoved(i, i);
	}

	@Override
	public void changeOrder(GeometricalObject object, int offset) {
		if(offset == 0) return;
		
		// The index 1 - initial position.
		int i1 = indexOf(object);
		if(i1 < 0) return;
		
		// The index 2 - final position.
		int i2 = i1 + offset;
		if(i2 < 0 || i2 >= objects.size()) return;

		GeometricalObject o = objects.remove(i1);
		objects.add(i2, o);
		
		if(i1 < i2) {
			notifyListenersObjectsChanged(i1, i2);
		} else {
			notifyListenersObjectsChanged(i2, i1);
		}
	}

	@Override
	public int indexOf(GeometricalObject object) {
		return objects.indexOf(object);
	}

	@Override
	public void clear() {
		if(objects.isEmpty()) return;
		
		objects.forEach(o -> o.removeGeometricalObjectListener(this));
		objects.clear();
		modified = true;
		
		notifyListenersObjectsRemoved(0, 0);
	}

	@Override
	public void clearModifiedFlag() {
		modified = false;
	}

	@Override
	public boolean isModified() {
		return modified;
	}
	
	//-------------------------------------------------------------
	//				GeometricalObject listener method
	//-------------------------------------------------------------

	@Override
	public void geometricalObjectChanged(GeometricalObject o) {
		int i = indexOf(o);
		notifyListenersObjectsChanged(i, i);
	}
	
	//-------------------------------------------------------------
	//						  Listeners
	//-------------------------------------------------------------
	
	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies all of the listeners that the new objects have been added.
	 *
	 * @param index0 the index of the first addition
	 * @param index1 the index of the last addition
	 */
	private void notifyListenersObjectsAdded(int index0, int index1) {
		listeners.forEach(l -> l.objectsAdded(this, index0, index1));
	}
	
	/**
	 * Notifies all of the listeners that some objects have been removed.
	 *
	 * @param index0 the index of the first removed object
	 * @param index1 the index of the last removed object
	 */
	private void notifyListenersObjectsRemoved(int index0, int index1) {
		listeners.forEach(l -> l.objectsRemoved(this, index0, index1));
	}
	
	/**
	 * Notifies all of the listeners that some objects have been changed.
	 *
	 * @param index0 the index of the first changed object
	 * @param index1 the index of the last changed object
	 */
	private void notifyListenersObjectsChanged(int index0, int index1) {
		listeners.forEach(l -> l.objectsChanged(this, index0, index1));
	}
}