package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

/**
 * Models objects which can hold multiple documents.
 *
 * @author Filip Nemec
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Creates a new document and returns the reference.
	 *
	 * @return the reference to the newly created document
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Returns the current document.
	 *
	 * @return the current document
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Load the document from the given path and returns the
	 * reference.
	 *
	 * @param path the path, must not be null
	 * @return the reference to the loaded document
	 * @throws IOException if file loading failed
	 */
	SingleDocumentModel loadDocument(Path path) throws IOException;
	
	/**
	 * Saves the document to the given path.
	 *
	 * @param model the document to be saved
	 * @param newPath the save destination path
	 * @throws IOException if file saving failed
	 * @throws FileAlreadyExistsException if a document is already opened
	 */
	void saveDocument(SingleDocumentModel model, Path newPath) throws IOException, FileAlreadyExistsException;
	
	/**
	 * Closes the specified document.
	 *
	 * @param model the document to be closed
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Adds a new listener to this model.
	 *
	 * @param l the listener
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Removes the specified listener from this model.
	 *
	 * @param l the listener to be removed
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Returns the number of document that this model currently holds.
	 *
	 * @return the number of document that this model currently holds
	 */
	int getNumberOfDocuments();
	
	/**
	 * Returns the document on the given index.
	 *
	 * @param index the index
	 * @return the document on the given index
	 */
	SingleDocumentModel getDocument(int index);
}
