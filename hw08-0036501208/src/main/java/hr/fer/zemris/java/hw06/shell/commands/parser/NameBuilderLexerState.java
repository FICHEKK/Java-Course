package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * All of the states {@code NameBuilderLexer} can
 * operate in.
 *
 * @author Filip Nemec
 */
public enum NameBuilderLexerState {
	
	/** Processing the plain text. */
	PROCESSING_TEXT,
	
	/** Processing the group tag. */
	PROCESSING_GROUP_TAG,
}
