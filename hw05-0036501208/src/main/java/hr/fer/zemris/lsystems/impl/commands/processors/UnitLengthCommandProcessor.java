package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given unit length command and
 * sets the {@code LSystemBuilder's} unit length if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class UnitLengthCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words.length != 2) {
			String msg = "Unit length command must be of format: \"unitLength <double>\"";
			throw new IllegalArgumentException(msg);
		}
		
		try {
			double unitLength = Double.parseDouble(words[1]);
			builder.setUnitLength(unitLength);
		} catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid unit length input, could not parse double.");
		}
	}
}
