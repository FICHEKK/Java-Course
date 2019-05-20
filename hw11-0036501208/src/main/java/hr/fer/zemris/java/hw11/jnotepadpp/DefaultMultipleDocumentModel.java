package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * The default implementation of the multiple document model.
 *
 * @author Filip Nemec
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;

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
		SingleDocumentModel doc = new DefaultSingleDocumentModel(null, "");
		
//		currentDocument = doc;
		documents.add(doc);
		
		addTab(UNNAMED_TITLE, SAVED_ICON, new JScrollPane(doc.getTextComponent()));
		doc.getTextComponent().grabFocus();
		
		// Switching to the newly opened document.
		int index = documents.size() - 1;
		setToolTipTextAt(index, UNNAMED_TITLE);
		setSelectedIndex(index);
		
		doc.addSingleDocumentListener(new SingleDocumentListener() {
			
			@Override
			public void documentModifyStatusUpdated(SingleDocumentModel model) {
				if(model.isModified()) {
					setIconAt(index, NOT_SAVED_ICON);
				} else {
					setIconAt(index, SAVED_ICON);
				}
			}
			
			@Override
			public void documentFilePathUpdated(SingleDocumentModel model) {
				setTitleAt(getSelectedIndex(), model.getFilePath().getFileName().toString());
				notifyListenersCurrentDocumentChanged(currentDocument, currentDocument);
			}
		});

		return doc;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path, "Path cannot be null");
		
		try {
			String text = Files.readString(path, StandardCharsets.UTF_8);
			SingleDocumentModel doc = new DefaultSingleDocumentModel(path, text);
			
			boolean alreadyOpened = false;
			int index = 0;
			for(SingleDocumentModel document : documents) {
				if(Objects.equals(document.getFilePath(), path)) {
					alreadyOpened = true;
					break;
				}
				index++;
			}
			
			if(alreadyOpened) {
				setSelectedIndex(index);
				return documents.get(index);
				
			} else {
				documents.add(doc);
				
				addTab(path.getFileName().toString(), SAVED_ICON, new JScrollPane(doc.getTextComponent()));
				doc.getTextComponent().grabFocus();
				
				int openIndex = documents.size() - 1;
				setToolTipTextAt(openIndex, path.toAbsolutePath().toString());
				setSelectedIndex(openIndex);
				
				doc.addSingleDocumentListener(new SingleDocumentListener() {
					
					@Override
					public void documentModifyStatusUpdated(SingleDocumentModel model) {
						if(model.isModified()) {
							setIconAt(openIndex, NOT_SAVED_ICON);
						} else {
							setIconAt(openIndex, SAVED_ICON);
						}
					}
					
					@Override
					public void documentFilePathUpdated(SingleDocumentModel model) {
						setTitleAt(getSelectedIndex(), model.getFilePath().getFileName().toString());
						notifyListenersCurrentDocumentChanged(currentDocument, currentDocument);
					}
				});
				
				return doc;	
			}
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
										  "Error during the reading of the file. File was not opened.",
										  "Error",
										  JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	//---------------------------------------------------------------------
	//							  SAVING	
	//---------------------------------------------------------------------

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		// If we are saving the current document, that is fine.
		// However, if we are trying to save to a file that is already
		// opened, we should block it.
		if(!Objects.equals(currentDocument.getFilePath(), newPath)) {
			
			// Check if the save destination is already an opened file.
			if(newPath != null) {
				for(SingleDocumentModel doc : documents) {
					if(Objects.equals(doc.getFilePath(), newPath)) {
						JOptionPane.showMessageDialog(this,
									"Cannot save as the specified file is already opened!",
									"Error",
									JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
		}
		
		Path dest = (newPath == null) ? model.getFilePath() : newPath;
		
		if(dest == null) {
			JFileChooser jfc = new JFileChooser("C:\\Users\\FICHEKK\\Desktop");
			jfc.setDialogTitle("Save to...");
			
			if(jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			dest = jfc.getSelectedFile().toPath();
		}
		
		try {
			Files.writeString(dest, model.getTextComponent().getText());
			
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "File saving error occured.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		model.setFilePath(dest);
		model.setModified(false);
		
		JOptionPane.showMessageDialog(this, "File saved successfully.");
	}
	
	//---------------------------------------------------------------------
	//							  CLOSING	
	//---------------------------------------------------------------------

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int index = documents.indexOf(model);
		
		if(index < 0) return;

		documents.remove(index);
		removeTabAt(index);
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
	 * Helper method that creates a new document with the given path,
	 * tab title and document text.
	 *
	 * @param path the file path, can be null
	 * @param title the tab title
	 * @param text the document text
	 * @return the reference to the newly created document model
	 */
//	private SingleDocumentModel createNewDocument(Path path, String title, String text) {
//		SingleDocumentModel doc = new DefaultSingleDocumentModel(path, text);
//		
//		setCurrentDocument(doc);
//		addDocument(doc);
//		
//		addTab(title, SAVED_ICON, new JScrollPane(doc.getTextComponent()));
//		doc.getTextComponent().grabFocus();
//		
//		int index = getTabCount() - 1;
//		setToolTipTextAt(index, title);
//		setSelectedIndex(index);
//		
//		doc.addSingleDocumentListener(new SingleDocumentListener() {
//			
//			@Override
//			public void documentModifyStatusUpdated(SingleDocumentModel model) {
//				if(model.isModified()) {
//					setIconAt(index, NOT_SAVED_ICON);
//				}
//			}
//			
//			@Override
//			public void documentFilePathUpdated(SingleDocumentModel model) {
//				// TODO Auto-generated method stub
//			}
//		});
//		
//		return doc;
//	}
	
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
