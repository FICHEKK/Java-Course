package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * A simple command that moves the turtle by the
 * given step and draws the trail said turtle
 * moved onto.
 * 
 * @author Filip Nemec
 */
public class DrawCommand implements Command {
	
	/** Length of the turtle step. */
	private double step;
	
	/** Size of the trail. */
	private static final float TRAIL_SIZE = 1f;
	
	/**
	 * Constructs a draw command where the turtle
	 * is moved by the given step.
	 * 
	 * @param step turtle step length
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		
		Vector2D position = state.getPosition();
		Vector2D oldPosition = position.copy();
		
		double theta = state.getDirection().angle();
		double jumpLength = step * state.getStep();
		position.translate(new Vector2D(Math.cos(theta) * jumpLength, Math.sin(theta) * jumpLength));

		painter.drawLine(oldPosition.getX(), oldPosition.getY(),
						    position.getX(),    position.getY(), state.getColor(), TRAIL_SIZE);
	}
}
