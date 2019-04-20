package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Outputs the file, defined by it's path, to the
 * given shell.
 *
 * @author Filip Nemec
 */
public class CatShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Outputs the given file to the shell.");
		desc.add("");
		desc.add("Usage: 'cat <file path> ?charset?'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment should not be null.");
		Objects.requireNonNull(arguments, "Given arguments should not be null.");
		
		try {
			String[] args = ArgumentParser.getArgs(arguments);
		
			if(args.length < 1 || args.length > 2) {
				env.writeln("Invalid argument(s) '" + arguments + "': <file path> ?charset? expected.");
				return ShellStatus.CONTINUE;
			}
		
			try {
				Charset charset = args.length == 1 ? Charset.defaultCharset() : Charset.forName(args[1]);
				
				BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]), charset);
				
				try {
					String line = null;
					while((line = reader.readLine()) != null) {
						env.writeln(line);
					}
				} finally {
					reader.close();
				}
				
			} catch(UnsupportedCharsetException | IllegalCharsetNameException e) {
				env.writeln("'" + args[1] + "' is not a supported charset.");
				
			} catch (NoSuchFileException e) {
				env.writeln("Provided file was not found.");
				
			} catch (IOException e) {
				env.writeln("Error loading file.");
				
			}
			
		} catch(IllegalArgumentException e) {
			env.writeln(e.getMessage());
			
		}

		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "cat";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
