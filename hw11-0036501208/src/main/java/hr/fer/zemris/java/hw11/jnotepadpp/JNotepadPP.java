package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Swing application that shows the functionality of a simple
 * text editor.
 *
 * @author Filip Nemec
 */
public class JNotepadPP extends JFrame {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 1L;
	
	/** The default tab title for the new documents. */
	private static final String UNNAMED_TITLE = "(unnamed)";
	
	/** The default application title when there are no tabs open. */
	private static final String DEFAULT_TITLE = "JNotepad++";
	
	/** The icon that displays that the document is saved. */
	private static final ImageIcon SAVED_ICON = loadIcon("icons/green_floppy_disk.png");
	
	/** The icon that displays that the document is not saved. */
	private static final ImageIcon NOT_SAVED_ICON = loadIcon("icons/red_floppy_disk.png");
	
	/** The tabbed pane that supports multiple documents opened at the same time. */
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	/** List that stores all of the tab paths. */
	private List<Path> tabPaths = new LinkedList<>();
	
	/** The reference to the last cut/copied text. */
	private String pasteBuffer = "";
	
	
	/**
	 * Starts and initializes a new JNotepad++ application.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(800, 600);
		setLocation(100, 100);
		setTitle(DEFAULT_TITLE);
		
		addWindowListener(new WindowAdapter() {
			@Override
	        public void windowClosing(WindowEvent e) {
	            exitApplication.actionPerformed(null);
	        }
		});
		
		initGUI();
	}
	
	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		Container pane = getContentPane();
		
		initMenuBar();
		pane.add(createToolBar(), BorderLayout.PAGE_START);
		pane.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addChangeListener(e -> {
			System.out.println("Change");
			boolean hasOpenTabs = tabbedPane.getSelectedIndex() >= 0;
			
			// Setting up buttons
			saveDocument  .setEnabled(hasOpenTabs);
			saveAsDocument.setEnabled(hasOpenTabs);
			closeDocument .setEnabled(hasOpenTabs);
			statistics    .setEnabled(hasOpenTabs);
			pasteText     .setEnabled(hasOpenTabs);
			
			if(hasOpenTabs) {
				JTextArea editor = getCurrentEditor();
				boolean hasSelection = editor.getCaret().getDot() != editor.getCaret().getMark();
				
				cutText.setEnabled(hasSelection);
				copyText.setEnabled(hasSelection);
			}

			// Setting up the proper window title
			updateWindowTitle();
		});

		
		initActions();
	}

	private void initActions() {
		// File menu
		newDocument.putValue(Action.NAME, "New");
		newDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		newDocument.putValue(Action.SHORT_DESCRIPTION, "Opens a brand new document.");
		
		openDocument.putValue(Action.NAME, "Open");
		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openDocument.putValue(Action.SHORT_DESCRIPTION, "Opens an existing document.");
		
		saveDocument.putValue(Action.NAME, "Save");
		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocument.putValue(Action.SHORT_DESCRIPTION, "Saves the currently opened document.");
		saveDocument.setEnabled(false);
		
		saveAsDocument.putValue(Action.NAME, "Save as");
		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		saveAsDocument.putValue(Action.SHORT_DESCRIPTION, "Saves the document on the desired destination.");
		saveAsDocument.setEnabled(false);
		
		closeDocument.putValue(Action.NAME, "Close");
		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		closeDocument.putValue(Action.SHORT_DESCRIPTION, "Closes the currently opened document.");
		closeDocument.setEnabled(false);
		
		statistics.putValue(Action.NAME, "Statistical info");
		statistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		statistics.putValue(Action.MNEMONIC_KEY,KeyEvent.VK_I);
		statistics.putValue(Action.SHORT_DESCRIPTION, "Shows the total number of: characters, non-blank characters and lines.");
		statistics.setEnabled(false);
		
		exitApplication.putValue(Action.NAME, "Exit");
		exitApplication.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift Q"));
		exitApplication.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		exitApplication.putValue(Action.SHORT_DESCRIPTION, "Exits the application.");
		
		// Edit menu
		cutText.putValue(Action.NAME, "Cut");
		cutText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutText.putValue(Action.SHORT_DESCRIPTION, "Cuts the selected text - removes it and saves it for pasting.");
		cutText.setEnabled(false);
		
		copyText.putValue(Action.NAME, "Copy");
		copyText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyText.putValue(Action.SHORT_DESCRIPTION, "Copies the selected text for further pasting.");
		copyText.setEnabled(false);
		
		pasteText.putValue(Action.NAME, "Paste");
		pasteText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteText.putValue(Action.SHORT_DESCRIPTION, "Pastes the previously copied text.");
		pasteText.setEnabled(false);
	}

	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		file.add(new JMenuItem(newDocument));
		file.add(new JMenuItem(openDocument));
		file.addSeparator();
		file.add(new JMenuItem(saveDocument));
		file.add(new JMenuItem(saveAsDocument));
		file.addSeparator();
		file.add(new JMenuItem(statistics));
		file.addSeparator();
		file.add(new JMenuItem(closeDocument));
		file.add(new JMenuItem(exitApplication));
		
		JMenu edit = new JMenu("Edit");
		menuBar.add(edit);
		edit.add(new JMenuItem(cutText));
		edit.add(new JMenuItem(copyText));
		edit.add(new JMenuItem(pasteText));
		
		setJMenuBar(menuBar);
	}
	
	private JToolBar createToolBar() {
		JToolBar tb = new JToolBar();
		tb.setFloatable(true);
		tb.setVisible(true);
		
		tb.add(new JButton(newDocument));
		tb.add(new JButton(openDocument));
		tb.add(new JButton(saveDocument));
		tb.add(new JButton(saveAsDocument));
		tb.add(new JButton(closeDocument));
		
		tb.add(new JButton(cutText));
		tb.add(new JButton(copyText));
		tb.add(new JButton(pasteText));
		
		tb.add(new JButton(statistics));
		tb.add(new JButton(exitApplication));
		
		return tb;
	}
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - OPEN
	//--------------------------------------------------------------------------------
	
	/**
	 * Creates a new empty document.
	 */
	private final Action newDocument = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			openDocument(UNNAMED_TITLE, null, null);
		}
	};
	
	/**
	 * Prompts the user to choose the source path, and then loads the 
	 * selected file (unless an error occurs, in which case the user
	 * will be notified).
	 */
	private final Action openDocument = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(new File("C:\\Users\\FICHEKK\\Desktop"));
			jfc.setDialogTitle("Open existing file...");
			
			if(jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			try {
				Path filePath = jfc.getSelectedFile().toPath();
				String fileText = Files.readString(filePath, StandardCharsets.UTF_8);
				
				// If file reading passed, only then create a new document.
				openDocument(filePath.getFileName().toString(), fileText, filePath);
				
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(JNotepadPP.this,
											  "Error during the reading of the file. File was not opened.",
											  "Error",
											  JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - SAVE
	//--------------------------------------------------------------------------------
	
	/**
	 * Saves the document. If the document was empty (not saved on the disc
	 * before opening), user will be prompted to choose the destination path.
	 * If the document was loaded from some source beforehand, file will be
	 * saved to that path, and older file version (file before editing) will
	 * be overwritten.
	 */
	private final Action saveDocument = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Path savePath = tabPaths.get(tabbedPane.getSelectedIndex());
			
			// This is a new document that has not been saved to disk yet.
			if(savePath == null) {
				JFileChooser jfc = new JFileChooser("C:\\Users\\FICHEKK\\Desktop");
				jfc.setDialogTitle("Save to...");
				
				if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				savePath = jfc.getSelectedFile().toPath();
			}
			
			performSaving(savePath, "File was saved.");
		}
	};
	
	/**
	 * Prompts the user to select the saving destination. If there already is
	 * a file at the destination path, user will be asked if the file should
	 * be overwritten.
	 */
	private final Action saveAsDocument = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser("C:\\Users\\FICHEKK\\Desktop");
			jfc.setDialogTitle("Save as...");
			
			if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
				
			Path saveAsPath = jfc.getSelectedFile().toPath();
			
			boolean overwriting = false;
			if(Files.exists(saveAsPath)) {
				if(JOptionPane.showConfirmDialog(JNotepadPP.this,
											  "File already exists. Are you sure you want to overwrite it?",
											  "Overwrite file?",
											  JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(JNotepadPP.this, "File was not overwritten.");
					return;
				}
				
				overwriting = true;
			}
			
			performSaving(saveAsPath, "File was " + (overwriting ? "overwritten" : "saved") + ".");
		}
	};
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - CLOSE
	//--------------------------------------------------------------------------------
	
	/**
	 * Closes the currently selected tab. This action
	 * is not enabled if there are no opened tabs.
	 */
	private final Action closeDocument = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = tabbedPane.getSelectedIndex();
			
			if(tabbedPane.getIconAt(index) == NOT_SAVED_ICON) {
				if(JOptionPane.showConfirmDialog(JNotepadPP.this,
												 "This document was not saved. Are you sure you want to close it?",
												 "Warning",
												 JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					return;
				}
			}
			
			tabbedPane.removeTabAt(index);
			tabPaths.remove(index);
		}
	};
	
	/**
	 * Exits the application.
	 */
	private final Action exitApplication = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;
		
		/** All of the offered options if any file was not saved during the exit. */
		private final String[] options = {"Save", "Discard", "Abort closing"};

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean aborted = false;
			
			for(int i = tabbedPane.getTabCount() - 1; i >= 0; i--) {
				if(tabbedPane.getIconAt(i) == NOT_SAVED_ICON) {
					tabbedPane.setSelectedIndex(i);
					
					int result = JOptionPane.showOptionDialog(JNotepadPP.this,
															  "This document was not saved. What would you like to do?", 
															  "Select action",
															  JOptionPane.YES_NO_CANCEL_OPTION,
															  JOptionPane.QUESTION_MESSAGE,
															  null,
															  options,
															  options[0]);
					// saving
					if(result == JOptionPane.YES_OPTION) {
						saveDocument.actionPerformed(null);
					
					// aborting
					} else if(result == JOptionPane.CANCEL_OPTION) { //
						aborted = true;
						break;
					}
					
					tabbedPane.removeTabAt(i);
				}
			}
			
			if(!aborted) {
				dispose();
			}
		}
	};
	
	//--------------------------------------------------------------------------------
	//						  ACTIONS - CUT, COPY, PASTE
	//--------------------------------------------------------------------------------
	
	private final Action cutText = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = getCurrentEditor();
			
			int dot  = editor.getCaret().getDot();
			int mark = editor.getCaret().getMark();
			
			int start = Math.min(dot, mark);
			int len   = Math.abs(dot - mark);
			
			Document doc = editor.getDocument();
			
			try {
				pasteBuffer = doc.getText(start, len);
				doc.remove(start, len);
				
			} catch (BadLocationException e1) {
			}
		}
	};
	
	private final Action copyText = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = getCurrentEditor();
			
			int dot  = editor.getCaret().getDot();
			int mark = editor.getCaret().getMark();
			
			int start = Math.min(dot, mark);
			int len   = Math.abs(dot - mark);
			
			try {
				pasteBuffer = editor.getDocument().getText(start, len);
				
			} catch (BadLocationException e1) {
			}
			
		}
	};
	
	private final Action pasteText = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if(pasteBuffer.isEmpty()) return;
			
			JTextArea editor = getCurrentEditor();
			Document doc = editor.getDocument();
			
			try {
				doc.insertString(editor.getCaretPosition(), pasteBuffer, null);
				
			} catch (BadLocationException e1) {
			}
		}
	};
	
	//--------------------------------------------------------------------------------
	//							ACTIONS - STATISTICAL INFO
	//--------------------------------------------------------------------------------
	
	/**
	 * Calculates the statistics for the currently opened document.
	 * Statistics consists of: total number of characters, total number
	 * of non-blank characters, total number of lines.
	 */
	private final Action statistics = new AbstractAction() {
		
		/** Used for serialization. */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int totalChars = 0;
			int nonBlankChars = 0;
			int lines = 0;

			JTextArea editor = getCurrentEditor();
			String text = editor.getText();
			
			// number of chars
			totalChars = text.length();
			
			// number of non-blank chars
			char[] chars = text.toCharArray();
			for(char c : chars) {
				if(!Character.isWhitespace(c)) {
					nonBlankChars++;
				}
			}
			
			// number of lines
			lines = editor.getLineCount();
			
			JOptionPane.showMessageDialog(JNotepadPP.this,
										  "Total characters: " + totalChars + System.lineSeparator() +
										  "Non-blank characters: " + nonBlankChars + System.lineSeparator() +
										  "Lines: " + lines,
										  "Statistical info",
										  JOptionPane.INFORMATION_MESSAGE);
		}
	};

	//--------------------------------------------------------------------------------
	//								HELPER METHODS
	//--------------------------------------------------------------------------------
	
	/**
	 * Opens a new document in the new tab and returns the reference to the
	 * {@code JTextArea} that represents the newly opened document.
	 *
	 * @param docTitle the title of the document
	 * @param docPath the path associated with the opening document
	 * @return the reference to the {@code JTextArea} that represents the newly created
	 * 		   document
	 */
	private JTextArea openDocument(String docTitle, String docText, Path docPath) {
		JTextArea docEditor = new JTextArea();
		tabPaths.add(docPath);
		tabbedPane.addTab(docTitle, SAVED_ICON, new JScrollPane(docEditor));
		
		int index = tabbedPane.getTabCount() - 1;
		tabbedPane.setToolTipTextAt(index, docPath == null ? UNNAMED_TITLE : docPath.toAbsolutePath().toString());
		tabbedPane.setSelectedIndex(index);
		
		docEditor.grabFocus();
		docEditor.setText(docText);
		
		docEditor.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				tabbedPane.setIconAt(index, NOT_SAVED_ICON);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				tabbedPane.setIconAt(index, NOT_SAVED_ICON);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
		// Cut and copy for this newly opened document.
		docEditor.getCaret().addChangeListener(e -> {
			boolean hasSelection = docEditor.getCaret().getDot() != docEditor.getCaret().getMark();
			
			cutText.setEnabled(hasSelection);
			copyText.setEnabled(hasSelection);
		});
		
		// Nothing is selected initially, so cut and copy should be disabled.
		cutText.setEnabled(false);
		copyText.setEnabled(false);
		
		return docEditor;
	}
	
	/**
	 * Performs the saving operation to the given destination.
	 *
	 * @param saveDestination the path to the save destination
	 * @param successMsg the message that will be displayed if the
	 * 					 saving operation was successful
	 */
	private void performSaving(Path saveDestination, String successMsg) {
		try {
			Files.writeString(saveDestination, getCurrentEditor().getText());
			
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(JNotepadPP.this, "File saving error occured.", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
		int index = tabbedPane.getSelectedIndex();
		
		// Update the path as it might have been null.
		tabPaths.set(index, saveDestination);
		
		// Update the title and icon to green.
		tabbedPane.setTitleAt(index, saveDestination.getFileName().toString());
		tabbedPane.setIconAt(index, SAVED_ICON);
		
		updateWindowTitle();
		
		JOptionPane.showMessageDialog(JNotepadPP.this, successMsg);
	}
	
	/**
	 * A helper method that returns the reference to the currently opened
	 * instance of {@code JTextArea} text editor.
	 *
	 * @return the reference to the currently opened instance of {@code JTextArea}
	 * 		   text editor
	 */
	private JTextArea getCurrentEditor() {
		JViewport viewport = ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport();
		return (JTextArea) viewport.getView();
	}
	
	/**
	 * Updates this {@code JFrame's} title to the proper one.
	 */
	private void updateWindowTitle() {
		String title = "";
		
		// If there are any tabs opened.
		if(tabbedPane.getSelectedIndex() >= 0) {
			Path docPath = tabPaths.get(tabbedPane.getSelectedIndex());
			
			title = (docPath != null) ? docPath.toAbsolutePath().toString() : UNNAMED_TITLE;
			title += " - ";
		}
		
		title += DEFAULT_TITLE;
		
		JNotepadPP.this.setTitle(title);
	}
	
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
	
	//--------------------------------------------------------------------------------
	//									main
	//--------------------------------------------------------------------------------
	
	/**
	 * Simple main method that simply starts the JNotepad++ application in the
	 * EDT (event dispatching thread).
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}
}
