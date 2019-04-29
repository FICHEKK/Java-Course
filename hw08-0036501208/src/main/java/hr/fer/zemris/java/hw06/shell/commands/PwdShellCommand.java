package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that prints the current working directory.
 *
 * @author Filip Nemec
 */
public class PwdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		env.writeln(env.getCurrentDirectory().toString());
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Prints the current working directory.");
		desc.add("");
		desc.add("Usage: 'pwd'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "pwd";
	}
}
