package hr.fer.zemris.java.hw05.db;

/**
 * Models objects whose job is to retrieve a certain
 * field from the {@code StudentRecord} instance.
 *
 * @author Filip Nemec
 */
public class FieldValueGetters {
	
	/** Field value getter that retrieves first name from the student record. */
	public static final IFieldValueGetter FIRST_NAME = record -> record.getFirstName();
	
	/** Field value getter that retrieves last name from the student record. */
	public static final IFieldValueGetter LAST_NAME = record -> record.getLastName();
	
	/** Field value getter that retrieves jmbag from the student record. */
	public static final IFieldValueGetter JMBAG = record -> record.getJmbag();
}
