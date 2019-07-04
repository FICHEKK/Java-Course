package hr.fer.zemris.java.hw17.jvdraw.objects;

import java.awt.Color;
import java.awt.Point;

import hr.fer.zemris.java.hw17.jvdraw.editors.FilledCircleEditor;
import hr.fer.zemris.java.hw17.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectVisitor;

/**
 * Models a special kind of circle - the filled circle.
 *
 * @author Filip Nemec
 */
public class FilledCircle extends Circle {
	
	/** The color of the circle's area. */
	private Color areaColor;
	
	/**
	 * Constructs a new filled circle. 
	 *
	 * @param center the center point
	 * @param radius the radius
	 * @param outlineColor the outline color
	 * @param areaColor the area color
	 */
	public FilledCircle(Point center, int radius, Color outlineColor, Color areaColor) {
		super(center, radius, outlineColor);
		this.areaColor = areaColor;
	}

	@Override
	public void accept(GeometricalObjectVisitor v) {
		v.visit(this);
	}

	@Override
	public GeometricalObjectEditor createGeometricalObjectEditor() {
		return new FilledCircleEditor(this);
	}
	
	//--------------------------------------------------------------
	//							GETTERS
	//--------------------------------------------------------------
	
	/**
	 * @return the circle's area color
	 */
	public Color getAreaColor() {
		return areaColor;
	}
	
	//--------------------------------------------------------------
	//							SETTERS
	//--------------------------------------------------------------
	
	/**
	 * Sets the new area color for this circle.
	 *
	 * @param areaColor the new area color
	 */
	public void setAreaColor(Color areaColor) {
		this.areaColor = areaColor;
		notifyListeners();
	}
	
	@Override
	public String toString() {
		int r = areaColor.getRed();
		int g = areaColor.getGreen();
		int b = areaColor.getBlue();
		return "Filled circle (" + center.x + ", " + center.y + "), " + radius + ", #" + String.format("%02x%02x%02x", r, g, b);
	}
}