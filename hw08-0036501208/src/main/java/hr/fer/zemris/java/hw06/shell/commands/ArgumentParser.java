package hr.fer.zemris.java.hw06.shell.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A simple utility class that splits the given command
 * arguments. It offers a feature of using quotes to
 * allow whitespace between arguments:
 * <p>
 *  abc def  -> 2 arguments: abc, def <br>
 * "abc def" -> 1 argument : abc def
 *
 * @author Filip Nemec
 */
class ArgumentParser {
	
	/**
	 * Splits the given argument {@code String} into an array of
	 * arguments. It allows quoting, where a single argument can
	 * be split by whitespace if it is inside quotes.
	 *
	 * @param arguments sequence to be split
	 * @return an array of arguments from the original sequence
	 * @throws IllegalArgumentException if quotes were not closed
	 */
	public static String[] getArgs(String arguments) {
		Objects.requireNonNull(arguments, "Cannot get arguments for a null reference.");
		if(arguments.isBlank()) return new String[0];
		
		char[] chars = arguments.toCharArray();
		
		List<String> args = new LinkedList<>();
		
		boolean inQuotes = false;
		boolean argStartFound = false;
		int argStart = 0;
		
		for(int i = 0; i < chars.length; i++) {
			if(inQuotes) {
				if(chars[i] == '"') {
					inQuotes = false;
					
					if(i + 1 < chars.length && !Character.isWhitespace(chars[i + 1])) {
						String msg = "After closing quotes, white-space or EOF expected. Was: '" + chars[i+1] + "'.";
						throw new IllegalArgumentException(msg);
					}
					
					String quotted = arguments.substring(argStart, i).trim();
					if(!quotted.isBlank())
						args.add(quotted);
					
					argStart = i + 1;
					argStartFound = false;
				}
			} else {
				if(!argStartFound) {
					if(Character.isWhitespace(chars[i])) continue;
					
					if(i == (chars.length-1)) {
						args.add(arguments.substring(argStart, i + 1));
						break;
					}
					
					argStartFound = true;
					
					if(chars[i] == '"') {
						inQuotes = true;
						argStart = i + 1;
						continue;
					}
					
					argStart = i;
				} else {
					if(i == (chars.length-1)) {
						args.add(arguments.substring(argStart));
						
					} else if(Character.isWhitespace(chars[i])) {
						args.add(arguments.substring(argStart, i));
						argStartFound = false;
						
					} else if(chars[i] == '"') {
						args.add(arguments.substring(argStart, i));
						inQuotes = true;
						argStart = i + 1;
					}
				}
			}
		}
		
		if(inQuotes)
			throw new IllegalArgumentException("Quotes were not closed.");
		
		return args.toArray(new String[0]);
	}
	
	/**
	 * Extracts the arguments, but also requires the argument count
	 * to be satisfied.
	 *
	 * @param sequence the sequence to extract arguments from
	 * @param min the minimum number of arguments required
	 * @param max the maximum number of arguments required
	 * @return the arguments
	 * @throws IllegalArgumentException if error during parsing occurs,
	 * 									or if the argument count is not met
	 */
	public static String[] extractArgs(String sequence, int min, int max) {
		String[] arguments = getArgs(sequence);
		
		if(arguments.length < min || arguments.length > max) {
			String msg = "Expected argument count range [" + min + ", " + max + "]. Was: " + arguments.length;
			throw new IllegalArgumentException(msg);
		}
		
		return arguments;
	}
}
