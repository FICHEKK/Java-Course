package hr.fer.zemris.java.hw05.db;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hr.fer.zemris.java.hw05.db.exceptions.StudentDatabaseException;

/**
 * A simple representation of a student database.
 * Internally, it hold a reference to the list of
 * objects that are instances of {@code StudentRecord}
 * class.
 * <p>
 * It also offers direct O(1) access to a student record if
 * the jmbag is provided.
 *
 * @author Filip Nemec
 */
public class StudentDatabase {
	
	/** List holding all the student records in this database. */
	private List<StudentRecord> studentRecords = new LinkedList<StudentRecord>();
	
	/** Map that maps all the indexes to its corresponding student record. */
	private Map<String, StudentRecord> indexer = new HashMap<String, StudentRecord>();
	
	/**
	 * Constructs a new student database from the given list
	 * of {@code String} database rows.
	 *
	 * @param databaseRows the list of {@code String} database rows
	 */
	public StudentDatabase(List<String> databaseRows) {
		Objects.requireNonNull(databaseRows, "Provided list should not be null");
		
		for(String row : databaseRows) {
			processRow(row);
		}
	}
	
	/**
	 * Processes the given database row.
	 *
	 * @param row the database row
	 * @throws NullPointerException if the given row is {@code null}
	 * @throws StudentDatabaseException if the given row is of invalid format
	 */
	private void processRow(String row) {
		Objects.requireNonNull(row, "Cannot process a null row.");
		
		String[] attributes = row.split("\\s+");
		
		if(attributes.length < 4) {
			String msg = "'" + row + "' is invalid. Expected 4 attributes: " +
					     "JMBAG, last name, first name and final grade.";
			throw new StudentDatabaseException(msg);
		}
		
		String jmbag     = extractJMBAG(attributes);
		String lastName  = extractLastName(attributes);
		String firstName = extractFirstName(attributes);
		int finalGrade   = extractFinalGrade(attributes);
		
		StudentRecord record = new StudentRecord(jmbag, lastName, firstName, finalGrade);
		studentRecords.add(record);
		indexer.put(jmbag, record);
	}
	
	/**
	 * Extracts and returns the final grade from the attributes.
	 *
	 * @param attributes the attributes
	 * @return the integer value that represents the final grade
	 * @throws StudentDatabaseException if the given grade is out of the valid range
	 * @throws IllegalArgumentException if the given grade could not be parsed to integer
	 */
	private static int extractFinalGrade(String[] attributes) {
		String gradeString = attributes[attributes.length - 1];
		
		try {
			int grade = Integer.parseInt(gradeString);

			if(grade < 1 || grade > 5) {
				String msg = "Grade should be in range from 1 to 5. Given value was: " + grade;
				throw new StudentDatabaseException(msg);
			}

			return grade;
		} catch(NumberFormatException nfe) {
			throw new IllegalArgumentException("'" + gradeString + "' could not be parsed to integer.");
		}
	}
	
	/**
	 * Extracts and returns the first name from the attributes.
	 *
	 * @param attributes the attributes
	 * @return the {@code String} that represents the first name
	 */
	private static String extractFirstName(String[] attributes) {
		return attributes[attributes.length - 2];
	}

	/**
	 * Extracts and returns the last name from the attributes.
	 *
	 * @param attributes the attributes
	 * @return the {@code String} that represents the last name
	 */
	private static String extractLastName(String[] attributes) {
		var lastName = new StringBuilder();
		
		for(int i = 1, last = attributes.length - 3; i <= last; i++) {
			lastName.append(attributes[i]);
			
			if(i != last) {
				lastName.append(" ");
			}
		}
		return lastName.toString();
	}
	
	/**
	 * Extracts and returns the JMBAG from the attributes.
	 *
	 * @param attributes the attributes
	 * @return the {@code String} that represents the JMBAG
	 * @throws StudentDatabaseException if duplicate jmbag student record was provided
	 */
	private String extractJMBAG(String[] attributes) {
		String jmbag = attributes[0];
		
		if(indexer.containsKey(jmbag)) {
			throw new StudentDatabaseException("Duplicate JMBAG '" + jmbag + "'.");
		} else {
			return jmbag;
		}
	}
	
	/**
	 * Returns the student record for the given jmbag
	 * in O(1) time-complexity.
	 *
	 * @param jmbag the student record jmbag
	 * @return the student record for the given jmbag
	 * @throws StudentDatabaseException if student with given jmbag was not found 
	 */
	public StudentRecord forJMBAG(String jmbag) {
		Objects.requireNonNull(jmbag, "Cannot find student records for a null key.");
		StudentRecord direct = indexer.get(jmbag);
		
		if(direct == null)
			throw new StudentDatabaseException("Student with index '" + jmbag + "' was not found.");
		
		return direct;
	}
	
	/**
	 * Filters this database using the given filter.
	 *
	 * @param filter the filter
	 * @return list of student records that passed the filter test
	 */
	public List<StudentRecord> filter(IFilter filter) {
		var temp = new LinkedList<StudentRecord>();
		
		for(StudentRecord record : studentRecords) {
			if(filter.accepts(record)) {
				temp.add(record);
			}
		}
		
		return temp;
	}
}
