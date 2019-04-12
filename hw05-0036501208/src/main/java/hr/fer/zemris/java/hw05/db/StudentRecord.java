package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * A simple class encapsulating a single
 * student record.
 * 
 * @author Filip Nemec
 */
public class StudentRecord {
	
	/** Student's jmbag. */
	private final String jmbag;
	
	/** Student's last name. */
	private final String lastName;
	
	/** Student's first name. */
	private final String firstName;
	
	/** Student's final grade. */
	private final int finalGrade;
	
	/**
	 * Constructs a new student record.
	 *
	 * @param jmbag student's jmbag
	 * @param lastName student's last name
	 * @param firstName student's first name
	 * @param finalGrade student's final grade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Returns the student's jmbag.
	 * @return the student's jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}
	
	/**
	 * Returns the student's last name.
	 * @return the student's last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Returns the student's first name.
	 * @return the student's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	
	/**
	 * Returns the student's final grade.
	 * @return the student's final final
	 */
	public int getFinalGrade() {
		return finalGrade;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
	
	@Override
	public String toString() {
		return jmbag + " " + lastName + " " + firstName + " " + finalGrade;
	}
}
