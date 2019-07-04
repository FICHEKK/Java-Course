package hr.fer.zemris.java.hw17.jvdraw.visitors;

import java.awt.Point;
import java.awt.Rectangle;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * The bounding box visitor - calculates the bounding
 * box for the collection of geometrical objects.
 *
 * @author Filip Nemec
 */
public class GeometricalObjectBBCalculator implements GeometricalObjectVisitor {
	
	/** The minimal X coordinate - the "left" side of the bounding box. */
	private int minX = Integer.MAX_VALUE;
	
	/** The maximal X coordinate - the "right" side of the bounding box. */
	private int maxX = Integer.MIN_VALUE;
	
	/** The minimal Y coordinate - the "top" side of the bounding box. */
	private int minY = Integer.MAX_VALUE;
	
	/** The maximal Y coordinate - the "bottom" side of the bounding box. */
	private int maxY = Integer.MIN_VALUE;

	@Override
	public void visit(Line line) {
		processLine(line.getStart(), line.getEnd());
	}

	@Override
	public void visit(Circle circle) {
		// Convert a circle to a line that is equal to the diagonal
		// of the circle's bounding box.
		Point center = circle.getCenter();
		int radius = circle.getRadius();
		
		Point start = new Point(center.x - radius, center.y - radius);
		Point end = new Point(center.x + radius, center.y + radius);
		
		processLine(start, end);
	}

	@Override
	public void visit(FilledCircle filledCircle) {
		visit((Circle) filledCircle);
	}
	
	/**
	 * Reshapes the bounding box based on the given line.
	 *
	 * @param start the line start
	 * @param end the line end
	 */
	private void processLine(Point start, Point end) {
		minX = Math.min( Math.min(start.x, end.x), minX );
		maxX = Math.max( Math.max(start.x, end.x), maxX );
		minY = Math.min( Math.min(start.y, end.y), minY );
		maxY = Math.max( Math.max(start.y, end.y), maxY );
	}
	
	/**
	 * Returns the generated bounding box.
	 *
	 * @return the bounding box
	 */
	public Rectangle getBoundingBox() {
		int width = maxX - minX;
		int height = maxY - minY;
		return new Rectangle(minX, minY, width, height);
	}
}
