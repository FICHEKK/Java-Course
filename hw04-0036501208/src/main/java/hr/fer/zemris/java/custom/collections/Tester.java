package hr.fer.zemris.java.custom.collections;

/**
 * Represents a tester that determines if a given value should
 * return {@code true} or {@code false}.
 * 
 * @param <T> the type of elements that this tester will be testing
 */
@FunctionalInterface
public interface Tester<T> {
	/**
	 * Tests whether the given object satisfies the given criteria.
	 * 
	 * @param obj object being tested
	 * @return {@code true} if the given object passed the test,
	 * 		   {@code false} otherwise
	 */
	boolean test(T obj);
}
