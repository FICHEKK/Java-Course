package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A simple command that removes the last placed
 * turtle state from the stack.
 * 
 * @author Filip Nemec
 */
public class PopCommand implements Command {
	
	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.popState();
	}
}
