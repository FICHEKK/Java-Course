package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	private List<SingleDocumentListener> listeners;
	
	private boolean modified;
	
	private Path path;
	
	private JTextArea textArea;
	
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

	@Override
	public JTextArea getTextComponent() {
		return textArea;
	}

	@Override
	public Path getFilePath() {
		return path;
	}

	@Override
	public void setFilePath(Path path) {
		if(this.path != path) {
			this.path = path;
			notifyListenersFilePathChanged();
		}
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		if(this.modified != modified) {
			this.modified = modified;
			notifyListenersModifiedChanged();
		}
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
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}
	
	private void notifyListenersFilePathChanged() {
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	}
}
