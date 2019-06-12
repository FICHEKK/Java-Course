package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.database.PollOption;
import hr.fer.zemris.java.p12.dao.DAOProvider;


/**
 * Servlet that processes the voting results.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeRezultatiServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int pollID = (int) req.getSession().getAttribute("pollID");
		
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);
		
		Collections.sort(pollOptions, PollOption.BY_VOTE_COUNT.reversed());
		
		req.getSession().setAttribute("pollOptionsSorted", pollOptions);
		req.setAttribute("mostVotedPollOptions", getMostVotedOptions(pollOptions));
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}
	
	/**
	 * Returns the list of the bands with the most votes. There
	 * can be multiple bands if they have the same number of votes.
	 *
	 * @param sortedBands a list of bands that is sorted from highest
	 * 				      voted to the least voted
	 * @return the list of the bands with the most votes
	 */
	private List<PollOption> getMostVotedOptions(List<PollOption> sorted) {
		List<PollOption> mostVoted = new LinkedList<>();
		
		if(!sorted.isEmpty()) {
			mostVoted.add(sorted.get(0));
			int mostVotes = sorted.get(0).getVotes();
			
			for(int i = 1; i < sorted.size(); i++) {
				int bandVotes = sorted.get(i).getVotes();
				if(bandVotes < mostVotes) break;
				
				mostVoted.add(sorted.get(i));
			}
		}
		
		return mostVoted;
	}
}
