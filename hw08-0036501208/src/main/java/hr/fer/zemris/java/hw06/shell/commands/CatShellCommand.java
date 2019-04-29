package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Outputs the file, defined by it's path, to the
 * given shell.
 *
 * @author Filip Nemec
 */
public class CatShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String[] args = ArgumentParser.extractArgs(arguments, 1, 2);
			Charset charset = args.length == 1 ? Charset.defaultCharset() : Charset.forName(args[1]);
			
			Path filePath = env.getCurrentDirectory().resolve(args[0]);
			
			if(Files.isDirectory(filePath)) {
				env.writeln("Provided path is a directory. File path is required.");
				return ShellStatus.CONTINUE;
			}
			
			try(BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
				String line = null;
				while((line = reader.readLine()) != null) {
					env.writeln(line);
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
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Outputs the given file to the shell.");
		desc.add("");
		desc.add("Usage: 'cat <file path> ?charset?'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "cat";
	}
}
