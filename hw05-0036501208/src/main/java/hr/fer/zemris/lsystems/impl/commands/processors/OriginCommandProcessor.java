package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given origin command and
 * sets the {@code LSystemBuilder's} origin if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class OriginCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words.length != 3) {
			String msg = "Origin command must be of format: \"origin <double [0, 1]> <double[0, 1]>\"";
			throw new IllegalArgumentException(msg);
		}
		
		try {
			double x = Double.parseDouble(words[1]);
			double y = Double.parseDouble(words[2]);
			builder.setOrigin(x, y);
		} catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid origin input, could not parse double.");
		}
	}
}
