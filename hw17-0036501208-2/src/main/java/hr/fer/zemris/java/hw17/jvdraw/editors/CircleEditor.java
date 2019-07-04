package hr.fer.zemris.java.hw17.jvdraw.editors;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;

/**
 * An editor used to edit a single circle.
 *
 * @author Filip Nemec
 */
public class CircleEditor extends GeometricalObjectEditor {
	
	/** Used for serialization. */
	private static final long serialVersionUID = -4844638062678206240L;
	
	/** The subject of editing. */
	private Circle circle;
	
	/** The field for editing the circle's center X coordinate. */
	private JTextField centerXField;
	
	/** The field for editing the circle's center Y coordinate. */
	private JTextField centerYField;
	
	/** The field for editing the circle's radius. */
	private JTextField radiusField;
	
	/** The field for editing the circle's red color component. */
	private JTextField redField;
	
	/** The field for editing the circle's green color component. */
	private JTextField greenField;
	
	/** The field for editing the circle's blue color component. */
	private JTextField blueField;

	/**
	 * Constructs a new editor for the given circle.
	 *
	 * @param circle the circle to be edited
	 */
	public CircleEditor(Circle circle) {
		this.circle = Objects.requireNonNull(circle, "Circle cannot be null.");
		
		this.setLayout(new GridLayout(0, 2));
		
		// Circle shape
		this.add(new JLabel("Center X:"));
		this.add(centerXField = new JTextField(String.valueOf(circle.getCenter().x)));
		this.add(new JLabel("Center Y:"));
		this.add(centerYField = new JTextField(String.valueOf(circle.getCenter().y)));
		this.add(new JLabel("Radius:"));
		this.add(radiusField = new JTextField(String.valueOf(circle.getRadius())));
		
		// Circle color
		Color color = circle.getOutlineColor();
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
			Integer.parseInt(centerXField.getText());
			Integer.parseInt(centerYField.getText());
		} catch(NumberFormatException e) {
			throw new IllegalStateException("Center coordinates must be integers.");
		}
		
		int radius = Integer.parseInt(radiusField.getText());
		if(radius <= 0) throw new IllegalStateException("Radius must be bigger than 0.");
		
		int r = Integer.parseInt(redField.getText());
		if(r < 0 || r > 255) throw new IllegalStateException("Red must be in range [0, 255].");
		
		int g = Integer.parseInt(greenField.getText());
		if(g < 0 || g > 255) throw new IllegalStateException("Green must be in range [0, 255].");
		
		int b = Integer.parseInt(blueField.getText());
		if(b < 0 || b > 255) throw new IllegalStateException("Blue must be in range [0, 255].");
	}

	@Override
	public void acceptEditing() {
		circle.setCenter(new Point(Integer.parseInt(centerXField.getText()),
								   Integer.parseInt(centerYField.getText())));
		circle.setRadius(Integer.parseInt(radiusField.getText()));
		
		int r = Integer.parseInt(redField.getText());
		int g = Integer.parseInt(greenField.getText());
		int b = Integer.parseInt(blueField.getText());
		circle.setOutlineColor(new Color(r, g, b));
	}
}