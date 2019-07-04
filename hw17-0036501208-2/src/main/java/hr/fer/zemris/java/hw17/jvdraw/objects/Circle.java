package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editors.CircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Models an unfilled circle.
 *
 * @author Filip Nemec
 */
public class Circle extends GeometricalObject {
	
	/** The center point. */
	protected Point center;
	
	/** The radius of this circle. */
	protected int radius;
	
	/** The circle outline color. */
	protected Color outlineColor;
	
	/**
	 * Constructs a new circle.
	 *
	 * @param center the center point of the circle
	 * @param radius the radius of the circle
	 * @param outlineColor the circle outline color
	 */
	public Circle(Point center, int radius, Color outlineColor) {
		this.center = center;
		this.radius = radius;
		this.outlineColor = outlineColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new CircleEditor(this);
	}
	
	//--------------------------------------------------------------
	//							GETTERS
	//--------------------------------------------------------------
	
	/**
	 * @return the circle's center point
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * @return the circle's radius
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * @return the circle's outline color
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}
	
	//--------------------------------------------------------------
	//							SETTERS
	//--------------------------------------------------------------
	
	/**
	 * Sets the new center point of the circle.
	 *
	 * @param center the new center point of the circle
	 */
	public void setCenter(Point center) {
		this.center = center;
		notifyListeners();
	}
	/**
	 * Sets the new radius of the circle.
	 *
	 * @param radius the new radius of the circle
	 */
	public void setRadius(int radius) {
		this.radius = radius;
		notifyListeners();
	}
	
	/**
	 * Sets the new outline color for the circle.
	 *
	 * @param outlineColor the new outline color of the circle
	 */
	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		return "Circle (" + center.x + ", " + center.y + "), " + radius;
	}
}