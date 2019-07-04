package hr.fer.zemris.java.hw17.jvdraw.editors;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JTextField;

import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;

/**
 * An editor used to edit a single filled circle.
 *
 * @author Filip Nemec
 */
public class FilledCircleEditor extends GeometricalObjectEditor {

	/** Used for serialization. */
	private static final long serialVersionUID = 7749348707448679825L;
	
	/** The subject of editing. */
	private FilledCircle filledCircle;
	
	/** The field for editing the circle's center X coordinate. */
	private JTextField centerXField;
	
	/** The field for editing the circle's center Y coordinate. */
	private JTextField centerYField;
	
	/** The field for editing the circle's radius. */
	private JTextField radiusField;
	
	/** The field for editing the circle's outline red color component. */
	private JTextField outlineRedField;
	
	/** The field for editing the circle's outline green color component. */
	private JTextField outlineGreenField;
	
	/** The field for editing the circle's outline blue color component. */
	private JTextField outlineBlueField;
	
	/** The field for editing the circle's area red color component. */
	private JTextField areaRedField;
	
	/** The field for editing the circle's area green color component. */
	private JTextField areaGreenField;
	
	/** The field for editing the circle's area blue color component. */
	private JTextField areaBlueField;

	/**
	 * Constructs a new editor for the given filled circle.
	 *
	 * @param filledCircle the filled circle to be edited
	 */
	public FilledCircleEditor(FilledCircle filledCircle) {
		this.filledCircle = Objects.requireNonNull(filledCircle, "Filled circle cannot be null.");
		
		this.setLayout(new GridLayout(0, 2));
		
		// Circle shape
		this.add(new JLabel("Center X:"));
		this.add(centerXField = new JTextField(String.valueOf(filledCircle.getCenter().x)));
		this.add(new JLabel("Center Y:"));
		this.add(centerYField = new JTextField(String.valueOf(filledCircle.getCenter().y)));
		this.add(new JLabel("Radius:"));
		this.add(radiusField = new JTextField(String.valueOf(filledCircle.getRadius())));
		
		// Outline color
		Color outlineColor = filledCircle.getOutlineColor();
		this.add(new JLabel("Outline R:"));
		this.add(outlineRedField = new JTextField(String.valueOf(outlineColor.getRed())));
		this.add(new JLabel("Outline G:"));
		this.add(outlineGreenField = new JTextField(String.valueOf(outlineColor.getGreen())));
		this.add(new JLabel("Outline B:"));
		this.add(outlineBlueField = new JTextField(String.valueOf(outlineColor.getBlue())));
		
		// Area color
		Color areaColor = filledCircle.getAreaColor();
		this.add(new JLabel("Area R:"));
		this.add(areaRedField = new JTextField(String.valueOf(areaColor.getRed())));
		this.add(new JLabel("Area G:"));
		this.add(areaGreenField = new JTextField(String.valueOf(areaColor.getGreen())));
		this.add(new JLabel("Area B:"));
		this.add(areaBlueField = new JTextField(String.valueOf(areaColor.getBlue())));
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
		
		// Checking the outline color.
		int or = Integer.parseInt(outlineRedField.getText());
		if(or < 0 || or > 255) throw new IllegalStateException("Outline red must be in range [0, 255].");
		
		int og = Integer.parseInt(outlineGreenField.getText());
		if(og < 0 || og > 255) throw new IllegalStateException("Outline green must be in range [0, 255].");
		
		int ob = Integer.parseInt(outlineBlueField.getText());
		if(ob < 0 || ob > 255) throw new IllegalStateException("Outline blue must be in range [0, 255].");
		
		// Checking the area color.
		int ar = Integer.parseInt(outlineRedField.getText());
		if(ar < 0 || ar > 255) throw new IllegalStateException("Area red must be in range [0, 255].");
		
		int ag = Integer.parseInt(outlineGreenField.getText());
		if(ag < 0 || ag > 255) throw new IllegalStateException("Area green must be in range [0, 255].");
		
		int ab = Integer.parseInt(outlineBlueField.getText());
		if(ab < 0 || ab > 255) throw new IllegalStateException("Area blue must be in range [0, 255].");
	}

	@Override
	public void acceptEditing() {
		filledCircle.setCenter(new Point(Integer.parseInt(centerXField.getText()),
										 Integer.parseInt(centerYField.getText())));	
		filledCircle.setRadius(Integer.parseInt(radiusField.getText()));
		
		int or = Integer.parseInt(outlineRedField.getText());
		int og = Integer.parseInt(outlineGreenField.getText());
		int ob = Integer.parseInt(outlineBlueField.getText());
		filledCircle.setOutlineColor(new Color(or, og, ob));
		
		int ar = Integer.parseInt(areaRedField.getText());
		int ag = Integer.parseInt(areaGreenField.getText());
		int ab = Integer.parseInt(areaBlueField.getText());
		filledCircle.setAreaColor(new Color(ar, ag, ab));
	}
}