package hr.fer.zemris.java.hw16.gallery;

/**
 * Models a single gallery picture. This class
 * encapsulates the picture title, picture description
 * and picture's tags.
 *
 * @author Filip Nemec
 */
public class Picture {
	
	/** The title. */
	private String title;
	
	/** The description. */
	private String description;
	
	/** The collection of tags. */
	private String[] tags;
	
	/**
	 * Constructs a new picture.
	 *
	 * @param title the title
	 * @param description the description
	 * @param tags the tags
	 */
	public Picture(String title, String description, String[] tags) {
		this.title = title;
		this.description = description;
		this.tags = tags;
	}

	/**
	 * Returns the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the tags.
	 *
	 * @return the tags
	 */
	public String[] getTags() {
		return tags;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		
		sb.append("TITLE: " + title + "\r\n");
		sb.append("DESCRIPTION: " + description + "\r\n");
		
		sb.append("TAGS: ");
		for(String tag : tags) {
			sb.append(tag + " ");
		}
		
		return sb.toString();
	}
}