package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that displays all of the supported
 * character sets, which is platform dependent.
 *
 * @author Filip Nemec
 */
public class CharsetsShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Lists the names of all the supported charsets.");
		desc.add("");
		desc.add("Usage: 'charsets'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment should not be null.");
		Objects.requireNonNull(arguments, "Given arguments should not be null.");
		
		if(!arguments.isBlank()) {
			env.writeln("Excess argument(s): '" + arguments + "' is not needed.");
			return ShellStatus.CONTINUE;
		}
		
		Map<String, Charset> charsetMap = Charset.availableCharsets();
		
		int charsetNumber = 1;
		for(Map.Entry<String, Charset> entry : charsetMap.entrySet()) {
			env.writeln(charsetNumber + ") " + entry.getKey());
			charsetNumber++;
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "charsets";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
