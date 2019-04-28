package hr.fer.zemris.java.hw06.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Models objects that represent commands
 * for the shell environment.
 *
 * @author Filip Nemec
 */
public interface ShellCommand {
	
	/**
	 * Executes the command that does a particular
	 * defined job.
	 *
	 * @param env the shell that this command should be executed on
	 * @param arguments arguments for the command
	 * @return the status of the given shell after the execution of this command
	 * @throws NullPointerException if {@code env} or {@code arguments} is {@code null}
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns this command's name.
	 *
	 * @return this command's name.
	 */
	String getCommandName();
	
	/**
	 * Returns this command's description.
	 *
	 * @return this command's description.
	 */
	List<String> getCommandDescription();
}
