package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Models objects that listen for the {@code SingleDocumentModel}
 * changes.
 *
 * @author Filip Nemec
 */
public interface SingleDocumentListener {
	
	/**
	 * Defines what will happen once the "modify" status
	 * gets changed.
	 *
	 * @param model the reference to the document that is
	 * 		  being listened to
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Defines what will happen once the file path gets
	 * updated.
	 *
	 * @param model the reference to the document that is
	 * 		  being listened to
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
