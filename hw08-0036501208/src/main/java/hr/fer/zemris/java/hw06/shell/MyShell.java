package hr.fer.zemris.java.hw06.shell;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.DropdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexDumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ListdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MassRenameShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PopdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PushdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.PwdShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * A simple shell implementation that supports the
 * commands for operating with files. 
 *
 * @author Filip Nemec
 */
public class MyShell {
	
	/**
	 * Program starts from here.
	 *
	 * @param args none are taken into consideration
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		EnvironmentImpl env = new EnvironmentImpl(scanner);
		
		env.writeln("Welcome to MyShell v1.0");
		
		ShellStatus status = ShellStatus.CONTINUE;
		do {
			try {
				env.write(env.PROMPT_SYMBOL + " ");
				String line = readLineOrLines(env);
				
				String[] splitted = splitCommandFromArguments(line);
				String commandName = splitted[0];
				String arguments = splitted[1];
				
				ShellCommand command = env.commands().get(commandName);
				
				if(command != null) {
					status = command.executeCommand(env, arguments);
				} else {
					env.writeln("'" + commandName + "' is not a valid command.");
					env.writeln("Type 'help' to see a list of all the valid commands.");
				}
			} catch(ShellIOException e) {
				status = ShellStatus.TERMINATE;
			} catch(Exception e) {
				status = ShellStatus.TERMINATE;
			}
		} while(status != ShellStatus.TERMINATE);
		
		scanner.close();
	}
	
	/**
	 * Reads a single or multiple lines from the shell. Multiple lines
	 * will be read only if the multi-line character is present.
	 *
	 * @param shell the shell that is being read
	 * @return a single {@code String} that is the shell command
	 */
	private static String readLineOrLines(EnvironmentImpl shell) {
		var sb = new StringBuilder();
		
		while(true) {
			String line = shell.readLine();
			
			if(line == null) break;
			
			line = line.trim();
			if(line.endsWith("" + shell.MORE_LINES_SYMBOL)) {
				sb.append(line.substring(0, line.length() - 1) + " ");
				shell.write(shell.MULTI_LINE_SYMBOL + " ");
			} else {
				sb.append(line);
				break;
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Splits the full command into command name and arguments.
	 *
	 * @param line command to be splitted
	 * @return {@code String} array with 2 elements: command name and arguments
	 */
	private static String[] splitCommandFromArguments(String line) {
		String trimmed = line.trim();
		
		var sb = new StringBuilder();
		
		int i = 0;
		for(i = 0; i < trimmed.length(); i++) {
			char current = trimmed.charAt(i);
			if(Character.isWhitespace(current)) break;
			sb.append(current);
		}
		
		String[] parts = new String[2];
		parts[0] = sb.toString(); 								  // the command
		parts[1] = trimmed.substring(i, trimmed.length()).trim(); // the arguments
		
		return parts;
	}
	
	/**
	 * Simple shell implementation that offers the user basic
	 * file manipulation commands.
	 *
	 * @author Filip Nemec
	 */
	private static class EnvironmentImpl implements Environment {
		
		/** Symbol that prompts the user to enter a command. */
		private char PROMPT_SYMBOL = '>';
		
		/** Symbol that signals that the given command will be continued in the next line. */
		private char MORE_LINES_SYMBOL = '\\';
		
		/** Symbol that gets printed at the start of each multi-line. */
		private char MULTI_LINE_SYMBOL = '|';
		
		/** Maps the {@code String} values to its corresponding commands. */
		private static final SortedMap<String, ShellCommand> commands;
		
		/** Scanner used for reading lines from the shell. */
		private Scanner scanner = new Scanner(System.in);
		
		/** The current directory path. */
		private Path currentDirectory = Paths.get(".").toAbsolutePath().normalize();
		
		/** Map used for storing and retrieving shared data. */
		private Map<String, Object> sharedData = new HashMap<>();
		
		static {
			SortedMap<String, ShellCommand> cmds = new TreeMap<>();
			
			cmds.put("symbol",      new SymbolShellCommand());
			cmds.put("exit", 	 	new ExitShellCommand());
			cmds.put("help", 		new HelpShellCommand());
			cmds.put("tree", 	  	new TreeShellCommand());
			cmds.put("mkdir", 	  	new MkdirShellCommand());
			cmds.put("charsets",  	new CharsetsShellCommand());
			cmds.put("ls", 		  	new LsShellCommand());
			cmds.put("cat",	 	  	new CatShellCommand());
			cmds.put("copy",	  	new CopyShellCommand());
			cmds.put("hexdump",   	new HexDumpShellCommand());
			cmds.put("pwd",		  	new PwdShellCommand());
			cmds.put("cd",		  	new CdShellCommand());
			cmds.put("pushd",	  	new PushdShellCommand());
			cmds.put("listd",	  	new ListdShellCommand());
			cmds.put("dropd",	  	new DropdShellCommand());
			cmds.put("popd",	 	new PopdShellCommand());
			cmds.put("massrename", 	new MassRenameShellCommand());
			
			commands = Collections.unmodifiableSortedMap(cmds);
		}
		
		/**
		 * Constructs a new shell and initializes the
		 * user prompt.
		 * 
		 * @param scanner the scanner used for reading from the shell
		 */
		public EnvironmentImpl(Scanner scanner) {
			this.scanner = Objects.requireNonNull(scanner, "Given scanner must not be null.");
		}
		
		//------------------------------------------------------------------------
		//						SHELL WRITING AND READING
		//------------------------------------------------------------------------

		@Override
		public String readLine() throws ShellIOException {
			try {
				return scanner.nextLine();
				
			} catch(NoSuchElementException e) {
				throw new ShellIOException("New line was not found.");
				
			} catch(IllegalStateException e) {
				throw new ShellIOException("Stream was closed.");
				
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			if(text == null)
				throw new ShellIOException("Please provide non-null text to write to the console.");
			System.out.print(text);
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			if(text == null)
				throw new ShellIOException("Please provide non-null text to write to the console.");
			System.out.println(text);
		}
		
		//------------------------------------------------------------------------
		//							GETTERS AND SETTERS
		//------------------------------------------------------------------------
		
		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getPromptSymbol() {
			return PROMPT_SYMBOL;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			PROMPT_SYMBOL = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return MORE_LINES_SYMBOL;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			MORE_LINES_SYMBOL = symbol;
		}
		
		@Override
		public Character getMultilineSymbol() {
			return MULTI_LINE_SYMBOL;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			MULTI_LINE_SYMBOL = symbol;
		}

		@Override
		public Path getCurrentDirectory() {
			return currentDirectory;
		}

		@Override
		public void setCurrentDirectory(Path path) {
			if(!Files.isDirectory(path))
				throw new ShellIOException("Given path '" + path + "' is not a valid directory.");
			
			this.currentDirectory = path.toAbsolutePath().normalize();
		}

		@Override
		public Object getSharedData(String key) {
			return sharedData.get(key);
		}

		@Override
		public void setSharedData(String key, Object value) {
			Objects.requireNonNull(key, "Shared data key cannot be null.");
			sharedData.put(key, value);
		}
	}
}
