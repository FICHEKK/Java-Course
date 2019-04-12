package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A simple command that rotates the turtle by
 * the given angle.
 * 
 * @author Filip Nemec
 */
public class RotateCommand implements Command {
	
	/** When this command is executed, turtle will get rotated by this angle. */
	private double angle;
	
	/**
	 * Constructs a new rotate command with the given angle.
	 * @param angle the angle of rotation
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().getDirection().rotate(Math.toRadians(angle));
	}
}
