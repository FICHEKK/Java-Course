package hr.fer.zemris.java.hw14.database;

import java.util.Comparator;

/**
 * Representation of a single poll option.
 *
 * @author Filip Nemec
 */
public class PollOption {
	
	/** Compares the poll options by their vote count. */
	public static final Comparator<PollOption> BY_VOTE_COUNT = (p1, p2) -> p1.getVotes().compareTo(p2.getVotes());
	
	/** The unique ID for the poll option. */
	public final String id;
	
	/** The title of the poll option. */
	public final String title;
	
	/** The link of the poll option. */
	public final String link;
	
	/** The number of votes that this poll option has. */
	private Integer votes;
	
	/**
	 * Constructs a new poll option.
	 *
	 * @param id the poll option id
	 * @param title the poll option title
	 * @param link the link of the poll option
	 */
	public PollOption(String id, String title, String link) {
		this.id = id;
		this.title = title;
		this.link = link;
	}
	
	/**
	 * Changes the vote count for this poll option.
	 *
	 * @param votes new vote count
	 */
	public void setVotes(Integer votes) {
		this.votes = votes;
	}
	
	/**
	 * Returns the vote count of this poll option.
	 *
	 * @return the vote count of this poll option
	 */
	public Integer getVotes() {
		return votes;
	}
}
