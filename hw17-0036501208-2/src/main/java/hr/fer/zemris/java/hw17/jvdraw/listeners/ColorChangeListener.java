package hr.fer.zemris.java.hw17.jvdraw.listeners;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.color.IColorProvider;

/**
 * Defines observers for the change of color.
 *
 * @author Filip Nemec
 */
public interface ColorChangeListener {
	
	/**
	 * Performed once the subject's color changes.
	 *
	 * @param source the source of the color
	 * @param oldColor the old color
	 * @param newColor the new color
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}