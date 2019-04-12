package hr.fer.zemris.java.hw05.db;

/**
 * Strategy that defines {@code String} operators.
 *
 * @author Filip Nemec
 */
public interface IComparisonOperator {
	
	/**
	 * Checks if the given {@code String} objects
	 * satisfy the defined criteria.
	 *
	 * @param value1 the first {@code String}
	 * @param value2 the second {@code String}
	 * @return {@code true} if the given {@code Strings} satisfy the given
	 * 		   criteria, {@code false} otherwise
	 */
	boolean satisfied(String value1, String value2);
}
