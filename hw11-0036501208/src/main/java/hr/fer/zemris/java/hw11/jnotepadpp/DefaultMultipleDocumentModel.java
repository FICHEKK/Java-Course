package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * The default implementation of the multiple document model.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

	/** The icon that displays that the document is saved. */
	private static final ImageIcon SAVED_ICON = loadIcon("icons/green_floppy_disk.png");
	
	/** The icon that displays that the document is not saved. */
	private static final ImageIcon NOT_SAVED_ICON = loadIcon("icons/red_floppy_disk.png");
	
	/** The default tab title for the new documents. */
	private static final String UNNAMED_TITLE = "(unnamed)";
	
	/** A list of documents that this model tracks. */
	private List<SingleDocumentModel> documents = new LinkedList<>();
	
	/** Reference to the current document. */
	private SingleDocumentModel currentDocument;
	
	/** A list of listeners for this multi-document model. */
	private List<MultipleDocumentListener> listeners = new LinkedList<>();
	
	//---------------------------------------------------------------------
	//						  	CONSTRUCTOR	
	//---------------------------------------------------------------------
	
	/**
	 * Constructs a new tabbed pane multi-document model and adds a listener
	 * that tracks if the current tab changed.
	 */
	public DefaultMultipleDocumentModel() {
		addChangeListener(e -> {
			SingleDocumentModel prevCurrentDocument = currentDocument;
			currentDocument = documents.isEmpty() ? null : documents.get(getSelectedIndex());
			
			notifyListenersCurrentDocumentChanged(prevCurrentDocument, currentDocument);
		});
	}
	
	//---------------------------------------------------------------------
	//						  OPENING DOCUMENTS	
	//---------------------------------------------------------------------

	@Override
	public SingleDocumentModel createNewDocument() {
		return openDocument(null, UNNAMED_TITLE, "");
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) throws IOException {
		int index = documentAlreadyOpened(path);
		
		if(index >= 0) {
			setSelectedIndex(index);
			return documents.get(index);
			
		} else {
			String text = Files.readString(path, StandardCharsets.UTF_8);
			return openDocument(path, path.getFileName().toString(), text);
			
		}
	}
	
	/**
	 * Helper method that opens and sets up the newly opened
	 * document.
	 *
	 * @param path the path of the opened document
	 * @param title the title of the tab that document will reside in
	 * @param text the text of the document
	 * @return the reference to the newly created document
	 */
	private SingleDocumentModel openDocument(Path path, String title, String text) {
		SingleDocumentModel doc = new DefaultSingleDocumentModel(path, text);
		documents.add(doc);
		
		// Create new tab and set focus to opened document.
		addTab(title, SAVED_ICON, new JScrollPane(doc.getTextComponent()));
		doc.getTextComponent().grabFocus();
		
		// Switching to the newly opened document.
		int index = documents.size() - 1;
		setToolTipTextAt(index, path == null ? UNNAMED_TITLE : path.toAbsolutePath().toString());
		setSelectedIndex(index);
		
		// Add document listener
		doc.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				setIconAt(documents.indexOf(doc), model.isModified() ? NOT_SAVED_ICON : SAVED_ICON);
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setTitleAt(getSelectedIndex(), model.getFilePath().getFileName().toString());
				notifyListenersCurrentDocumentChanged(currentDocument, currentDocument);
			}
		});
		
		// Notify listeners that a new document was added.
		notifyListenersDocumentAdded(doc);
		
		return doc;
	}
	
	//---------------------------------------------------------------------
	//							  SAVING	
	//---------------------------------------------------------------------

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) throws IOException, FileAlreadyExistsException {
		// If we are saving the current document, that is fine.
		// However, if we are trying to save to a file that is already
		// opened, we should block it.
		if(!Objects.equals(currentDocument.getFilePath(), newPath)) {
			
			// Check if the save destination is already an opened file.
			if(documentAlreadyOpened(newPath) >= 0)
				throw new FileAlreadyExistsException("Document is already opened.");
		}
		
		Path dest = (newPath == null) ? model.getFilePath() : newPath;
		
		Files.writeString(dest, model.getTextComponent().getText());
		
		model.setFilePath(dest);
		model.setModified(false);
	}
	
	//---------------------------------------------------------------------
	//							  CLOSING	
	//---------------------------------------------------------------------

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		
		if(index < 0) return;

		SingleDocumentModel removed = documents.remove(index);
		removeTabAt(index);
		notifyListenersDocumentRemoved(removed);
	}
	
	//---------------------------------------------------------------------
	//							  GETTERS	
	//---------------------------------------------------------------------

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}
	
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}
	
	//---------------------------------------------------------------------
	//							HELPER METHODS	
	//---------------------------------------------------------------------
	
	/**
	 * Loads the icon as a resource for the given path.
	 *
	 * @param path the path
	 * @return an {@code ImageIcon} for the given path, or {@code null}
	 * 		   if the icon was not found
	 */
	private static ImageIcon loadIcon(String path) {
		InputStream is = JNotepadPP.class.getResourceAsStream(path);
		
		if(is == null)
			throw new IllegalArgumentException("Resource was not found.");
			
		byte[] bytes = null;
		
		try {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ImageIcon(bytes);
	}
	
	/**
	 * Checks if the document for the given path is already
	 * opened. If it is, this method will return the document tab
	 * index, otherwise will return {@code -1}.
	 *
	 * @param path the path to be evaluated
	 * @return if there is an opened document the given path - returns
	 * 		   the index of the document tab, {@code -1} otherwise 
	 */
	private int documentAlreadyOpened(Path path) {
		if(path == null) return -1;
		
		int index = 0;
		for(SingleDocumentModel document : documents) {
			if(Objects.equals(document.getFilePath(), path))
				return index;

			index++;
		}
		
		return -1;
	}
	
	//---------------------------------------------------------------------
	//							  LISTENERS	
	//---------------------------------------------------------------------

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		if(listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(l);
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}
	
	//---------------------------------------------------------------------
	//						NOTIFYING LISTENERS
	//---------------------------------------------------------------------
	
	/**
	 * Notifies all of the subscribed listeners that the current document
	 * has been changed.
	 *
	 * @param prev the previous document
	 * @param current the new document
	 */
	private void notifyListenersCurrentDocumentChanged(SingleDocumentModel prev, SingleDocumentModel current) {
		listeners.forEach(l -> l.currentDocumentChanged(prev, current));
	}
	
	/**
	 * Notifies all of the subscribed listeners that a new document
	 * has been added.
	 *
	 * @param added the new (added) document
	 */
	private void notifyListenersDocumentAdded(SingleDocumentModel added) {
		listeners.forEach(l -> l.documentAdded(added));
	}
	
	/**
	 * Notifies all of the subscribed listeners that a document
	 * has been removed.
	 *
	 * @param added the old (removed) document
	 */
	private void notifyListenersDocumentRemoved(SingleDocumentModel removed) {
		listeners.forEach(l -> l.documentRemoved(removed));
	}
	
	//---------------------------------------------------------------------
	//							ITERATOR
	//---------------------------------------------------------------------
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return documents.iterator();
	}
}
