package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Objects;

/**
 * Utility class that offers methods for validating
 * variable, method, tag and operator strings.
 */
class SmartScriptParserUtil {
	
	/**
	 * Checks if the variable name is valid, determined
	 * by certain rules: first char must be a letter and
	 * the rest need to be letters, digits or underscores.
	 * 
	 * @param variable variable string to be checked
	 * @return {@code true} if valid, {@code false} otherwise
	 */
	public static boolean isVariableNameValid(String variable) {
		Objects.requireNonNull(variable);
		char[] chars = variable.toCharArray();
		
		// First char must be a letter.
		if(!Character.isLetter(chars[0]))
			return false;
		
		return isTheRestValid(chars, 1);
	}
	
	/**
	 * Checks if the function name is valid, determined
	 * by certain rules: first char must be a '@', the second
	 * char must be a letter and the rest need to be letters,
	 * digits or underscores.
	 * 
	 * @param function function string to be checked
	 * @return {@code true} if valid, {@code false} otherwise
	 */
	public static boolean isFunctionNameValid(String function) {
		Objects.requireNonNull(function);
		char[] chars = function.toCharArray();
		
		// First char must be '@' and second must be a letter.
		if(chars[0] != '@' || !Character.isLetter(chars[1]))
			return false;
		
		return isTheRestValid(chars, 2);
	}
	
	/**
	 * Checks if the rest of the char array consists of letter, digit
	 * or underscore characters only.
	 * 
	 * @param text text to be validated
	 * @param startingIndex index from where we start checking
	 * @return {@code true} if it is valid, {@code false} otherwise
	 */
	private static boolean isTheRestValid(char[] chars, int startingIndex) {
		// All other must be letters, underscores or digits.
		for(int i = startingIndex; i < chars.length; i++) {
			char c = chars[i];
			if(!Character.isLetter(c) && !Character.isDigit(c) && c != '_')
				return false;
		}
		
		// All tests passed, the rest of the char array is valid.
		return true;
	}
	
	/**
	 * Checks if the given operator string is valid.
	 * Valid operators are {@code +, -, *, / and ^}.
	 * 
	 * @param operator operator to be validated
	 * @return {@code true} if it is valid, {@code false} otherwise
	 */
	public static boolean isOperatorValid(String operator) {
		boolean valid = operator.equals("+") ||
						operator.equals("-") ||
						operator.equals("*") ||
						operator.equals("/") ||
						operator.equals("^");
		return valid;
	}
	
	/**
	 * Checks if the given tag name is valid. It is valid
	 * if the tag name is '=' or a valid variable name.
	 * 
	 * @param tag tag to be checked
	 * @return {@code true} if it is valid, {@code false} otherwise
	 */
	public static boolean isTagNameValid(String tag) {
		return tag.equals("=") || isVariableNameValid(tag);
	}
}
