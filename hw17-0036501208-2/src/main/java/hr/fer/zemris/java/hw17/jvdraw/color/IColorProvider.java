package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.listeners.ColorChangeListener;

/**
 * Models objects that can access and provide a color.
 *
 * @author Filip Nemec
 */
public interface IColorProvider {
	
	/**
	 * @return the current color
	 */
	public Color getCurrentColor();

	/**
	 * Adds a new color change listener.
	 *
	 * @param l the new listener
	 */
	public void addColorChangeListener(ColorChangeListener l);

	/**
	 * Removes the specified color change listener.
	 *
	 * @param l the listener to be removed
	 */
	public void removeColorChangeListener(ColorChangeListener l);
}