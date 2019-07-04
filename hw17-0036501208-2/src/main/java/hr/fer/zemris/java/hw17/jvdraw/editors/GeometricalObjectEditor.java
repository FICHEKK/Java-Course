package hr.fer.zemris.java.hw17.jvdraw.editors;

import javax.swing.JPanel;

/**
 * An abstract model of the geometrical object editor.
 * The editor is used to dynamically change the image
 * on the screen, while offering a simple editing interface.
 *
 * @author Filip Nemec
 */
public abstract class GeometricalObjectEditor extends JPanel {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 2666812558354965135L;

	/**
	 * Checks if the editing was done correctly.
	 */
	public abstract void checkEditing();

	/**
	 * Usually called after {@code #checkEditing()}; accepts the
	 * editing and changes the geometrical object that was being
	 * edited.
	 */
	public abstract void acceptEditing();
}
