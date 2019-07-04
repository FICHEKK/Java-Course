package hr.fer.zemris.java.hw17.jvdraw.editors;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * An editor used to edit a single line.
 *
 * @author Filip Nemec
 */
public class LineEditor extends GeometricalObjectEditor {
	
	/** Used for serialization. */
	private static final long serialVersionUID = 7340414054886955926L;
	
	/** The subject of editing. */
	private Line line;
	
	/** The field for editing the line's x0 coordinate. */
	private JTextField startXField;
	
	/** The field for editing the line's y0 coordinate. */
	private JTextField startYField;
	
	/** The field for editing the line's x1 coordinate. */
	private JTextField endXField;
	
	/** The field for editing the line's y1 coordinate. */
	private JTextField endYField;
	
	/** The field for editing the line's red color component. */
	private JTextField redField;
	
	/** The field for editing the line's green color component. */
	private JTextField greenField;
	
	/** The field for editing the line's blue color component. */
	private JTextField blueField;

	/**
	 * Constructs a new editor for the given line.
	 *
	 * @param line the line to be edited
	 */
	public LineEditor(Line line) {
		this.line = Objects.requireNonNull(line, "Line cannot be null.");
		
		this.setLayout(new GridLayout(0, 2));
		
		// Line starting point
		this.add(new JLabel("Start X:"));
		this.add(startXField = new JTextField(String.valueOf(line.getStart().x)));
		this.add(new JLabel("Start Y:"));
		this.add(startYField = new JTextField(String.valueOf(line.getStart().y)));
		
		// Line ending point
		this.add(new JLabel("End X:"));
		this.add(endXField = new JTextField(String.valueOf(line.getEnd().x)));
		this.add(new JLabel("End Y:"));
		this.add(endYField = new JTextField(String.valueOf(line.getEnd().y)));
		
		// Line color
		Color color = line.getColor();
		this.add(new JLabel("Color R:"));
		this.add(redField = new JTextField(String.valueOf(color.getRed())));
		this.add(new JLabel("Color G:"));
		this.add(greenField = new JTextField(String.valueOf(color.getGreen())));
		this.add(new JLabel("Color B:"));
		this.add(blueField = new JTextField(String.valueOf(color.getBlue())));
	}

	@Override
	public void checkEditing() {
		try {
			Integer.parseInt(startXField.getText());
			Integer.parseInt(startYField.getText());
			Integer.parseInt(endXField.getText());
			Integer.parseInt(endYField.getText());
		} catch(NumberFormatException e) {
			throw new IllegalStateException("Starting and ending point coordinates must be integers.");
		}
		
		int r = Integer.parseInt(redField.getText());
		if(r < 0 || r > 255) throw new IllegalStateException("Red must be in range [0, 255].");
		
		int g = Integer.parseInt(greenField.getText());
		if(g < 0 || g > 255) throw new IllegalStateException("Green must be in range [0, 255].");
		
		int b = Integer.parseInt(blueField.getText());
		if(b < 0 || b > 255) throw new IllegalStateException("Blue must be in range [0, 255].");
	}

	@Override
	public void acceptEditing() {
		line.setStart(new Point(Integer.parseInt(startXField.getText()),
								Integer.parseInt(startYField.getText())));
		
		line.setEnd(new Point(Integer.parseInt(endXField.getText()),
							  Integer.parseInt(endYField.getText())));

		int r = Integer.parseInt(redField.getText());
		int g = Integer.parseInt(greenField.getText());
		int b = Integer.parseInt(blueField.getText());
		line.setColor(new Color(r, g, b));
	}
}