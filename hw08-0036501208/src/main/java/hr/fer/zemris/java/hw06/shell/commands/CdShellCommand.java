package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellIOException;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that changes the current working directory.
 *
 * @author Filip Nemec
 */
public class CdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String argument = ArgumentParser.extractArgs(arguments, 1, 1)[0];
			
			Path workingDir = null;
			
			if(argument.equals("..")) {
				Path parentDir = env.getCurrentDirectory().getParent();
				
				if(parentDir == null) {
					env.writeln("There is no parent directory.");
					return ShellStatus.CONTINUE;
				}
				
				workingDir = parentDir;

			} else {
				workingDir = env.getCurrentDirectory().resolve(argument);
				
			}
			
			env.setCurrentDirectory(workingDir);
			env.writeln("Current directory changed to '" + workingDir + "'");
			
		} catch(ShellIOException | IllegalArgumentException e) {
			env.writeln(e.getMessage());
			
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Changes the current working directory to the given one.");
		desc.add("- Given argument must be a directory path.");
		desc.add("");
		desc.add("Usage: 'cd <dir path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "cd";
	}
}
