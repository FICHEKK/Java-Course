package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.p12.dao.DAOProvider;

/**
 * A servlet that updates the "results" file once
 * the user has voted.
 *
 * @author Filip Nemec
 */
@SuppressWarnings("serial")
public class GlasanjeGlasajServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int optionID = Integer.parseInt(req.getParameter("id"));
		DAOProvider.getDao().vote(optionID);
		
		resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati");
	}
}
