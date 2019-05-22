package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Models a basic document model.
 *
 * @author Filip Nemec
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	/** The list of listeners subscribed to this document. */
	private List<SingleDocumentListener> listeners;
	
	/** Flag that says if this document has been modified. */
	private boolean modified;
	
	/** The path connected to this document. */
	private Path path;
	
	/** The text area connected to this document. */
	private JTextArea textArea;
	
	//------------------------------------------------------------------
	//							CONSTRUCTOR
	//------------------------------------------------------------------
	
	/**
	 * Constructs a new document model with the given path and text.
	 *
	 * @param path the path that this document is connected to
	 * @param text the text that this document should hold
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.path = path;
		this.textArea = new JTextArea(text);
		
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setModified(true);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}
	
	//------------------------------------------------------------------
	//							  SETTERS
	//------------------------------------------------------------------
	
	@Override
	public void setFilePath(Path path) {
		if(!Objects.equals(this.path, path)) {
			this.path = path;
			notifyListenersFilePathChanged();
		}
	}

	@Override
	public void setModified(boolean modified) {
		if(this.modified != modified) {
			this.modified = modified;
			notifyListenersModifiedChanged();
		}
	}
	
	//------------------------------------------------------------------
	//							  GETTERS
	//------------------------------------------------------------------
	
	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}
	
	@Override
	public Path getFilePath() {
		return path;
	}
	
	@Override
	public boolean isModified() {
		return modified;
	}
	
	//------------------------------------------------------------------
	//						ADD/REMOVE LISTENERS
	//------------------------------------------------------------------

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		if(listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(l);
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
	
	//------------------------------------------------------------------
	//						NOTIFYING LISTENERS
	//------------------------------------------------------------------
	
	private void notifyListenersModifiedChanged() {
		if(listeners == null) return;
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}
	
	private void notifyListenersFilePathChanged() {
		if(listeners == null) return;
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}
	
	//------------------------------------------------------------------
	//							toString
	//------------------------------------------------------------------
	
	@Override
	public String toString() {
		return path == null ? "Unnamed" : path.toAbsolutePath().toString();
	}
}
