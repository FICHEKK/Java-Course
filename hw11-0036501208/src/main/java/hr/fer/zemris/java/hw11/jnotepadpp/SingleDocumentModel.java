package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Models object that represent a single document.
 *
 * @author Filip Nemec
 */
public interface SingleDocumentModel {
	
	/**
	 * Returns the {@code JTextArea} that this document is in
	 *
	 * @return the {@code JTextArea} that this document is in
	 */
	JTextArea getTextComponent();
	
	/**
	 * Returns the path of this document. This value can be null if
	 * no path is associated with this document.
	 *
	 * @return the path of this document
	 */
	Path getFilePath();
	
	/**
	 * Sets the path of this document.
	 *
	 * @param path the path
	 */
	void setFilePath(Path path);
	
	/**
	 * Returns {@code true} if this document is modified, {@code false}
	 * otherwise.
	 *
	 * @return {@code true} if this document is modified, {@code false}
	 * otherwise
	 */
	boolean isModified();
	
	/**
	 * Sets the "modified" flag to the given value.
	 *
	 * @param modified the new value of the "modified" flag
	 */
	void setModified(boolean modified);
	
	/**
	 * Adds a new listener of this document.
	 *
	 * @param l the listener
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Removes the specified listener.
	 *
	 * @param l the listener to be removed
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
