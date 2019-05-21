package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;

import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.component.LocalizedAbstractAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.component.LocalizedMenu;

/**
 * Swing application that shows the functionality of a simple
 * <i>Notepad</i> like text editor.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class JNotepadPP extends JFrame {

	/** The default tab title for the new documents. */
	private static final String UNNAMED_TITLE = "(unnamed)";
	
	/** The default application title when there are no tabs open. */
	private static final String DEFAULT_TITLE = "JNotepad++";
	
	/** The date format used by the application's clock. */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	/** The container that can hold multiple documents. */
	private DefaultMultipleDocumentModel model;
	
	/** The reference to the last cut/copied text. */
	private String pasteBuffer = "";
	
	/** Reference to the document text length label. */
	private JLabel lengthLabel;
	
	/** Reference to the caret position label. */
	private JLabel caretLabel;
	
	/** Reference to the time label. */
	private JLabel timeLabel;
	
	/** Reference to the clock. */
	private Timer clock;
	
	/** The translation provider for this application. */
	private FormLocalizationProvider translator = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

	/**
	 * Starts and initializes a new JNotepad++ application.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(960, 640);
		setLocation(240, 120);
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
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createToolBar(), BorderLayout.PAGE_START);
		panel.add(model = new DefaultMultipleDocumentModel(), BorderLayout.CENTER);
		panel.add(createStatusBar(), BorderLayout.PAGE_END);
		pane.add(panel);
		
		initMenuBar();
		initActions();
		
		translator.addLocalizationListener(() -> {
			updateLengthLabelText();
			updateCaretLabelText();
		});
		
		model.addMultipleDocumentListener(new MultipleDocumentListener() {
			
			@Override
			public void documentRemoved(SingleDocumentModel removedModel) {
				boolean hasOpenDocuments = model.getCurrentDocument() != null;
				toggleDocumentDependentActions(hasOpenDocuments);
			}
			
			@Override
			public void documentAdded(SingleDocumentModel addedModel) {
				// If the first document was just opened, toggle buttons.
				if(model.getNumberOfDocuments() == 1) {
					toggleDocumentDependentActions(true);
				}
			}
			
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				updateWindowTitle();
				updateCaretLabelText();
				updateLengthLabelText();
				
				if(model.getCurrentDocument() == null) return;
				
				JTextArea editor = model.getCurrentDocument().getTextComponent();
				
				// Length label
				editor.getDocument().addDocumentListener(new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						lengthLabel.setText(translator.getString("length") + ": " + e.getDocument().getLength());
					}
					
					@Override
					public void insertUpdate(DocumentEvent e) {
						lengthLabel.setText(translator.getString("length") + ": " + e.getDocument().getLength());
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {
						lengthLabel.setText(translator.getString("length") + ": " + e.getDocument().getLength());
					}
				});

				// Caret label
				ChangeListener caretListener = e -> {
						boolean hasSelection = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark()) > 0;
						toggleSelectionDependentActions(hasSelection);
						updateCaretLabelText();
				};
				
				editor.getCaret().addChangeListener(caretListener);
			}
		});
	}
	
	/**
	 * Enables or disables all of the actions that are dependent
	 * on the open document. For example, "save" option should
	 * not be enabled if there is no open document.
	 *
	 * @param hasOpenDocuments if there is an open document
	 */
	private void toggleDocumentDependentActions(boolean hasOpenDocuments) {
		saveDocument  .setEnabled(hasOpenDocuments);
		saveAsDocument.setEnabled(hasOpenDocuments);
		closeDocument .setEnabled(hasOpenDocuments);
		statistics    .setEnabled(hasOpenDocuments);
		pasteText     .setEnabled(hasOpenDocuments);
	}
	
	/**
	 * Enables or disables all of the actions that are dependent
	 * on the text selection. For example, "cut" option should
	 * not be enabled if there is no text selection.
	 *
	 * @param hasSelection if there is a text selection
	 */
	private void toggleSelectionDependentActions(boolean hasSelection) {
		cutText			.setEnabled(hasSelection);
		copyText		.setEnabled(hasSelection);
		toUpperCase		.setEnabled(hasSelection);
		toLowerCase		.setEnabled(hasSelection);
		invertCase		.setEnabled(hasSelection);
		sortAscending	.setEnabled(hasSelection);
		sortDescending	.setEnabled(hasSelection);
		unique			.setEnabled(hasSelection);
	}

	/**
	 * Initializes all of the actions.
	 */
	private void initActions() {
		// File menu
		newDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		newDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		newDocument.putValue(Action.SHORT_DESCRIPTION, "Opens a brand new document.");
		
		openDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		openDocument.putValue(Action.SHORT_DESCRIPTION, "Opens an existing document.");
		
		saveDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocument.putValue(Action.SHORT_DESCRIPTION, "Saves the currently opened document.");
		saveDocument.setEnabled(false);
		
		saveAsDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
		saveAsDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		saveAsDocument.putValue(Action.SHORT_DESCRIPTION, "Saves the document on the desired destination.");
		saveAsDocument.setEnabled(false);
		
		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		closeDocument.putValue(Action.SHORT_DESCRIPTION, "Closes the currently opened document.");
		closeDocument.setEnabled(false);
		
		statistics.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		statistics.putValue(Action.MNEMONIC_KEY,KeyEvent.VK_I);
		statistics.putValue(Action.SHORT_DESCRIPTION, "Shows the total number of: characters, non-blank characters and lines.");
		statistics.setEnabled(false);
		
		exitApplication.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift Q"));
		exitApplication.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		exitApplication.putValue(Action.SHORT_DESCRIPTION, "Exits the application.");
		
		// Edit menu
		cutText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cutText.putValue(Action.SHORT_DESCRIPTION, "Cuts the selected text - removes it and saves it for pasting.");
		cutText.setEnabled(false);
		
		copyText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copyText.putValue(Action.SHORT_DESCRIPTION, "Copies the selected text for further pasting.");
		copyText.setEnabled(false);
		
		pasteText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		pasteText.putValue(Action.SHORT_DESCRIPTION, "Pastes the previously copied text.");
		pasteText.setEnabled(false);
		
		// Tools menu
		toUpperCase.putValue(Action.SHORT_DESCRIPTION, "Converts the selected text to uppercase.");
		toUpperCase.setEnabled(false);
		
		toLowerCase.putValue(Action.SHORT_DESCRIPTION, "Converts the selected text to lowercase.");
		toLowerCase.setEnabled(false);
		
		invertCase.putValue(Action.SHORT_DESCRIPTION, "Selected upper case text becomes lower case, and vice versa.");
		invertCase.setEnabled(false);
		
		unique.putValue(Action.SHORT_DESCRIPTION, "Deletes all of the duplicate lines, leaving only the first occurance.");
		unique.setEnabled(false);
	}
	
	/**
	 * Initializes the menu-bar.
	 */
	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		// FILE
		JMenu file = new LocalizedMenu("file", translator);
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
		
		// EDIT
		JMenu edit = new LocalizedMenu("edit", translator);
		menuBar.add(edit);
		edit.add(new JMenuItem(cutText));
		edit.add(new JMenuItem(copyText));
		edit.add(new JMenuItem(pasteText));
		
		// TOOLS
		JMenu tools = new LocalizedMenu("tools", translator);
		menuBar.add(tools);
		JMenu changeCase = new LocalizedMenu("change_case", translator);
		tools.add(changeCase);
		changeCase.add(new JMenuItem(toUpperCase));
		changeCase.add(new JMenuItem(toLowerCase));
		changeCase.add(new JMenuItem(invertCase));
		JMenu sort = new LocalizedMenu("sort", translator);
		tools.add(sort);
		sort.add(sortAscending);
		sort.add(sortDescending);
		tools.add(new JMenuItem(unique));
		
		// LANGUAGE
		JMenu languages = new LocalizedMenu("languages", translator);
		menuBar.add(languages);
		languages.add(new JMenuItem(languageHR));
		languages.add(new JMenuItem(languageEN));
		languages.add(new JMenuItem(languageDE));
		
		setJMenuBar(menuBar);
	}
	
	/**
	 * Creates and returns the toolbar for this application.
	 *
	 * @return the initialized toolbar
	 */
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
		
		tb.add(new JButton(toUpperCase));
		tb.add(new JButton(toLowerCase));
		tb.add(new JButton(invertCase));
		
		tb.add(new JButton(statistics));
		tb.add(new JButton(exitApplication));
		
		return tb;
	}
	
	/**
	 * Creates and returns the status bar.
	 *
	 * @return the status bar
	 */
	private JPanel createStatusBar() {
		JPanel statusBarPanel = new JPanel(new GridLayout(1, 3));
		
		statusBarPanel.add(lengthLabel = new JLabel());
		statusBarPanel.add(caretLabel = new JLabel());
		statusBarPanel.add(timeLabel = new JLabel(DATE_FORMAT.format(new Date(System.currentTimeMillis()))));
		lengthLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		caretLabel .setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		timeLabel  .setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
		
		initClock();
		
		return statusBarPanel;
	}
	
	/**
	 * Initializes the clock that runs in a new thread. This clock will
	 * refresh the time label text each second.
	 */
	private void initClock() {
		// Updating time periodically each second.
		clock = new Timer(1000, e -> {
			timeLabel.setText(DATE_FORMAT.format(new Date(System.currentTimeMillis())));
		});

		clock.start();
	}

	//--------------------------------------------------------------------------------
	//								ACTIONS - OPEN
	//--------------------------------------------------------------------------------
	
	/**
	 * Creates a new empty document.
	 */
	private final Action newDocument = new LocalizedAbstractAction("new", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
		}
	};
	
	/**
	 * Prompts the user to choose the source path, and then loads the 
	 * selected file (unless an error occurs, in which case the user
	 * will be notified).
	 */
	private final Action openDocument = new LocalizedAbstractAction("open", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser(new File("C:\\Users\\FICHEKK\\Desktop"));
			jfc.setDialogTitle("Open existing file...");
			
			if(jfc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			
			model.loadDocument(jfc.getSelectedFile().toPath());
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
	private final Action saveDocument = new LocalizedAbstractAction("save", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			model.saveDocument(doc, doc.getFilePath());
		}
	};
	
	/**
	 * Prompts the user to select the saving destination. If there already is
	 * a file at the destination path, user will be asked if the file should
	 * be overwritten.
	 */
	private final Action saveAsDocument = new LocalizedAbstractAction("save_as", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser("C:\\Users\\FICHEKK\\Desktop");
			jfc.setDialogTitle("Save as...");
			
			if(jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
				
			Path saveAsPath = jfc.getSelectedFile().toPath();
			
			if(Files.exists(saveAsPath)) {
				if(JOptionPane.showConfirmDialog(JNotepadPP.this,
											  "File already exists. Are you sure you want to overwrite it?",
											  "Overwrite file?",
											  JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(JNotepadPP.this, "File was not overwritten.");
					return;
				}
			}
			
			model.saveDocument(model.getCurrentDocument(), saveAsPath);
		}
	};
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - CLOSE
	//--------------------------------------------------------------------------------
	
	/**
	 * Closes the currently selected tab. This action
	 * is not enabled if there are no opened tabs.
	 */
	private final Action closeDocument = new LocalizedAbstractAction("close", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			model.closeDocument(model.getCurrentDocument());
		}
	};
	
	/**
	 * Exits the application.
	 */
	private final Action exitApplication = new LocalizedAbstractAction("exit", translator) {
		
		/** All of the offered options if any file was not saved during the exit. */
		private final String[] options = {"Save", "Discard", "Abort closing"};

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean aborted = false;
			
			for(int i = model.getNumberOfDocuments() - 1; i >= 0; i--) {
				SingleDocumentModel doc = model.getDocument(i);
				
				if(doc.isModified()) {
					model.setSelectedIndex(i);
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
						model.saveDocument(doc, doc.getFilePath());
					
					// aborting
					} else if(result == JOptionPane.CANCEL_OPTION) {
						aborted = true;
						break;
					}
					
					model.closeDocument(doc);
				}
			}
			
			if(!aborted) {
				dispose();
				clock.stop();
			}
		}
	};
	
	//--------------------------------------------------------------------------------
	//						  ACTIONS - CUT, COPY, PASTE
	//--------------------------------------------------------------------------------
	
	/**
	 * Cuts the selected portion of the text. That simply means
	 * that the selected text will be removed and stored to the
	 * internal buffer for later pasting operation.
	 */
	private final Action cutText = new LocalizedAbstractAction("cut", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			
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
	
	
	/**
	 * Copies the selected portion of the text. That simply means
	 * that the selected text will be stored to the internal buffer
	 * for later pasting operation.
	 */
	private final Action copyText = new LocalizedAbstractAction("copy", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			
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
	
	/**
	 * Pastes the previously cut/copied text to the selected location.
	 * This simply means that the stored text from previous cut/copy
	 * operation will be inserted at the current caret position.
	 */
	private final Action pasteText = new LocalizedAbstractAction("paste", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(pasteBuffer.isEmpty()) return;
			
			JTextArea editor = model.getCurrentDocument().getTextComponent();
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
	private final Action statistics = new LocalizedAbstractAction("statistics", translator) {

		@Override
		public void actionPerformed(ActionEvent e) {
			int totalChars = 0;
			int nonBlankChars = 0;
			int lines = 0;

			JTextArea editor = model.getCurrentDocument().getTextComponent();
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
	//					ACTIONS - UPPERCASE, LOWERCASE, INVERTCASE
	//--------------------------------------------------------------------------------
	
	/**
	 * Inverts the selected portion of the text, meaning that lower case
	 * letters will become upper case and vice versa. Characters that
	 * cannot be upper and lower cased are not affected (for example, digits).
	 */
	private final Action invertCase = new LocalizedAbstractAction("invert_case", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			performTextConversion(text -> {
				char[] chars = text.toCharArray();
				
				for(int i = 0; i < chars.length; i++) {
					if(Character.isUpperCase(chars[i])) {
						chars[i] = Character.toLowerCase(chars[i]);
						
					} else if(Character.isLowerCase(chars[i])) {
						chars[i] = Character.toUpperCase(chars[i]);
					}
				}
				
				return new String(chars);
			});
		}
	};
	
	/** Converts the selected portion of the text to the upper-case. */
	private final Action toUpperCase = new LocalizedAbstractAction("to_uppercase", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			performTextConversion(String::toUpperCase);
		}
	};
	
	/** Converts the selected portion of the text to the lower-case. */
	private final Action toLowerCase = new LocalizedAbstractAction("to_lowercase", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			performTextConversion(String::toLowerCase);
		}
	};
	
	/**
	 * A general method for converting the selected portion of the
	 * text.
	 *
	 * @param textConverter the object that can convert text
	 */
	private void performTextConversion(Function<String, String> textConverter) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		Caret caret = editor.getCaret();
		
		int start = Math.min(caret.getDot(), caret.getMark());
		int len   = Math.abs(caret.getDot() - caret.getMark());
		
		if(len < 1) return;
		
		Document doc = editor.getDocument();
		
		try {
			String text = textConverter.apply(doc.getText(start, len));
			
			doc.remove(start, len);
			doc.insertString(start, text, null);
			
		} catch(BadLocationException ignorable) {
		}
	}
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - SORT
	//--------------------------------------------------------------------------------
	
	/**
	 * Sorts the selected text in the ascending order.
	 */
	private final Action sortAscending = new LocalizedAbstractAction("ascending", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelectedLines((line1, line2) -> line1.compareTo(line2));
		}
	};
	
	
	/**
	 * Sorts the selected text in the descending order.
	 */
	private final Action sortDescending = new LocalizedAbstractAction("descending", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sortSelectedLines(((line1, line2) -> line2.compareTo(line1)));
		}
	};
	
	/**
	 * Sorts the selected lines. Sorting process is defined by the
	 * given {@code Comparator} object.
	 *
	 * @param comparator the object that does the comparing
	 */
	
	private void sortSelectedLines(Comparator<String> comparator) {
		try {
			var lines = new LinkedList<String>();
			saveSelectedLinesTo(lines);
			lines.sort(comparator);
			insertLines(lines, removeSelectedLines());
			
		} catch (BadLocationException ex) {
		}
	}
	
	//--------------------------------------------------------------------------------
	//								ACTIONS - UNIQUE
	//--------------------------------------------------------------------------------
	
	/**
	 * Removes all of the duplicate lines from the selected
	 * text. Only the first occurrence is not removed. 
	 */
	private final Action unique = new LocalizedAbstractAction("unique", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				var uniqueLines = new LinkedHashSet<String>();
				saveSelectedLinesTo(uniqueLines);
				insertLines(uniqueLines, removeSelectedLines());
				
			} catch (BadLocationException ex) {
			}
		}
	};
	
	/**
	 * Saves the selected lines to the given collection
	 *
	 * @param collection the storage for the lines
	 * @throws BadLocationException if the offset is invalid
	 */
	private void saveSelectedLinesTo(Collection<String> collection) throws BadLocationException {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		
		int dot  = editor.getCaret().getDot();
		int mark = editor.getCaret().getMark();
		int startLine = editor.getLineOfOffset(Math.min(dot, mark));
		int endLine   = editor.getLineOfOffset(Math.max(dot, mark));
		
		Element root = editor.getDocument().getDefaultRootElement();
		
		for(int line = startLine; line <= endLine; line++) {
			Element elem = root.getElement(line);
			
			int start = elem.getStartOffset();
			int end   = elem.getEndOffset();
					
			collection.add(editor.getText(start, end - start));
		}
	}
	
	/**
	 * Removes the currently selected lines.
	 *
	 * @throws BadLocationException if the offset is invalid
	 * @return the offset of cursor after the deletion
	 */
	private int removeSelectedLines() throws BadLocationException {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		
		int dot = editor.getCaret().getDot();
		int mark = editor.getCaret().getMark();
		int startLine = editor.getLineOfOffset(Math.min(dot, mark));
		int endLine   = editor.getLineOfOffset(Math.max(dot, mark));
		
		Element root = editor.getDocument().getDefaultRootElement();
		
		int firstLineStart = root.getElement(startLine).getStartOffset();
		int lastLineEnd    = root.getElement(endLine).getEndOffset();
		editor.getDocument().remove(firstLineStart, lastLineEnd - firstLineStart - 1);
		
		return firstLineStart;
	}
	
	/**
	 * Inserts the given collection of lines to the current document.
	 *
	 * @param lines the collection of lines
	 * @param offset the starting offset for insertion
	 * @throws BadLocationException if the offset is invalid
	 */
	private void insertLines(Collection<String> lines, int offset) throws BadLocationException {
		Document doc = model.getCurrentDocument().getTextComponent().getDocument();
		
		for(String line : lines) {
			doc.insertString(offset, line, null);
			offset += line.length();
		}
	}

	//--------------------------------------------------------------------------------
	//							  ACTIONS - LANGUAGES
	//--------------------------------------------------------------------------------
	
	private final Action languageHR = new LocalizedAbstractAction("croatian", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	private final Action languageEN = new LocalizedAbstractAction("english", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	private final Action languageDE = new LocalizedAbstractAction("german", translator) {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	};
	
	//--------------------------------------------------------------------------------
	//								HELPER METHODS
	//--------------------------------------------------------------------------------

	/**
	 * Updates this {@code JFrame's} title to the proper one.
	 */
	private void updateWindowTitle() {
		String title = "";
		
		// If there are any tabs opened.
		if(model.getCurrentDocument() != null) {
			Path docPath = model.getCurrentDocument().getFilePath();
			
			title = (docPath != null) ? docPath.toAbsolutePath().toString() : UNNAMED_TITLE;
			title += " - ";
		}
		
		title += DEFAULT_TITLE;
		
		JNotepadPP.this.setTitle(title);
	}
	
	/**
	 * Updates the caret label text.
	 */
	private void updateCaretLabelText() {
		var doc = model.getCurrentDocument();
		String text = "";
		
		if(doc != null) {
			try {
				JTextArea editor = doc.getTextComponent();
				int caret = editor.getCaretPosition();
				int ln = editor.getLineOfOffset(caret);
				int col = caret - editor.getLineStartOffset(ln);
				int sel = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
				
				text = translator.getString("ln")  + ": " + (ln + 1)   + "  " + 
					   translator.getString("col") + ": " + (col + 1) + "  " +
					   translator.getString("sel") + ": " + sel;
				
			} catch(BadLocationException ex) {
			}
		}  
		
		caretLabel.setText(text);
	}
	
	/**
	 * Updates the length label text.
	 */
	private void updateLengthLabelText() {
		var doc = model.getCurrentDocument();
		String text = "";
		
		if(doc != null) {
			text = translator.getString("length") + ": " + doc.getTextComponent().getDocument().getLength();
		}
		
		lengthLabel.setText(text);
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
