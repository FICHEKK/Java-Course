package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * The painter - paints the collection of geometrical objects
 * on the screen.
 *
 * @author Filip Nemec
 */
public class GeometricalObjectPainter implements GeometricalObjectVisitor {
	
	/** The graphics object used for painting. */
	private Graphics2D g2d;
	
	/**
	 * Constructs a new painting visitor.
	 *
	 * @param g2d the graphics object used for painting
	 */
	public GeometricalObjectPainter(Graphics2D g2d) {
		this.g2d = Objects.requireNonNull(g2d, "Graphics2D object cannot be null.");
	}

	@Override
	public void visit(Line line) {
		g2d.setColor(line.getColor());
		
		Point start = line.getStart();
		Point end = line.getEnd();
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void visit(Circle circle) {
		g2d.setColor(circle.getOutlineColor());
		
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		Point center = filledCircle.getCenter();
		int radius = filledCircle.getRadius();
		
		g2d.setColor(filledCircle.getAreaColor());
		g2d.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
		
		g2d.setColor(filledCircle.getOutlineColor());
		g2d.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
	}
}