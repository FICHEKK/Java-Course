package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Path;
import java.util.SortedMap;

import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;

/**
 * Models objects that act as an environment.
 *
 * @author Filip Nemec
 */
public interface Environment {
	
	/**
	 * Reads the line from the environment.
	 *
	 * @return a line represented by a {@code String}
	 * @throws ShellIOException if error during reading occurs
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes the given text to the environment.
	 *
	 * @param text text to be written
	 * @throws ShellIOException if error during writing occurs
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes the given text and a new line to the
	 * environment.
	 *
	 * @param text text to be written
	 * @throws ShellIOException if error during writing occurs
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returns a map of commands that this environment
	 * supports.
	 *
	 * @return map of commands that this environment
	 * 		   supports
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the multi-line symbol for this environment.
	 *
	 * @return the multi-line symbol for this environment.
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets the new multiline symbol.
	 *
	 * @param symbol new multi-line symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns the prompt symbol for this environment.
	 *
	 * @return the prompt symbol for this environment.
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets the new prompt symbol.
	 *
	 * @param symbol new prompt symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns the more-lines symbol for this environment.
	 *
	 * @return the more-lines symbol for this environment.
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets the new more-lines symbol.
	 *
	 * @param symbol new more-lines symbol
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Returns the current directory.
	 *
	 * @return the current directory
	 */
	Path getCurrentDirectory();
	
	/**
	 * Sets the current directory path.
	 *
	 * @param path the new current directory path
	 */
	void setCurrentDirectory(Path path);
	
	
	Object getSharedData(String key);
	void setSharedData(String key, Object value);
}
