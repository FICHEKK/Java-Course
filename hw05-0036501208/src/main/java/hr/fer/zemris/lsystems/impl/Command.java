package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Models a single command that can be executed.
 * 
 * @author Filip Nemec
 */
@FunctionalInterface
public interface Command {
	/**
	 * Executes a command.
	 * 
	 * @param ctx command context
	 * @param painter the painter
	 */
	void execute(Context ctx, Painter painter);
}
