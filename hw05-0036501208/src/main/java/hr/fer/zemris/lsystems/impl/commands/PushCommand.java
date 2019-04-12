package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * A simple command that copies the current state
 * and pushed it to the stack.
 * 
 * @author Filip Nemec
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context ctx, Painter painter) {
		TurtleState currentCopy = ctx.getCurrentState().copy();
		ctx.pushState(currentCopy);
	}
}
