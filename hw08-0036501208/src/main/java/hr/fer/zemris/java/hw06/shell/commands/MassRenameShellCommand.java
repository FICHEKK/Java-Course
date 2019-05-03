package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;
import hr.fer.zemris.java.hw06.shell.commands.parser.NameBuilder;
import hr.fer.zemris.java.hw06.shell.commands.parser.NameBuilderParser;
import hr.fer.zemris.java.hw06.shell.commands.parser.NameBuilderParserException;

/**
 * Command that is used for renaming or transferring of a
 * whole directory.
 *
 * @author Filip Nemec
 */
public class MassRenameShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String[] args = ArgumentParser.extractArgs(arguments, 4, 5);
			
			Path dir1 	   = Paths.get(args[0]).toAbsolutePath().normalize();
			Path dir2  	   = Paths.get(args[1]).toAbsolutePath().normalize();
			String cmd     = args[2];
			String pattern = args[3];
			
			switch (cmd) {
				case "filter":
					showFilterResults(env, dir1, pattern);
					break;
					
				case "groups":
					showGroupingResults(env, dir1, pattern);
					break;
					
				case "show":
					if(args.length != 5)
						throw new IllegalArgumentException("'show' command requires an expression.");
					
					showRenameConversion(env, dir1, pattern, args[4]);
					break;
				
				case "execute":
					if(args.length != 5)
						throw new IllegalArgumentException("'execute' command requires an expression.");
					
					executeRenameConversion(dir1, dir2, pattern, args[4]);
					break;
	
				default:
					env.writeln("Command '" + cmd + "' is not supported.");
					break;
			}
			
		} catch(IllegalArgumentException | IOException | NameBuilderParserException e) {
			env.writeln(e.getMessage());
			
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Shows the results from the grouping command
	 *
	 * @param env the environment for writing the output
	 * @param dir1 the directory containing the files that will be filtered
	 * @param pattern the filter pattern
	 * @throws IOException if an error in the file system occurs
	 */
	private void showGroupingResults(Environment env, Path dir1, String pattern) throws IOException {
		filter(dir1, pattern).forEach(filter -> {
			var sb = new StringBuilder();
			sb.append(filter.toString());
			
			for(int i = 0; i < filter.numberOfGroups(); i++) {
				sb.append(" " + i + ": ");
				sb.append(filter.group(i));
			}
			
			env.writeln(sb.toString());
		});
	}
	
	/**
	 * Writes all of the file names that passed the given filter
	 * for the given directory.
	 *
	 * @param env the environment for writing the output
	 * @param directory the directory containing the files that will be filtered
	 * @param pattern the filter pattern
	 * @throws IOException if an error in the file system occurs
	 */
	private void showFilterResults(Environment env, Path directory, String pattern) throws IOException {
		filter(directory, pattern).forEach(filter -> env.writeln(filter.toString()));
	}
	
	/**
	 * Gives a preview of which files and how will those file names be changed
	 * for the given expression.
	 *
	 * @param env the environment for writing the output
	 * @param dir the directory containing the files that will be filtered
	 * @param pattern the filter pattern
	 * @param expr the output format expression
	 * @throws IOException if an error in the file system occurs
	 */ 
	private void showRenameConversion(Environment env, Path dir, String pattern, String expr) throws IOException {
		List<FilterResult> files = filter(dir, pattern);
		NameBuilderParser parser = new NameBuilderParser(expr);
		NameBuilder builder = parser.getNameBuilder();
		
		for(FilterResult file : files) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			env.writeln(file.toString() + " => " + sb.toString());
		}
	}
	
	/**
	 * Executes the renaming.
	 *
	 * @param dirSrc the source directory
	 * @param dirDest the destination directory
	 * @param pattern the pattern
	 * @param expr the expression
	 * @throws IOException if an error in the file system occurs
	 */
	private void executeRenameConversion(Path dirSrc, Path dirDest, String pattern, String expr) throws IOException {
		List<FilterResult> files = filter(dirSrc, pattern);
		NameBuilderParser parser = new NameBuilderParser(expr);
		NameBuilder builder = parser.getNameBuilder();
		
		for(FilterResult file : files) {
			StringBuilder sb = new StringBuilder();
			builder.execute(file, sb);
			
			Path from = dirSrc.resolve(file.toString());
			Path to = dirDest.resolve(sb.toString());
			
			Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	/**
	 * Filters each file name in the given directory by the
	 * given pattern and returns the list of all file names
	 * that passed the filter.
	 *
	 * @param dir the directory that holds the files that will be filtered
	 * @param pattern the filter pattern
	 * @return list of all the file names that passed the filter
	 * @throws IOException if an error in the file system occurs
	 */
	private static List<FilterResult> filter(Path dir, String pattern) throws IOException {
		Pattern mask = Pattern.compile(pattern, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
		
		return Files.walk(dir).filter(path -> !Files.isDirectory(path))
							  .map(path -> path.getFileName().toString())
							  .filter(fileName -> mask.matcher(fileName).matches())
							  .map(fileName -> new FilterResult(fileName, pattern))
							  .collect(Collectors.toList());
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Renames all of the files from the given directory to another directory.");
		desc.add("");
		desc.add("VALID CMDs:");
		desc.add("- filter <mask>");
		desc.add("- groups <mask>");
		desc.add("- show <mask> <expression>");
		desc.add("");
		desc.add("Usage: 'massrename <dir1> <dir2> <cmd> <mask> ?other?'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "massrename";
	}
}