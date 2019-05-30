package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * State in which this lexer is currently in.
 * TEXT = processing text
 * TAG = processing tag
 */
public enum ScriptLexerState {
	
	/** This lexer is currently processing text. */
	PROCESSING_TEXT,
	
	/** This lexer is currently processing a tag. */
	PROCESSING_TAG,
}
