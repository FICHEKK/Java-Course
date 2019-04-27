package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Models objects that perform a specific operation.
 *
 * @author Filip Nemec
 */
interface Operation {
	
	/**
	 * Performs the specified operation on the 2
	 * given {@code int} values.
	 *
	 * @param i1 the first {@code int}
	 * @param i2 the second {@code int}
	 * @return the result of the operation
	 */
	int asInt(int i1, int i2);
	
	/**
	 * Performs the specified operation on the 2
	 * given {@code double} values.
	 *
	 * @param d1 the first {@code double}
	 * @param d2 the second {@code double}
	 * @return the result of the operation
	 */
	double asDouble(double d1, double d2);
}
