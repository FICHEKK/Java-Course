package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * A command which takes a file, and dumps its data
 * to the shell in hexadecimal format.
 *
 * @author Filip Nemec
 */
public class HexDumpShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	private static final List<String> DESCRIPTION;
	
	/** Defines how many bytes will be per row. */
	private static final int BYTES_PER_ROW = 16;
	
	/** Defines how big the reading block is. */
	private static final int BLOCK_SIZE = 1024;
	
	/** Defines if the hexadecimal digits 'a', 'b', 'c', 'd', 'e' and 'f' should be uppercase or not. */
	private static final boolean UPPERCASE = true;
	
	/**
	 * The number of characters in a row. It is always double the byte per row count
	 * since one byte is represented by 2 chars, e.g. byte 10 is '0a'.
	 */
	private static final int CHARS_PER_ROW = BYTES_PER_ROW * 2;
	
	/** Number of partitions that hex dump is divided into. */
	private static final int PARTITION_COUNT = 2;
	
	static {
		var desc = new LinkedList<String>();
		
		desc.add("- Displays the entire file's contents in hexadecimal format to the shell.");
		desc.add("- As an argument it expects a path that needs to be a file, not a directory.");
		desc.add("");
		desc.add("Usage: 'hexdump <file path>'");
		
		DESCRIPTION = Collections.unmodifiableList(desc);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Given environment should not be null.");
		Objects.requireNonNull(arguments, "Given arguments should not be null.");
		
		String[] args = null;
		try {
			args = ArgumentParser.getArgs(arguments);
		} catch(IllegalArgumentException e) {
			env.writeln(e.getMessage());
			return ShellStatus.CONTINUE;
		}
		
		if(args.length != 1) {
			env.writeln("Expected a single argument: file path. Was " + args.length + " argument(s).");
			return ShellStatus.CONTINUE;
		}
		
		Path path = Paths.get(args[0]);
		
		if(Files.isDirectory(path)) {
			env.writeln("Given path '" + path + "' is not a file, but a directory.");
			return ShellStatus.CONTINUE;
		}
		
		try(BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
			byte[] bytes = new byte[BLOCK_SIZE];
			
			int count;
			while((count = bis.read(bytes)) > 0) {
				outputHexData(env, bytes, count);
			}
		} catch(IOException e) {
			env.writeln("Error during the retrieval of file information.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	private static void outputHexData(Environment env, byte[] bytes, int count) {
		restrictBytes(bytes);
		String hex = Utility.byteToHex(bytes, count);
		
		if(UPPERCASE) {
			hex = hex.toUpperCase();
		}
		
		var sb = new StringBuilder();
		
		// Size of a partition.
		int pSize = CHARS_PER_ROW / PARTITION_COUNT;
		
		// Last char index.
		int lastChar = count * 2 - 1;
		
		// Number of full rows.
		int rowCount  = count / BYTES_PER_ROW;
		
		// If there is a remainder of bytes, there
		// will be an extra row.
		if(count % BYTES_PER_ROW != 0) {
			rowCount++;
		}
		
		for(int row = 0; row < rowCount; row++) {
			int rowStartIndex = row * BYTES_PER_ROW;
			sb.append(intToHex(rowStartIndex) + ": ");
			
			// p stands for partition
			for(int p = 0; p < PARTITION_COUNT; p++) {
				int pStart = (row * CHARS_PER_ROW) + (p * pSize);
				for(int i = pStart; i < pStart + pSize; i += 2) {
					if(i <= lastChar) {
						sb.append(hex.charAt(i) + "" + hex.charAt(i+1) + " ");
					} else {
						sb.append("   ");
					}
				}
				
				sb.append("| ");
			}
			
			for(int i = rowStartIndex; i < rowStartIndex + BYTES_PER_ROW; i++) {
				if(i < count) {
					sb.append((char)bytes[i]);
				}
			}
			
			sb.append("\n");
		}
		
		env.write(sb.toString());
	}
	
	private static void restrictBytes(byte[] bytes) {
		for(int i = 0, len = bytes.length; i < len; i++) {
			if(bytes[i] < 32 || bytes[i] > 127) {
				bytes[i] = '.';
			}
		}
	}
	
	private static String intToHex(int value) {
		// ff_ff_ff_ff -> int
		// _0__1__2__3 -> indexes of bytes below
		// We extract those 4 bytes here.
		byte[] bytes = new byte[4];
		
		bytes[3] = (byte) value;
		bytes[2] = (byte) (value >>> 8);
		bytes[1] = (byte) (value >>> 16);
		bytes[0] = (byte) (value >>> 24);
		
		return Utility.byteToHex(bytes);
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		return DESCRIPTION;
	}

}
