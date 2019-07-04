package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Color;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * Visitor used to convert geometrical objects to
 * save file text lines.
 *
 * @author Filip Nemec
 */
public class GeometricalObjectSaveFileBuilder implements GeometricalObjectVisitor {
	
	/** The builder used for building the text of the file. */
	private StringBuilder fileTextBuilder = new StringBuilder();

	@Override
	public void visit(Line line) {
		fileTextBuilder.append("LINE ");
		fileTextBuilder.append(line.getStart().x + " " + line.getStart().y + " ");
		fileTextBuilder.append(line.getEnd().x + " " + line.getEnd().y + " ");
		
		Color c = line.getColor();
		fileTextBuilder.append(c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		
		fileTextBuilder.append("\r\n");
	}

	@Override
	public void visit(Circle circle) {
		fileTextBuilder.append("CIRCLE ");
		fileTextBuilder.append(circle.getCenter().x + " " + circle.getCenter().y + " ");
		fileTextBuilder.append(circle.getRadius() + " ");
		
		Color c = circle.getOutlineColor();
		fileTextBuilder.append(c.getRed() + " " + c.getGreen() + " " + c.getBlue());
		
		fileTextBuilder.append("\r\n");
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		fileTextBuilder.append("FCIRCLE ");
		fileTextBuilder.append(filledCircle.getCenter().x + " " + filledCircle.getCenter().y + " ");
		fileTextBuilder.append(filledCircle.getRadius() + " ");
		
		Color oc = filledCircle.getOutlineColor();
		fileTextBuilder.append(oc.getRed() + " " + oc.getGreen() + " " + oc.getBlue() + " ");
		
		Color ac = filledCircle.getAreaColor();
		fileTextBuilder.append(ac.getRed() + " " + ac.getGreen() + " " + ac.getBlue());
		
		fileTextBuilder.append("\r\n");
	}

	/**
	 * @return the built file text
	 */
	public String getFileText() {
		return fileTextBuilder.toString();
	}
}
