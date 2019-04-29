package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command used for creating directories.
 *
 * @author Filip Nemec
 */
public class MkdirShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String argument = ArgumentParser.extractArgs(arguments, 1, 1)[0];
			Path dir = env.getCurrentDirectory().resolve(argument);
			
			if(Files.isDirectory(dir)) {
				env.writeln("The directory '" + dir.getFileName() + "' already exists. New directory was not created.");
				return ShellStatus.CONTINUE;
			}

			Files.createDirectories(dir);
			env.writeln("Successfully created a new directory '" + dir.getFileName() + "'.");

		} catch(IllegalArgumentException e) {
			env.writeln(e.getMessage());
			
		} catch(IOException e) {
			env.writeln("Error during the creation of directories.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Creates a new directory.");
		desc.add("- It accepts a single argument: directory name.");
		desc.add("- After getting the name, it creates the appropriate directory structure.");
		desc.add("");
		desc.add("Usage: 'mkdir <dir path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "mkdir";
	}
}
