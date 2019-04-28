package hr.fer.zemris.java.hw06.shell.commands;

import java.io.File;
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
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that lists all of the files in the
 * given directory with detailed information.
 *
 * @author Filip Nemec
 */
public class LsShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Lists all of the files in the given directory with detailed information.");
		desc.add("");
		desc.add("Usage: 'ls <dir path>'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment should not be null.");
		Objects.requireNonNull(arguments, "Given arguments should not be null.");
		
		if(arguments.isEmpty()) {
			env.writeln("Given directory path cannot be empty.");
			return ShellStatus.CONTINUE;
		}
		
		File root = new File(arguments);
		
		if(!root.isDirectory()) {
			env.writeln("Given path '" + root.getName() + "' is not a directory.");
			return ShellStatus.CONTINUE;
		}
		
		File[] files = root.listFiles();
		
		if(files == null) {
			env.writeln("Error loading the given directory.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			for(File file : files) {
				String info     = getFileInfo(file.toPath());
				String size     = getFileSize(file.toPath());
				String creation = getFileCreationTime(file.toPath());
				String name 	= file.getName();
				
				env.writeln(info + " " + size + " " + creation + " " + name);
			}
		} catch(IOException e) {
			env.writeln("Error during the retrieval of file information.");
		}
		
		return ShellStatus.CONTINUE;
	}

	private String getFileCreationTime(Path path) throws IOException {
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
	public String getCommandName() {
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
