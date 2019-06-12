package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.database.Poll;
import hr.fer.zemris.java.hw14.database.PollOption;
import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * Servlet that prepares the list of the possible poll options
 * that can be voted for. Poll options are sorted in descending
 * order by their IDs.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int pollID = Integer.parseInt(req.getParameter("pollID"));
		
		Poll poll 					 = DAOProvider.getDao().getPoll(pollID);
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);
		
		req.getSession().setAttribute("pollID", pollID);
		req.getSession().setAttribute("pollTitle", poll.getTitle());
		req.getSession().setAttribute("pollMessage", poll.getMessage());
		
		req.getSession().setAttribute("pollOptions", pollOptions);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
