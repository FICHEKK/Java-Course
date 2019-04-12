package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given angle command and
 * sets the {@code LSystemBuilder's} angle if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class AngleCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words.length != 2) {
			String msg = "Angle command must be of format: \"angle <double>\"";
			throw new IllegalArgumentException(msg);
		}
		
		try {
			double angle = Double.parseDouble(words[1]) % 360; // Mod just in case someone put > 360 angle.
			builder.setAngle(angle);
		} catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("Invalid angle input, could not parse double.");
		}
	}
}
