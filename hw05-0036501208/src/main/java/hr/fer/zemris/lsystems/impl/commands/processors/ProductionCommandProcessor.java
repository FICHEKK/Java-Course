package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given production command and
 * sets the {@code LSystemBuilder's} production if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class ProductionCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words.length != 3) {
			String msg = "Production command must be of format: \"production <char> <sequence>\"";
			throw new IllegalArgumentException(msg);
		}
		
		String sign = words[1];
		if(sign.length() > 1) {
			var sb = new StringBuilder();
			sb.append("'" + sign + "' is an invalid input as a first argument in production.\n");
			sb.append("Length can't be longer than 1.");
			throw new IllegalArgumentException(sb.toString());
		}
		
		Character signChar = Character.valueOf(sign.charAt(0));
		String mappingSequence = words[2];

		builder.registerProduction(signChar, mappingSequence);
	}
}
