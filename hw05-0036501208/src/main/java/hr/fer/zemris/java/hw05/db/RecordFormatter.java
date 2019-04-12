package hr.fer.zemris.java.hw05.db;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple package-private class that formats the given list
 * of database's {@code StudentRecord} objects.
 *
 * @author Filip Nemec
 */
class RecordFormatter {
	
	/**
	 * Formats the given list of records.
	 *
	 * @param records the records to be formatted
	 * @return the formatted list of records
	 */
	public static List<String> format(List<StudentRecord> records) {
		if(records.isEmpty()) return null;
		
		List<String> formattedRecords = new LinkedList<String>();
		
		int longestLastName = findLongest(records, FieldValueGetters.LAST_NAME);
		int longestFirstName = findLongest(records, FieldValueGetters.FIRST_NAME);
		
		for(StudentRecord r : records) {
			var sb = new StringBuilder();
			
			String jmbag = r.getJmbag();
			String last = r.getLastName();
			String first = r.getFirstName();
			int grade = r.getFinalGrade();
			
			sb.append("| " + jmbag + " | ");
			sb.append(last + getChars(' ', longestLastName - last.length()) + " | ");
			sb.append(first + getChars(' ', longestFirstName - first.length()) + " | ");
			sb.append(grade + " |");
			
			formattedRecords.add(sb.toString());
		}
		
		if(formattedRecords.isEmpty()) return null;
		
		appendHeaderAndFooter(formattedRecords, longestLastName, longestFirstName);

		return formattedRecords;
	}
	
	/**
	 * Appends the header and footer to the start and to the
	 * end of the database table.
	 *
	 * @param formattedRecords list that header and footer will be appended to
	 */
	private static void appendHeaderAndFooter(List<String> formattedRecords, int longestLastName, int longestFirstName) {
		var borderBuilder = new StringBuilder();
		
		borderBuilder.append("+" + getChars('=', 12));
		borderBuilder.append("+" + getChars('=', longestLastName + 2));
		borderBuilder.append("+" + getChars('=', longestFirstName + 2));
		borderBuilder.append("+===+");
		
		String border = borderBuilder.toString();
		
		int first = 0;
		int last = formattedRecords.size() - 1;
		formattedRecords.set(first, border + "\n" + formattedRecords.get(first));
		formattedRecords.set(last , formattedRecords.get(last) + "\n" + border);
	}

	/**
	 * Returns the longest field length. Field being processes is defined
	 * by the given {@code IFieldValueGetter}.
	 *
	 * @param records the list of records
	 * @param fieldGetter the field getter strategy
	 * @return the length of the longest field found
	 */
	private static int findLongest(List<StudentRecord> records, IFieldValueGetter fieldGetter) {
		int longest = 0;
		
		for(StudentRecord r : records) {
			int fieldLength = fieldGetter.get(r).length();
			
			if(fieldLength > longest) {
				longest = fieldLength;
			}
		}
		
		return longest;
	}
	
	/**
	 * Returns a {@code String} consisting of only the given character.
	 * The length of the returning {@code String} is defined by
	 * the argument {@code numberOfChars}.
	 *
	 * @param whichChar the character
	 * @param numberOfChars the length of the returning {@code String}
	 * @return {@code String} consisting of only the given character
	 */
	private static String getChars(char whichChar, int numberOfChars) {
		var sb = new StringBuilder();
		
		for(int i = 0; i < numberOfChars; i++)
			sb.append(whichChar);
		
		return sb.toString();
	}
}
