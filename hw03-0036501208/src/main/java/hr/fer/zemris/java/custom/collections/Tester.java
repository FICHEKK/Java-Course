package hr.fer.zemris.java.custom.collections;

/**
 * Represents a tester that determines if a given value should
 * return {@code true} or {@code false}.
 */
@FunctionalInterface
public interface Tester {
	/**
	 * Tests whether the given object satisfies the given criteria.
	 * 
	 * @param obj object being tested
	 * @return {@code true} if the given object passed the test,
	 * 		   {@code false} otherwise
	 */
	boolean test(Object obj);
}
