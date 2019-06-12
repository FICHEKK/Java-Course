package hr.fer.zemris.java.p12.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.database.Poll;
import hr.fer.zemris.java.hw14.database.PollOption;

/**
 * Defines the 'Data Access Object' objects.
 *
 * @author Filip Nemec
 */
public interface DAO {
	
	/**
	 * Returns the poll with the provided id.
	 *
	 * @param id the poll id
	 * @return the poll with the provided id
	 */
	Poll getPoll(int id);
	
	/**
	 * Returns all of the polls.
	 *
	 * @return all of the polls
	 */
	List<Poll> getPolls();
	
	/**
	 * Returns all of the poll options for the
	 * specified poll.
	 *
	 * @param pollID the poll id
	 * @return all of the poll options for the specified poll
	 */
	List<PollOption> getPollOptions(int pollID);
	
	/**
	 * Votes for the selected poll option. Selection is
	 * defined by the argument {@code optionID}.
	 *
	 * @param optionID the option to vote for
	 */
	void vote(int optionID);
}