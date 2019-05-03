package hr.fer.zemris.java.hw06.shell.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates the results after the filtering process
 * has been done.
 *
 * @author Filip Nemec
 */
public class FilterResult {
	
	/** The file name. */
	private String fileName;
	
	/** The number of groups that this file name has. */
	private int numberOfGroups;
	
	/** The groups. */
	private String[] groups;
	
	/**
	 * Constructs a new filter result.
	 *
	 * @param fileName the file name
	 * @param pattern the filtering pattern
	 */
	public FilterResult(String fileName, String pattern) {
		this.fileName = fileName;
		this.groups = getAllGroups(pattern);
		this.numberOfGroups = groups.length;
	}
	
	@Override
	public String toString() {
		return fileName;
	}
	
	/**
	 * Returns the number of groups.
	 *
	 * @return the number of groups
	 */
	public int numberOfGroups() {
		return numberOfGroups;
	}
	
	/**
	 * Returns the group for the specified index.
	 *
	 * @param index the group index
	 * @return {@code String} that contains the group
	 * 		   at the given index
	 */
	public String group(int index) {
		if(index < 0 || index >= numberOfGroups) {
			throw new IndexOutOfBoundsException("For index " + index);
		}
		
		return groups[index];
	}
	
	/**
	 * Helper method that extracts all of the groups
	 * from the file name.
	 *
	 * @param pattern the extracting pattern
	 * @return {@code String[]} of all the groups
	 */
	private String[] getAllGroups(String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(fileName);
		
		List<String> groupList = new LinkedList<>();
		
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				groupList.add(m.group(i));
			}
		}
		
		return groupList.toArray(new String[0]);
	}
}
