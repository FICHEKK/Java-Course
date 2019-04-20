package hr.fer.zemris.java.hw06.shell;

/**
 * Defines what action should shell take after a command
 * has been executed.
 *
 * @author Filip Nemec
 */
public enum ShellStatus {
	/** Continue normal work. */
	CONTINUE,
	
	/** Terminate the shell. */
	TERMINATE,
}
