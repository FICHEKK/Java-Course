package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw14.database.Poll;
import hr.fer.zemris.java.hw14.database.PollOption;
import hr.fer.zemris.java.p12.dao.DAO;

/**
 * The SQL implementation of the Data Access Object (DAO).
 * This implementation uses the database as its data container,
 * and also offers basic data retrieval methods.
 *
 * @author Filip Nemec
 */
public class SQLDAO implements DAO {

	@Override
	public Poll getPoll(int pollID) {
		Connection conn = SQLConnectionProvider.getConnection();
		Poll poll = null;
		
		try(PreparedStatement statement = conn.prepareStatement("select * from Polls where id = " + pollID);
			ResultSet result = statement.executeQuery()) {
			
			while(result.next()) {
				String id      = result.getString(1);
				String title   = result.getString(2);
				String message = result.getString(3);
				
				poll = new Poll(id, title, message);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
		return poll;
	}

	@Override
	public List<Poll> getPolls() {
		Connection conn = SQLConnectionProvider.getConnection();
		List<Poll> polls = new LinkedList<>();
		
		try(PreparedStatement statement = conn.prepareStatement("select * from Polls");
			ResultSet result = statement.executeQuery()) {
			
			while(result.next()) {
				String id      = result.getString(1);
				String title   = result.getString(2);
				String message = result.getString(3);
				
				polls.add(new Poll(id, title, message));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return polls;
	}

	@Override
	public List<PollOption> getPollOptions(int pollID) {
		Connection conn = SQLConnectionProvider.getConnection();
		List<PollOption> pollOptions = new LinkedList<>();
		
		try(PreparedStatement statement = conn.prepareStatement("select * from PollOptions where pollID = " + pollID);
			ResultSet result = statement.executeQuery()) {
			
			while(result.next()) {
				String id      = result.getString(1);
				String title   = result.getString(2);
				String link    = result.getString(3);
				int votes 	   = result.getInt(5);
				
				PollOption option = new PollOption(id, title, link);
				option.setVotes(votes);
				
				pollOptions.add(option);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return pollOptions;
	}

	@Override
	public void vote(int optionID) {
		Connection conn = SQLConnectionProvider.getConnection();
		
		try(PreparedStatement statement = conn.prepareStatement("update PollOptions set votesCount = votesCount + 1 where id = " + optionID)) {
			statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}