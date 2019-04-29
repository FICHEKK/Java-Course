package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Stream;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that lists all of the files in the
 * given directory with detailed information.
 *
 * @author Filip Nemec
 */
public class LsShellCommand extends AbstractShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
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
		
		try(Stream<Path> pathStream = Files.list(root)) {
			pathStream.forEach(path -> {
				try {
					String info     = getFileInfo(path);
					String size     = getFileSize(path);
					String creation = getFileCreationTime(path);
					String name 	= path.getFileName().toString();
					
					env.writeln(info + " " + size + " " + creation + " " + name);
					
				} catch(IOException e) {
					env.writeln("Error during the retrieval of file information.");
					
				}
			});

		} catch(IOException e) {
			env.writeln("Error during stream creation.");
		}
		
		return ShellStatus.CONTINUE;
	}

	private static String getFileCreationTime(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		BasicFileAttributeView faView = Files.getFileAttributeView(
				path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
		);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		
		return sdf.format(new Date(fileTime.toMillis()));
	}

	private static String getFileInfo(Path file) {
		char[] info = new char[4];
		
		info[0] = Files.isDirectory(file)  ? 'd' : '-';
		info[1] = Files.isReadable(file)   ? 'r' : '-';
		info[2] = Files.isWritable(file)   ? 'w' : '-';
		info[3] = Files.isExecutable(file) ? 'x' : '-';
		
		return new String(info);
	}
	
	private static String getFileSize(Path file) throws IOException {
		return String.format("%10d", Files.size(file));
	}

	@Override
	protected void init() {
		var desc = new LinkedList<String>();
		
		desc.add("- Lists all of the files in the given directory with detailed information.");
		desc.add("");
		desc.add("Usage: 'ls <dir path>'");
		
		this.DESCRIPTION = Collections.unmodifiableList(desc);
		this.NAME = "ls";
	}
}
