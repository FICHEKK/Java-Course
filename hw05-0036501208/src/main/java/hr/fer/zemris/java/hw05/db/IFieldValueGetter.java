package hr.fer.zemris.java.hw05.db;

/**
 * Strategy that defines which {@code StudentRecord}
 * attribute should be retrieved.
 *
 * @author Filip Nemec
 */
public interface IFieldValueGetter {
	
	/**
	 * Returns the defined {@code StudentRecord} field.
	 *
	 * @param record the record
	 * @return the defined {@code StudentRecord} field
	 */
	String get(StudentRecord record);
}
