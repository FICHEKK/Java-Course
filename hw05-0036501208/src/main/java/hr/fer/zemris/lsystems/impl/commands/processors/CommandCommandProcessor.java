package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Processes the command of format: <br>
 * a) command 'char' 'string' <br>
 * b) command 'char' 'string' 'value'
 * 
 * @author Filip Nemec
 */
public class CommandCommandProcessor implements CommandProcessor {

	@Override
	public void process(LSystemBuilder builder, String[] words) {
		if(words[1].length() > 1) {
			throw new IllegalArgumentException("Character should be of size 1.");
		}
		
		char symbol = words[1].charAt(0);
		String action = "";
		
		if(words.length == 3) {
			action = words[2];
		} else if(words.length == 4) {
			action = words[2] + " " + words[3];
		} else {
			throw new IllegalArgumentException("Commands should have 3 or 4 tokens only.");
		}
		
		builder.registerCommand(symbol, action);
	}
}
