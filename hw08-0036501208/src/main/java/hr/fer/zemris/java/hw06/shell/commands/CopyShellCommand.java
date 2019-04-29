package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that copies the given source file to
 * the given destination path.
 *
 * @author Filip Nemec
 */
public class CopyShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		try {
			String[] args = ArgumentParser.extractArgs(arguments, 2, 2);
			
			if(args[0].equals(args[1])) {
				env.writeln("Source and destination should not point to the same path.");
				return ShellStatus.CONTINUE;
			}
			
			Path source 	 = env.getCurrentDirectory().resolve(args[0]);
			Path destination = env.getCurrentDirectory().resolve(args[1]);
			
			if(Files.isDirectory(destination)) {
				String inDirectoryPath = destination.toAbsolutePath() + File.separator + source.getFileName();
				destination = Paths.get(inDirectoryPath);
			}
			
			if(Files.exists(destination)) {
				env.writeln("File '" + destination.getFileName() + "' already exists.");
				env.writeln("Would you like to overwrite it? Type 'y' for yes, or 'n' for no.");
				
				while(true) {
					String answer = env.readLine();
					
					if(answer.equals("y")) {
						env.writeln("Overwriting file...");
						break;
					}
					
					if(answer.equals("n")) {
						env.writeln("File was not overwritten.");
						return ShellStatus.CONTINUE;
					}
					
					env.writeln("Invalid answer. Please write 'y' for yes, or 'n' for no.");
				}
			}
			
			int bytesCopied = copy(source, destination);
			env.writeln("File '" + source.toFile().getName() + "' was copied to:");
			env.writeln("'" + destination.toFile().getAbsolutePath() + "'.");
			env.writeln(bytesCopied + " bytes were copied.");
			
		} catch(IOException ex) {
			env.writeln("Error occured during the copying of the file.");
			
		} catch(InvalidPathException ex) {
			env.writeln("Given path in invalid.");
			
		} catch(IllegalArgumentException ex) {
			env.writeln(ex.getMessage());
			
		}

		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Copies the given source file to the given destination.
	 *
	 * @param source of the file that is being copied
	 * @param destination where the file should be copied to
	 * @return the number of bytes that were copied during this transfer
	 */
	private static int copy(Path source, Path destination) throws IOException {
		int bytesCopied = 0;
		
		try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(source));
			BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(destination))) {
			
			byte[] buffer = new byte[1024];
			int r;
			while((r = bis.read(buffer)) > 0) {
				bytesCopied += r;
				bos.write(buffer, 0, r);
			}
		}
		
		return bytesCopied;
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Copies the given source file to the given destination path.");
		desc.add("- If the provided destination file already exists, user will be asked if the file should be overriden.");
		desc.add("- If the provided destination is a directory, original file will be copied to that directory.");
		desc.add("");
		desc.add("Usage: 'copy <source file path> <destination file path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "copy";
	}
}
