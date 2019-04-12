package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents the states the lexer can operate
 * in. There are currently 2 modes: BASIC and
 * EXTENDED.
 */
public enum LexerState {
	/** Default state lexer operates in. */
	BASIC,
	
	/** Adds '#' functionality. */
	EXTENDED
}
