package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * A simple command that sets the turtle trail color.
 * 
 * @author Filip Nemec
 */
public class ColorCommand implements Command {
	
	/** A new turtle color. */
	private Color color;

	/**
	 * Constructs a new color command with the given
	 * color.
	 * 
	 * @param color the color
	 */
	public ColorCommand(Color color) {
		this.color = color;
	}

	@Override
	public void execute(Context ctx, Painter painter) {
		ctx.getCurrentState().setColor(color);
	}
}
