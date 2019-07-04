package hr.fer.zemris.java.hw17.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import hr.fer.zemris.java.hw17.jvdraw.color.ColorsInfoLabel;
import hr.fer.zemris.java.hw17.jvdraw.color.JColorArea;
import hr.fer.zemris.java.hw17.jvdraw.editors.GeometricalObjectEditor;
import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;
import hr.fer.zemris.java.hw17.jvdraw.tools.CircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.FilledCircleTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.LineTool;
import hr.fer.zemris.java.hw17.jvdraw.tools.Tool;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectBBCalculator;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectPainter;
import hr.fer.zemris.java.hw17.jvdraw.visitors.GeometricalObjectSaveFileBuilder;

/**
 * The main part of the application that connects all of the parts.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class JVDraw extends JFrame implements Supplier<Tool> {
	
	/** The path of the currently opened file. Can be null if the file was not saved. */
	private Path currentPath;

	/** The currently selected/used tool. */
	private Tool selectedTool;
	
	/** The drawing model. */
	private DrawingModel model;
	
	/** The drawing canvas. */
	private JDrawingCanvas canvas;
	
	/** The color area for the foreground color. */
	private JColorArea fgColorArea;
	
	/** The color area for the background color. */
	private JColorArea bgColorArea;
	
	//----------------------------------------------------------------
	//						  	Constructor
	//----------------------------------------------------------------
	
	/**
	 * Constructs a new {@code JVDraw} window.
	 */
	public JVDraw() {
		this.model = new DrawingModelImpl();
		this.canvas = new JDrawingCanvas(this, this.model);
		
		setTitle("JPaint++");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setResizable(false);
		
		initGUI();
	}

	/**
	 * Initializes the graphical user interface.
	 */
	private void initGUI() {
		Container pane = getContentPane();
		
		//-----------------------------------------------------
		//				  Top - Menu & Toolbar
		//-----------------------------------------------------
		
		initMenu();
		pane.add(createToolbar(), BorderLayout.PAGE_START);
		
		//-----------------------------------------------------
		//			Center - Canvas and object list
		//-----------------------------------------------------
		
		DrawingObjectListModel listModel = new DrawingObjectListModel(model);
		var objectList = new JList<GeometricalObject>(listModel);
		
		objectList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int index = objectList.getSelectedIndex();
				if(index < 0) return;
				
				GeometricalObject selected = model.getObject(index);
				
				if(e.getKeyCode() == KeyEvent.VK_DELETE) {
					model.remove(selected);
				
				} else if(e.getKeyCode() == KeyEvent.VK_PLUS) {
					model.changeOrder(selected, 1);
					objectList.setSelectedIndex(objectList.getSelectedIndex() + 1);
					
				} else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
					model.changeOrder(selected, -1);
					objectList.setSelectedIndex(objectList.getSelectedIndex() - 1);
					
				}
			}
		});
		
		objectList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		        	@SuppressWarnings("unchecked")
					int index = ((JList<GeometricalObject>) e.getSource()).locationToIndex(e.getPoint());
		        	
		        	if(index < 0) return;
		        	
		            GeometricalObject selected = model.getObject(index);
		            GeometricalObjectEditor editor = selected.createGeometricalObjectEditor();
		            
		        	while(true) {
		        		int selectedOption = JOptionPane.showConfirmDialog(JVDraw.this,
																		   editor,
																		   "Edit '" + selected + "'",
																		   JOptionPane.OK_CANCEL_OPTION);
									 
			            if(selectedOption == JOptionPane.OK_OPTION) {
							try {
								editor.checkEditing();
								editor.acceptEditing();
								
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(JVDraw.this, ex.getMessage());
								continue;
							}
						}
			            
			            break;
		        	}
		        }
			}
		});
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvas, objectList);
		splitPane.setResizeWeight(0.7);
		pane.add(splitPane, BorderLayout.CENTER);
		
		//-----------------------------------------------------
		//				  Bottom - Colors info
		//-----------------------------------------------------
		var bottomColorInfo = new ColorsInfoLabel(fgColorArea, bgColorArea);
		fgColorArea.addColorChangeListener(bottomColorInfo);
		bgColorArea.addColorChangeListener(bottomColorInfo);
		pane.add(bottomColorInfo, BorderLayout.SOUTH);
	}
	
	//----------------------------------------------------------------
	//							Menu
	//----------------------------------------------------------------
	
	/**
	 * Initializes the menu.
	 */
	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		// FILE
		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		// Open action
		open.putValue(Action.NAME, "Open");
		file.add(new JMenuItem(open));
		
		// Save action
		save.putValue(Action.NAME, "Save");
		file.add(new JMenuItem(save));
		
		// Save As action
		saveAs.putValue(Action.NAME, "Save As");
		file.add(new JMenuItem(saveAs));
		
		file.addSeparator();
		
		// Export action
		export.putValue(Action.NAME, "Export As Image");
		file.add(new JMenuItem(export));
		
		file.addSeparator();
		
		// Exit action
		exit.putValue(Action.NAME, "Exit");
		file.add(new JMenuItem(exit));
		
		setJMenuBar(menuBar);
	}
	
	//----------------------------------------------------------------
	//							Tool-bar
	//----------------------------------------------------------------
	
	/**
	 * Creates and returns the application's tool-bar.
	 *
	 * @return the created tool-bar
	 */
	private JToolBar createToolbar() {
		JToolBar tb = new JToolBar();
		
		tb.setFloatable(true);
		tb.setVisible(true);
		
		tb.add(fgColorArea = new JColorArea(Color.BLUE));
		tb.add(bgColorArea = new JColorArea(Color.BLACK));
		
		// 								Tools
		//---------------------------------------------------------------------
		
		JToggleButton line = new JToggleButton(onLineToolClick);
		line.setText("Line");
		line.setSelected(true);
		selectedTool = new LineTool(fgColorArea, model);
		
		JToggleButton circle = new JToggleButton(onCircleToolClick);
		circle.setText("Circle");
		
		JToggleButton filledCircle = new JToggleButton(onFilledCircleToolClick);
		filledCircle.setText("Filled circle");
		
		ButtonGroup group = new ButtonGroup();
		group.add(line);
		group.add(circle);
		group.add(filledCircle);
		
		tb.add(line);
		tb.add(circle);
		tb.add(filledCircle);
		
		//---------------------------------------------------------------------
		
		return tb;
	}
	
	//----------------------------------------------------------------
	//					Actions - on tool click
	//----------------------------------------------------------------
	
	private final Action onLineToolClick = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedTool = new LineTool(fgColorArea, model);
		}
	};
	
	private final Action onCircleToolClick = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedTool = new CircleTool(fgColorArea, model);
		}
	};
	
	private final Action onFilledCircleToolClick = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			selectedTool = new FilledCircleTool(fgColorArea, bgColorArea, model);
		}
	};
	
	//----------------------------------------------------------------
	//						  	  Open
	//----------------------------------------------------------------
	
	private final Action open = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			
			if(jfc.showOpenDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
				Path source = jfc.getSelectedFile().toPath();
				
				if(!source.toString().endsWith(".jvd")) {
					JOptionPane.showMessageDialog(JVDraw.this, "File with extension '.jvd' required.");
					return;
				}
				
				try {
					recreateModelFromText(Files.readAllLines(jfc.getSelectedFile().toPath()));
					
				} catch (IOException ex) {
					System.err.println("Could not read from the file.");
				}
				
				changeCurrentPathTo(source);
			}
		}
		
		/**
		 * Parses the '.jvd' file text, recreates the geometrical objects
		 * from the file text and adds them to the data model.
		 *
		 * @param lines the file text lines
		 */
		private void recreateModelFromText(List<String> lines) {
			model.clear();
			
			for(String line : lines) {
				String[] parts = line.split("\\s");
				
				if(parts[0].equals("LINE")) {
					Point start = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					Point end = new Point(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
					Color color = new Color(Integer.parseInt(parts[5]), Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));
					model.add(new Line(start, end, color));
					
				} else if(parts[0].equals("CIRCLE")) {
					Point center = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					int radius = Integer.parseInt(parts[3]);
					Color outlineColor = new Color(Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
					model.add(new Circle(center, radius, outlineColor));
					
				} else if(parts[0].equals("FCIRCLE")) {
					Point center = new Point(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					int radius = Integer.parseInt(parts[3]);
					Color outlineColor = new Color(Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
					Color areaColor = new Color(Integer.parseInt(parts[7]), Integer.parseInt(parts[8]), Integer.parseInt(parts[9]));
					model.add(new FilledCircle(center, radius, outlineColor, areaColor));
					
				}
			}
			
			model.clearModifiedFlag();
		}
	};

	//----------------------------------------------------------------
	//						 Saving as text
	//----------------------------------------------------------------
	
	private final Action save = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentPath == null) {
				saveToUserSpecifiedDestination();
				
			} else {
				saveTo(currentPath);
			}
		}
	};
	
	private final Action saveAs = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveToUserSpecifiedDestination();
		}
	};
	
	/**
	 * Prompts the user to choose the save destination.
	 */
	private void saveToUserSpecifiedDestination() {
		JFileChooser jfc = new JFileChooser();
		
		if(jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
			Path dest = jfc.getSelectedFile().toPath();
			dest = dest.resolveSibling(dest.getFileName() + ".jvd");
			
			saveTo(dest);
		}
	}
	
	/**
	 * Saves this project's file to the given destination.
	 *
	 * @param destination the destination path
	 */
	private void saveTo(Path destination) {
		var builder = new GeometricalObjectSaveFileBuilder();
		
		for(int i = 0; i < model.getSize(); i++) {
			model.getObject(i).accept(builder);
		}
		
		String fileText = builder.getFileText();
		
		try {
			Files.write(destination, fileText.getBytes());
		} catch (IOException ex) {
			System.err.println("Could not write to the file.");
			return;
		}
		
		changeCurrentPathTo(destination);
		model.clearModifiedFlag();
		
		JOptionPane.showMessageDialog(JVDraw.this, "Project successfully saved!");
	}
	
	//----------------------------------------------------------------
	//						 Exporting as image
	//----------------------------------------------------------------
	
	private final Action export = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			
			Path destination = null;
			String extension = null;
			
			while(true) {
				if(jfc.showSaveDialog(JVDraw.this) == JFileChooser.APPROVE_OPTION) {
					String name = jfc.getSelectedFile().toString();
					
					int dotIndex = name.lastIndexOf('.');
					if(dotIndex < 0) {
						JOptionPane.showMessageDialog(JVDraw.this, "Path does not contain an extension.");
						continue;
					}
					
					extension = name.substring(dotIndex + 1);
					
					if(extension.equals("png") || extension.equals("gif") || extension.equals("jpg")) {
						destination = jfc.getSelectedFile().toPath();
						break;
					} else {
						JOptionPane.showMessageDialog(JVDraw.this, "Image extension must be 'png', 'gif' or 'jpg'");
						continue;
					}
				}
			}
			
			exportAsImage(destination, extension);
		}

		/**
		 * Exports the current scene (image consisting of geometrical objects)
		 * as an image to the given destination.
		 *
		 * @param destination the destination
		 * @param extension the image extension; can be 'png', 'gif' or 'jpg'
		 */
		private void exportAsImage(Path destination, String extension) {
			var BBcalculator = new GeometricalObjectBBCalculator();
			for(int i = 0; i < model.getSize(); i++) {
				model.getObject(i).accept(BBcalculator);
			}

			Rectangle box = BBcalculator.getBoundingBox();

			BufferedImage image = new BufferedImage(box.width, box.height, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D g = image.createGraphics();
			g.translate(-box.x, -box.y);
			
			var painter = new GeometricalObjectPainter(g);
			for(int i = 0; i < model.getSize(); i++) {
				model.getObject(i).accept(painter);
			}
			
			g.dispose();
			
			try {
				ImageIO.write(image, extension, destination.toFile());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(JVDraw.this, "Error during image exporting. Image was not exported.");
				return;
			}
			
			JOptionPane.showMessageDialog(JVDraw.this, "Image has been exported.");
		}
	};
	
	//----------------------------------------------------------------
	//						    Exiting
	//----------------------------------------------------------------
	
	private final Action exit = new AbstractAction() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean aborted = false;
			
			if(model.isModified()) {
				int decision = getUserDecisionForUnsavedFile();
				
				if(decision == JOptionPane.YES_OPTION) {
					save.actionPerformed(null);
					
				} else if(decision == JOptionPane.NO_OPTION) {
					aborted = false;
					
				} else if(decision == JOptionPane.CANCEL_OPTION) {
					aborted = true;
				
				}
			}
			
			if(!aborted) {
				dispose();
			}
		}
	};
	
	//----------------------------------------------------------------
	//						  Helper methods
	//----------------------------------------------------------------
	
	/**
	 * Prompts the user to decide what should happen with the
	 * unsaved image.
	 *
	 * @return the result of the decision
	 */
	private int getUserDecisionForUnsavedFile() {
		String[] options = {"Save Project & Exit", "Discard Project & Exit", "Cancel Exiting"};
		
		return JOptionPane.showOptionDialog(JVDraw.this,
											"Image was not saved. What would you like to do?", 
											"Select action",
											JOptionPane.YES_NO_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE,
											null,
											options,
										    options[0]);
	}
	
	/**
	 * Changes the current path and updates the title.
	 *
	 * @param path the new current path
	 */
	private void changeCurrentPathTo(Path path) {
		this.currentPath = path;
		setTitle("JPaint++ | " + currentPath);
	}
	
	//----------------------------------------------------------------
	//						  Tool supplier
	//----------------------------------------------------------------

	@Override
	public Tool get() {
		return selectedTool;
	}
	
	//----------------------------------------------------------------
	//						 	 main
	//----------------------------------------------------------------
	
	/**
	 * Main simply starts the application by creating a new application window.
	 *
	 * @param args none are used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JVDraw().setVisible(true));
	}
}