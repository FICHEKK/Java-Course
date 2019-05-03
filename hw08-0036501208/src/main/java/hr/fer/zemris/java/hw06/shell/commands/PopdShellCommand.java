package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Pops the path from the stack, and sets the popped path
 * as a current directory.
 *
 * @author Filip Nemec
 */
public class PopdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData("cdstack");
		
		if(pathStack == null || pathStack.isEmpty()) {
			env.writeln("Stack is empty. Pop was not performed.");
			
		} else {
			Path popped = pathStack.pop();
			
			if(!Files.exists(popped)) {
				env.writeln("Popped path no longer exists. New directory was not set.");
				return ShellStatus.CONTINUE;
				
			} else {
				env.setCurrentDirectory(popped);
				env.writeln("Set current working directory to '" + popped + "'");
			}
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Pops the path from the stack, and sets the popped path as a current directory.");
		desc.add("- If the popped path does not exist, it just pops from the top of stack.");
		desc.add("");
		desc.add("Usage: 'popd'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "popd";
	}
}
