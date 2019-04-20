package hr.fer.zemris.java.hw06.shell.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command used for displaying and changing 
 * shell's symbols.
 *
 * @author Filip Nemec
 */
public class SymbolShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
	
		desc.add("- Outputs or changes the shell symbols.");
		desc.add("- Possible symbols are:");
		desc.add("      a) PROMPT");
		desc.add("      b) MORELINES");
		desc.add("      c) MULTILINE");
		desc.add("");
		desc.add("- If you wish to output the current symbol for PROMPT:");
		desc.add("      'symbol PROMPT' => outputs the current PROMPT symbol");
		desc.add("");
		desc.add("- If you wish to change the current symbol for PROMPT:");
		desc.add("      'symbol PROMPT #' => changes the current PROMPT symbol to '#'");
		desc.add("");
		desc.add("Usage: 'symbol <possible symbol> ?new symbol?'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = arguments.trim().replaceAll(" +", " ").split(" ");
		
		if(args.length == 1) {
			outputRequestedSymbol(env, args[0]);
			
		} else if(args.length == 2) {
			changeRequestedSymbol(env, args[0], args[1]);
			
		} else {
			env.writeln("'" + arguments + "' is not of a valid format.");
		}
		
		return ShellStatus.CONTINUE;
	}

	private void changeRequestedSymbol(Environment env, String symbol, String newSymbol) {
		if(newSymbol.length() != 1) {
			env.writeln("Single character expected, was: '" + newSymbol + "'.");
			return;
		}
		
		Character sym = newSymbol.charAt(0);
		
		if(symbol.equals("PROMPT")) {
			env.writeln("Symbol for PROMPT changed from '" + env.getPromptSymbol() + "' to '" + sym + "'.");
			env.setPromptSymbol(sym);
			
		} else if(symbol.equals("MORELINES")) {
			env.writeln("Symbol for MORELINES changed from '" + env.getMorelinesSymbol() + "' to '" + sym + "'.");
			env.setMorelinesSymbol(sym);
			
		} else if(symbol.equals("MULTILINE")) {
			env.writeln("Symbol for MULTILINE changed from '" + env.getMultilineSymbol() + "' to '" + sym + "'.");
			env.setMultilineSymbol(sym);
			
		} else {
			env.writeln("Given symbol '" + symbol + "' is not a valid symbol.");
			
		}
	}

	private void outputRequestedSymbol(Environment env, String symbol) {
		if(symbol.equals("PROMPT")) {
			env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'.");
			
		} else if(symbol.equals("MORELINES")) {
			env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'.");
			
		} else if(symbol.equals("MULTILINE")) {
			env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'.");
			
		} else {
			env.writeln("Given symbol '" + symbol + "' is not a valid symbol.");
			
		}
	}

	@Override
	public String getCommandName() {
		return "symbol";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
