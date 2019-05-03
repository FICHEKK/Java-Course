package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that lists all of the paths currently
 * stored on the stack.
 *
 * @author Filip Nemec
 */
public class ListdShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		@SuppressWarnings("unchecked")
		Stack<Path> pathStack = (Stack<Path>) env.getSharedData("cdstack");
		
		if(pathStack == null || pathStack.isEmpty()) {
			env.writeln("No directories stored.");
			
		} else {
			List<String> paths = pathStack.stream()
										 .map(path -> path.toString())
										 .collect(Collectors.toList());
			Collections.reverse(paths);
			paths.forEach(env::writeln);
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Lists all of the paths currently stored on the stack.");
		desc.add("");
		desc.add("Usage: 'listd <path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "listd";
	}
}
