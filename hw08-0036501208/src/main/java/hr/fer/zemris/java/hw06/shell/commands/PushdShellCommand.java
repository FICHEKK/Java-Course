package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that pushes the current working path to the
 * stack, and sets the working directory to the given
 * path.
 *
 * @author Filip Nemec
 */
public class PushdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String argument = ArgumentParser.extractArgs(arguments, 1, 1)[0];
		
		Path newPath = Paths.get(argument).toAbsolutePath().normalize();
		
		if(!Files.isDirectory(newPath)) {
			env.writeln("Given path is not a directory. Path was not pushed.");
			return ShellStatus.CONTINUE;
		}
		
		if(env.getSharedData("cdstack") == null) {
			env.setSharedData("cdstack", new Stack<Path>());
		}
		
		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData("cdstack");
		
		pathStack.push(env.getCurrentDirectory());
		
		env.writeln("Working directory '" + env.getCurrentDirectory() + "' pushed to the stack.");
		env.setCurrentDirectory(newPath);
		env.writeln("New working directory set to '" + newPath + "'");
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Pushes the current working path to the stack, and sets the working directory to the given path.");
		desc.add("");
		desc.add("Usage: 'pushd <path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "pushd";
	}
}