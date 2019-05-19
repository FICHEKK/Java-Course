package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Models objects that listen for the {@code MultipleDocumentModel}
 * changes.
 *
 * @author Filip Nemec
 */
public interface MultipleDocumentListener {
	
	/**
	 * Defines what will happen every time a current
	 * document changes.
	 *
	 * @param previousModel the previous model
	 * @param currentModel the current model
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	
	/**
	 * Defines what will happen once a new document
	 * is added.
	 *
	 * @param model the reference to the new document
	 */
	void documentAdded(SingleDocumentModel model);
	
	/**
	 * Defines what will happen once a document gets
	 * removed.
	 *
	 * @param model the reference to the removing document
	 */
	void documentRemoved(SingleDocumentModel model);
}
