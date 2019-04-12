package hr.fer.zemris.java.hw05.db;

/**
 * Represents objects that act as filters for
 * {@code StudentRecord} objects.
 *
 * @author Filip Nemec
 */
public interface IFilter {
	
	/**
	 * Evaluates if the given student record satisfies
	 * the defined criteria.
	 *
	 * @param record record to be evaluated
	 * @return {@code true} if record is accepted, {@code false} otherwise
	 */
	boolean accepts(StudentRecord record);
}