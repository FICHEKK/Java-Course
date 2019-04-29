package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A command that writes the entire directory tree
 * structure to the given environment.
 *
 * @author Filip Nemec
 */
public class TreeShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String argument = ArgumentParser.extractArgs(arguments, 1, 1)[0];
			
			Path root = null;
			if(argument.equals(".")) {
				root = env.getCurrentDirectory();
				
			} else {
				root = env.getCurrentDirectory().resolve(argument);
				
				if(!Files.isDirectory(root)) {
					env.writeln("Given path '" + root.getFileName() + "' is not a directory.");
					return ShellStatus.CONTINUE;
				}
			}
			
			Files.walkFileTree(root, new TreePrinter(env));
			
		} catch (IOException e) {
			env.writeln("Error during the tree walk.");
			
		} catch (IllegalArgumentException e) {
			env.writeln(e.getMessage());
			
		}
		
		return ShellStatus.CONTINUE;
	}
	
	private static class TreePrinter implements FileVisitor<Path> {
		
		private Environment env;
		private int depth = 0;
		
		public TreePrinter(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(depth * 2) + dir.getFileName());
			depth++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			env.writeln(" ".repeat(depth * 2) + file.getFileName());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			depth--;
			return FileVisitResult.CONTINUE;
		}
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Prints the entire directory tree structure to the shell.");
		desc.add("- It expects a single argument: directory root.");
		desc.add("");
		desc.add("Usage: 'tree <root path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "tree";
	}
}
