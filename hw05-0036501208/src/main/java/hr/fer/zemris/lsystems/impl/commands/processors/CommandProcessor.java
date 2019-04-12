package hr.fer.zemris.lsystems.impl.commands.processors;

import hr.fer.zemris.lsystems.LSystemBuilder;

/**
 * Models objects that processes the given
 * sequence of words and returns an appropriate
 * command.
 * 
 * @author Filip Nemec
 */
public interface CommandProcessor {
	/**
	 * Processes the given word sequence and returns the
	 * appropriate {@code Command} if
	 * 
	 * @param builder the builder we are applying the operation on
	 * @param words given sequence of words
	 * @return appropriate {@code Command} if there is one, {@code null}
	 * 		   if the given word sequence just changes the builder's state
	 */
	void process(LSystemBuilder builder, String[] words);
}
