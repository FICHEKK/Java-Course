package hr.fer.zemris.java.hw11.jnotepadpp;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTabbedPane;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	private List<SingleDocumentModel> documents;
	
	private SingleDocumentModel current;

	@Override
	public Iterator<SingleDocumentModel> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return current;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNumberOfDocuments() {
		return documents.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return documents.get(index);
	}

}
