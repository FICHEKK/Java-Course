package hr.fer.zemris.java.hw17.jvdraw.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Defines a single state in the State Design Pattern.
 * This state models a tool and will change the drawing behavior
 * based on the player's current {@code Tool} selection.
 *
 * @author Filip Nemec
 */
public interface Tool {
	
	/**
	 * Defines the action for the mouse press.
	 *
	 * @param e the mouse event
	 */
	public void mousePressed(MouseEvent e);

	/**
	 * Defines the action for the mouse release.
	 *
	 * @param e the mouse event
	 */
	public void mouseReleased(MouseEvent e);

	/**
	 * Defines the action for the mouse click.
	 *
	 * @param e the mouse event
	 */
	public void mouseClicked(MouseEvent e);

	/**
	 * Defines the action for the mouse move.
	 *
	 * @param e the mouse event
	 */
	public void mouseMoved(MouseEvent e);

	/**
	 * Defines the action for the mouse drag.
	 *
	 * @param e the mouse event
	 */
	public void mouseDragged(MouseEvent e);

	/**
	 * Defines the painting process for this tool.
	 *
	 * @param g2d the graphics object
	 */
	public void paint(Graphics2D g2d);
}