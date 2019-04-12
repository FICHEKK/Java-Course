package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * A simple command that scales the turtle shift.
 * 
 * @author Filip Nemec
 */
public class ScaleCommand implements Command {
	
	/** A factor by which the shift will get changed. */
	private double factor;
	
	/**
	 * Constructs a new scale command with the given factor.
	 * @param factor the factor
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState state = ctx.getCurrentState();
		state.setStep(state.getStep() * factor);
	}
}
