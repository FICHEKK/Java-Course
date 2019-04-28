package hr.fer.zemris.java.hw06.shell.commands;

/**
 * A simple utility class that offers basic conversion
 * between {@code String} values and {@code byte} arrays.
 *
 * @author Filip Nemec
 */
public class Utility {
	
	/** A {@code String} containing all the symbols which are the valid hexadecimal digits. */
	private static final String validHexSymbols = "0123456789aAbBcCdDeEfF";
	
	/** The constant that represents the hexadecimal base. */
	private static final int HEX = 16;
	
	/** Maps the {@code int} digit to its hexadecimal representation. */
	private static final char[] intToHex = "0123456789abcdef".toCharArray();
	
	/**
	 * Converts the given {@code String} to an array of {@code byte}
	 * values, if the conversion is possible. Given input should be
	 * in a format of hexadecimal digits, for example: "0ab4d5".
	 * <p>
	 * Length of the input should be an even number, as each {@code byte}
	 * value is 8 bits long and is represented by 2 hexadecimal digits.
	 * <p>
	 * Also, letters in hexadecimal are case-insensitive, meaning, letter
	 * 'a' is interpreted the same as the upper case letter 'A'.
	 *
	 * @param keyText the hexadecimal sequence of {@code byte} values
	 * @return an array of {@code byte} values
	 * @throws IllegalArgumentException if the input length is an odd number
	 * 									or if any of the given input characters
	 * 									are not a valid hexadecimal digit
	 */
	public static byte[] hexToByte(String keyText) {
		if(keyText.isEmpty())
			return new byte[0];
		
		int keyLength = keyText.length();
		if(keyLength % 2 == 1)
			throw new IllegalArgumentException("Key length should be an even number. Was: " + keyLength);
		
		byte[] bytes = new byte[keyLength / 2];
		
		for(int i = 0; i < keyLength; i += 2) {
			char left = requireValidHexDigit(keyText.charAt(i));
			char right = requireValidHexDigit(keyText.charAt(i + 1));

			bytes[i / 2] = (byte) ((Character.digit(left, HEX) << 4) + Character.digit(right, HEX));
		}
			
		return bytes;
	}
	
	/**
	 * Converts the given {@code byte} array to its {@code String}
	 * representation. For example, an array {0, 1, 127} will be
	 * converted to "00017f", as "00" is a hexadecimal representation
	 * of a {@code byte} value 0, "01" of a value 1 and "7f" of a value 127.
	 *
	 * @param byteArray array of {@code byte} values to be converted to {@code String}
	 * @return {@code byte} values converted to their {@code String} representation
	 */
	public static String byteToHex(byte[] byteArray) {
		return byteToHex(byteArray, byteArray.length);
	}
	
	/**
	 * Converts the given {@code byte} array to its {@code String}
	 * representation. This method also requires a specific limit which says how many
	 * elements should be processed.
	 * <p>
	 * For example, an array {0, 1, 127} will be
	 * converted to "00017f", as "00" is a hexadecimal representation
	 * of a {@code byte} value 0, "01" of a value 1 and "7f" of a value 127.
	 *
	 * @param byteArray array of {@code byte} values to be converted to {@code String}
	 * @param limit limits the number of bytes that will be processed
	 * @return {@code byte} values converted to their {@code String} representation
	 */
	public static String byteToHex(byte[] byteArray, int limit) {
		if(byteArray.length == 0) return "";
		
		var sb = new StringBuilder();
		
		for(int i = 0; i < limit; i++) {
			int b = byteArray[i];
			
			int left = ((b & 0xff) >>> 4); // Remove first 24 bits and shift by 4 to get bits 5 to 8.
			int right = b & 0x0f; // Right digit will simply be the last 4 bits.
			
			sb.append(intToHex[left]);
			sb.append(intToHex[right]);
		}
		
		return sb.toString();
	}
	
	private static char requireValidHexDigit(char digit) {
		if(validHexSymbols.indexOf(digit) < 0)
			throw new IllegalArgumentException("Symbol '" + digit + "' is not a valid hexadecimal digit.");
		
		return digit;
	}
}
