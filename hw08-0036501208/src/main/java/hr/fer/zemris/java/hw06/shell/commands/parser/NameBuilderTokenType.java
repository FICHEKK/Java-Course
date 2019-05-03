package hr.fer.zemris.java.hw06.shell.commands.parser;

/**
 * All of the possible name builder token
 * types.
 *
 * @author Filip Nemec
 */
enum NameBuilderTokenType {
	
	/** The plain text token. */
	TEXT,
	
	/** Indicates the opening of a group tag. */
	GROUP_OPEN,
	
	/** The {@code String} values found inside group tag. */
	GROUP_STRING,
	
	/** Indicates the comma in the group tag. */
	GROUP_COMMA,
	
	/** Indicates the closing of a group tag. */
	GROUP_CLOSE,
	
	/** This token will get returned once the end is reached. */
	EOF,
}
