package hr.fer.zemris.java.hw14.database;

/**
 * Models a single poll.
 *
 * @author Filip Nemec
 */
public class Poll {
	
	/** The poll id. */
	private final String id;
	
	/** The poll title. */
	private final String title;
	
	/** The poll message. */
	private final String message;
	
	/**
	 * Constructs a new poll.
	 *
	 * @param id the id
	 * @param title the title
	 * @param message the message
	 */
	public Poll(String id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Returns the poll id.
	 *
	 * @return the poll id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the poll title.
	 *
	 * @return the poll title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the poll message.
	 *
	 * @return the poll message
	 */
	public String getMessage() {
		return message;
	}
}
