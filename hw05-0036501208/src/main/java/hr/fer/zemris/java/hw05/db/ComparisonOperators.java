package hr.fer.zemris.java.hw05.db;

/**
 * A simple class that offers simple {@code String} comparison operators.
 * Supported operators are: >, >=, <, <=, !=, = and LIKE.
 * 
 * @author Filip nemec
 */
public class ComparisonOperators {
	
	/** Checks whether the first {@code String} is lexicographically less than the second. */
	public static final IComparisonOperator LESS = (s1, s2) -> s2.compareTo(s1) > 0;
	
	/** Checks whether the first {@code String} is lexicographically less or equal to the second. */
	public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s2.compareTo(s1) >= 0;
	
	/** Checks whether the first {@code String} is lexicographically greater than the second. */
	public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;
	
	/** Checks whether the first {@code String} is lexicographically greater or equal to the second. */
	public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;
	
	/** Checks whether the first {@code String} is equal to the second. */
	public static final IComparisonOperator EQUALS = (s1, s2) -> s1.equals(s2);
	
	/** Checks whether the first {@code String} is not equal to the second. */
	public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> !s1.equals(s2);
	
	/** Checks whether the first {@code String} satisfies the given pattern, which is the second {@code String}. */
	public static final IComparisonOperator LIKE = (s1, s2) -> like(s1, s2);
	
	/**
	 * Checks whether the given {@code sample} meets the given pattern.
	 * 
	 * @param sample the sample being checked
	 * @param pattern the pattern
	 * @return {@code true} if the given {@code sample} meets the given {@code pattern},
	 * 		   {@code false} otherwise
	 * @throws IllegalArgumentException if the given pattern contains more than 1 wildcard
	 */
	private static boolean like(String sample, String pattern) {
		char[] chars = pattern.toCharArray();
		
		int wildcardIndex = 0;
		int wildcardCounter = 0;
		for(int i = 0; i < chars.length; i++) {
			if(chars[i] == '*') {
				wildcardIndex = i;
				wildcardCounter++;
				
				if(wildcardCounter > 1) {
					String msg = "Given pattern can contain at most 1 wildcard character.";
					throw new IllegalArgumentException(msg);
				}
			}
		}
		
		int patternLength = chars.length - wildcardCounter;
		int sampleLength = sample.length();
		
		// Pattern "AA*AA" has length of 4 (without wildcard),
		// and its length should not be higher than the given
		// sample length (for example for sample "AAA").
		if(patternLength > sampleLength) return false;
		
		if(wildcardCounter == 0) {
			return sample.equals(pattern);
		} else {
			if(chars.length == 1) return true;
			
			if(wildcardIndex == 0) {
				return sample.endsWith(pattern.substring(1));
			} else if(wildcardIndex == chars.length - 1) {
				return sample.startsWith(pattern.substring(0, chars.length - 1));
			} else {
				return sample.startsWith(pattern.substring(0, wildcardIndex)) &&
					   sample.endsWith(pattern.substring(wildcardIndex + 1));
			}
		}
	}
}
