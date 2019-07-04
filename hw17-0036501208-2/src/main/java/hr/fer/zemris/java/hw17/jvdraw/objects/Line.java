package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.editors.LineEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Models a single line.
 *
 * @author Filip Nemec
 */
public class Line extends GeometricalObject {
	
	/** The starting point of the line. */
	private Point start;
	
	/** The ending point of the line. */
	private Point end;
	
	/** The line color. */
	private Color color;
	
	/**
	 * Constructs a new line.
	 *
	 * @param start the starting point
	 * @param end the ending point
	 * @param color the line color
	 */
	public Line(Point start, Point end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new LineEditor(this);
	}
	
	//--------------------------------------------------------------
	//							GETTERS
	//--------------------------------------------------------------

	/**
	 * @return the starting point of the line
	 */
	public Point getStart() {
		return start;
	}
	
	/**
	 * @return the ending point of the line
	 */
	public Point getEnd() {
		return end;
	}
	
	/**
	 * @return the line color
	 */
	public Color getColor() {
		return color;
	}
	
	//--------------------------------------------------------------
	//							SETTERS
	//--------------------------------------------------------------
	
	/**
	 * Sets the new starting point.
	 *
	 * @param start the new starting point
	 */
	public void setStart(Point start) {
		this.start = start;
		notifyListeners();
	}
	
	/**
	 * Sets the new ending point.
	 *
	 * @param end the new ending point
	 */
	public void setEnd(Point end) {
		this.end = end;
		notifyListeners();
	}
	
	/**
	 * Sets the new line color.
	 *
	 * @param color the new line color
	 */
	public void setColor(Color color) {
		this.color = color;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		return "Line (" + start.x + ", " + start.y + ") - (" + end.x + ", " + end.y + ")";
	}
}