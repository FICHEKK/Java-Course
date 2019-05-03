package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Drops the path that resides on top of <i>cdstack</i>.
 * It does not change the current working directory.
 *
 * @author Filip Nemec
 */
public class DropdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData("cdstack");
		
		if(pathStack == null || pathStack.isEmpty()) {
			env.writeln("Stack is empty. Drop was not performed.");
			
		} else {
			Path dropped = pathStack.pop();
			env.writeln("Dropped path '" + dropped + "'");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Pops the path from the stack, but does not change the current working directory.");
		desc.add("");
		desc.add("Usage: 'dropd'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "dropd";
	}
}
