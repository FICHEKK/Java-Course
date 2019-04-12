package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given unit length degree scaler command and
 * sets the {@code LSystemBuilder's} unit length degree scaler if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class UnitLengthDegreeScalerCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		String forProcessing = "";
		
		for(int i = 1; i < words.length; i++) {
			forProcessing += words[i];
		}
		
		builder.setUnitLengthDegreeScaler(getUnitLengthDegreeScalerValue(forProcessing));
	}
	
	private double getUnitLengthDegreeScalerValue(String input) {
		input = input.replaceAll(" ", "");
		
		int index;
		if((index = input.indexOf('/')) >= 0) {
			try {
				String numerator = input.substring(0, index);
				String denominator = input.substring(index + 1, input.length());
				return Double.parseDouble(numerator) / Double.parseDouble(denominator);
			} catch(IndexOutOfBoundsException ioobe) {
				String msg = "'" + input + "' is not a valid input format, excpected: number / number";
				throw new IllegalArgumentException(msg);
			} catch(NumberFormatException nfe) {
				String msg = "'" + input + "' could not be parsed to double";
				throw new IllegalArgumentException(msg);
			}
		} else {
			try {
				return Double.parseDouble(input);
			} catch(NumberFormatException nfe) {
				String msg = "'" + input + "' could not be parsed to double";
				throw new IllegalArgumentException(msg);
			}
		}
	}
}
