package hr.fer.zemris.java.hw17.jvdraw.visitors;

import hr.fer.zemris.java.hw17.jvdraw.objects.Circle;
import hr.fer.zemris.java.hw17.jvdraw.objects.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.objects.Line;

/**
 * Abstract model of the geometrical object visitor.
 *
 * @author Filip Nemec
 */
public interface GeometricalObjectVisitor {
	
	/**
	 * Defines the action upon visiting a {@code Line}.
	 *
	 * @param line the line being visited
	 */
	void visit(Line line);

	/**
	 * Defines the action upon visiting a {@code Circle}.
	 *
	 * @param circle the circle being visited
	 */
	void visit(Circle circle);

	/**
	 * Defines the action upon visiting a {@code FilledCircle}.
	 *
	 * @param filledCircle the filled circle being visited
	 */
	void visit(FilledCircle filledCircle);
}