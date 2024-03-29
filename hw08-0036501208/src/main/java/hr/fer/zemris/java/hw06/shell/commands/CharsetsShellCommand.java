package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that displays all of the supported
 * character sets, which is platform dependent.
 *
 * @author Filip Nemec
 */
public class CharsetsShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Charset.availableCharsets().forEach((k, v) -> env.writeln(k));
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Lists the names of all the supported charsets.");
		desc.add("");
		desc.add("Usage: 'charsets'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "charsets";
	}
}
