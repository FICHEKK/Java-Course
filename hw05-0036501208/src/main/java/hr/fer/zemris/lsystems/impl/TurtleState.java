package hr.fer.zemris.lsystems.impl;

import java.awt.Color;

import hr.fer.zemris.math.Vector2D;

/**
 * A simple class that encapsulates the
 * following turtle's properties: position,
 * direction, trail color and step length.
 * 
 * @author Filip Nemec
 */
public class TurtleState {
	
	/**
	 * Turtle's current position.
	 */
	private Vector2D position;
	
	/**
	 * Turtle's current direction.
	 */
	private Vector2D direction;
	
	/**
	 * Turtle's current trail color.
	 */
	private Color color;
	
	/**
	 * Turtle's step length.
	 */
	private double step;
	
	/**
	 * Constructs a new turtle state.
	 * 
	 * @param position turtle position
	 * @param direction turtle direction
	 * @param color turtle trail color
	 * @param step turtle's step length
	 */
	public TurtleState(Vector2D position, Vector2D direction, Color color, double step) {
		this.position = position;
		this.direction = direction;
		this.color = color;
		this.step = step;
	}
	
	/**
	 * Recreates an exact copy of this turtle state and returns it.
	 * 
	 * @return copy of this turtle state
	 */
	public TurtleState copy() {
		Color copy = new Color(color.getRed(), color.getGreen(), color.getBlue());
		return new TurtleState(position.copy(), direction.copy(), copy, step);
	}
	
	//----------------------------------------------------------
	//                   GETTERS AND SETTERS                    
	//----------------------------------------------------------
	
	/**
	 * Gets the current position vector.
	 * @return the current position vector
	 */
	public Vector2D getPosition() {
		return position;
	}
	
	/**
	 * Sets the position to the given value.
	 * @param position new position
	 */
	public void setPosition(Vector2D position) {
		this.position = position;
	}
	
	/**
	 * Gets the current direction vector.
	 * @return the current direction vector
	 */
	public Vector2D getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction to the given value.
	 * @param direction new direction
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}
	
	/**
	 * Gets the current color.
	 * @return the current color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color to the given value.
	 * @param color new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Gets the current step length.
	 * @return the current step length
	 */
	public double getStep() {
		return step;
	}
	
	/**
	 * Sets the step length to the given value.
	 * @param step new step length
	 */
	public void setStep(double step) {
		this.step = step;
	}
}
