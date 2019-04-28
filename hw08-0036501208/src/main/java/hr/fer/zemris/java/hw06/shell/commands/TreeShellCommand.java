package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A command that writes the entire directory tree
 * structure to the given environment.
 *
 * @author Filip Nemec
 */
public class TreeShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Prints the entire directory tree structure to the shell.");
		desc.add("- It expects a single argument: directory root.");
		desc.add("");
		desc.add("Usage: 'tree <root path>'");
		
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
		
		Path root = Paths.get(args[0]);
		
		try {
			Files.walkFileTree(root, new TreePrinter(env));
		} catch (IOException e) {
			env.writeln("Error during the tree walk.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "tree";
	}
	
	private static class TreePrinter implements FileVisitor<Path> {
		
		private Environment env;
		private int depth = 0;
		
		public TreePrinter(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Objects.requireNonNull(dir);
			
			env.writeln(" ".repeat(depth * 2) + dir.getFileName());
			depth++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Objects.requireNonNull(file);
			
			env.writeln(" ".repeat(depth * 2) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			Objects.requireNonNull(dir);
			
			depth--;
			return FileVisitResult.CONTINUE;
		}
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
