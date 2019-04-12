package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the given axiom command and
 * sets the {@code LSystemBuilder's} axiom if the
 * given word sequence is of a valid format.
 * 
 * @author Filip Nemec
 */
public class AxiomCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words.length != 2) {
			String msg = "Axiom command must be of format: \"axiom <String>\"";
			throw new IllegalArgumentException(msg);
		}
		
		builder.setAxiom(words[1]);
	}
}
