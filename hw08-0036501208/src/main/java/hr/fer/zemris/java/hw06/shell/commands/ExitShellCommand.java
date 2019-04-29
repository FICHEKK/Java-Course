package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that closes the given shell.
 *
 * @author Filip Nemec
 */
public class ExitShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		env.writeln("Closing the shell...");
		return ShellStatus.TERMINATE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Exits the shell and terminates the process.");
		desc.add("");
		desc.add("Usage: 'exit'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "exit";
	}
}
