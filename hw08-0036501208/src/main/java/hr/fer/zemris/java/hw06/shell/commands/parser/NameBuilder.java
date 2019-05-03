package hr.fer.zemris.java.hw06.shell.commands.parser;

import hr.fer.zemris.java.hw06.shell.commands.FilterResult;

/**
 * Models objects that can build names.
 *
 * @author Filip Nemec
 */
public interface NameBuilder {
	
	/**
	 * Builds the name by concatenating to the
	 * given {@code StringBuilder} object.
	 *
	 * @param result the source of data
	 * @param sb the string concatenation object
	 */
	void execute(FilterResult result, StringBuilder sb);
	
	/**
	 * Allows the composition of {@code NameBuilder} objects.
	 *
	 * @param other the next {@code NameBuilder} in chain
	 * @return the composite {@code NameBuilder}
	 */
	default NameBuilder then(NameBuilder other) {
		return (result, sb) -> {
			execute(result, sb);
			other.execute(result, sb);
		};
	}
}
