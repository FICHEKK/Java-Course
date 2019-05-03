package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * Simple factory class that offers basic
 * implementations of name-builder objects.
 *
 * @author Filip Nemec
 */
class DefaultNameBuilders {
	
	/**
	 * A blank name-builder that is usually
	 * used as a starting point of the
	 * composite name-builder.
	 *
	 * @return the blank name-builder
	 */
	static NameBuilder blank() {
		return (result, sb) -> { return; };
	}
	
	/**
	 * A name-builder that appends the given
	 * text.
	 *
	 * @param text the text to be appended
	 * @return a {@code NameBuilder} which appends the given text
	 */
	static NameBuilder text(String text) {
		return (result, sb) -> sb.append(text);
	}
	
	/**
	 * A name-builder that appends the result group
	 * at the given index.
	 *
	 * @param index the index of the group in the result
	 * @return a {@code NameBuilder} which appends
	 * 		   the result group at the given index
	 */
	static NameBuilder group(int index) {
		return (result, sb) -> sb.append(result.group(index));
	}
	
	/**
	 * A name-builder that appends the result group
	 * at the given index, with padding and minimum
	 * {@code String} width.
	 *
	 * @param index the index of the group in the result
	 * @param padding the padding {@code char}
	 * @param minWidth the minimum width of the appending {@code String}
	 * @return a {@code NameBuilder} which appends the result group at
	 * 		   the given index, with padding and minimum width
	 */
	static NameBuilder group(int index, char padding, int minWidth) {
		return (result, sb) -> sb.append(String.format("%" + minWidth + "s", result.group(index)).replace(' ', padding));
	}
}
