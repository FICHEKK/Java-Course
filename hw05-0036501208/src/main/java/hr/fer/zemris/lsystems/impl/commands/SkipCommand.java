package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * A simple command that moves the turtle by the
 * given step, but does <b>not</b> draw the trail it moved on
 * 
 * @author Filip Nemec
 */
public class SkipCommand implements Command {
	
	/** Length of the turtle step. */
	private double step;
	
	/**
	 * Constructs a skip command where the turtle
	 * is moved by the given step.
	 * 
	 * @param step turtle step length
	 */
	public SkipCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		
		Vector2D position = state.getPosition();
		
		double theta = state.getDirection().angle();
		double jumpLength = step * state.getStep();
		position.translate(new Vector2D(Math.cos(theta) * jumpLength, Math.sin(theta) * jumpLength));
	}
}