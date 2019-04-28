package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command used for creating directories.
 *
 * @author Filip Nemec
 */
public class MkdirShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Creates a new directory.");
		desc.add("- It accepts a single argument: directory name.");
		desc.add("- After getting the name, it creates the appropriate directory structure.");
		desc.add("");
		desc.add("Usage: 'mkdir <dir path>'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = null;
		
		try {
			args = ArgumentParser.getArgs(arguments);
		} catch(IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(args.length != 1) {
			env.writeln("One argument expected. Was " + args.length + ".");
			return ShellStatus.CONTINUE;
		}
		
		Path dir = Paths.get(args[0]);
		if(Files.exists(dir) && Files.isDirectory(dir)) {
			env.writeln("The directory '" + dir.getFileName() + "' already exists. New directory was not created.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.createDirectories(dir);
			env.writeln("Successfully created a new directory '" + dir.getFileName() + "'.");
		} catch (IOException e) {
			env.writeln("Error during the creation of directories.");
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
