package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that displays and gives a detailed
 * explanation of all the other commands
 * supported on the given shell.
 *
 * @author Filip Nemec
 */
public class HelpShellCommand implements ShellCommand {
	
	/** A border used for formatting command titles. */
	private static final String BORDER = "|";
	
	/** {@code String} of empty spaces used for formatting command titles. */
	private static final String EMPTY_SPACES = " ".repeat(12);
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- If no arguments are provided, simply lists all of the supported commands.");
		desc.add("- Otherwise, you can get details of a certain command by providing command name.");
		desc.add("");
		desc.add("Usage: 'help' ?command?");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = ArgumentParser.getArgs(arguments);
		
		if(args.length == 0) {
			printAllCommands(env);
			
		} else if(args.length == 1) {
			printCommandDescription(env, args[0]);
			
		} else {
			env.writeln("Expected 0 or 1 arguments. Was " + args.length + ".");
			
		}
		
		return ShellStatus.CONTINUE;
	}

	private static void printAllCommands(Environment env) {
		Map<String, ShellCommand> commands = env.commands();
		
		env.writeln("Available commands: ");
		
		int index = 1;
		for(Map.Entry<String, ShellCommand> entry : commands.entrySet()) {
			env.writeln("    " + index + ") " + entry.getValue().getCommandName());
			index++;
		}
		
		env.writeln("");
		env.writeln("If you want more details about a certain command, type 'help <command>'");
		env.writeln("");
	}
	
	private static void printCommandDescription(Environment env, String commandName) {
		Map<String, ShellCommand> commands = env.commands();
		
		ShellCommand command = commands.get(commandName);
		
		if(command != null) {
			String border = "=".repeat((BORDER.length() + EMPTY_SPACES.length()) * 2 + commandName.length());
			env.writeln(border);
			env.writeln(BORDER + EMPTY_SPACES + commandName + EMPTY_SPACES + BORDER);
			env.writeln(border);
			
			for(String descriptionLine : command.getCommandDescription()) {
				env.writeln(descriptionLine);
			}
			
			env.writeln("");
		} else {
			env.writeln("Given command '" + commandName + "' is not supported.");
			return;
		}
		
		env.writeln("_____________________________________");
		env.writeln("Legend:");
		env.writeln("<some field> -> the required field");
		env.writeln("?some field? -> the optional field");
		env.writeln("_____________________________________");
		env.writeln("");
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
