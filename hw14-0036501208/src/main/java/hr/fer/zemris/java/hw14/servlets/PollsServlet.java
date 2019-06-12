package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Simple servlet displaying all of the available
 * voting polls.
 *
 * @author Filip Nemec
 */
@WebServlet(name = "polls", urlPatterns = {"/servleti/index.html"})
public class PollsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ComboPooledDataSource cpds = (ComboPooledDataSource) req.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		
		try {
			ResultSet result = cpds.getConnection().prepareStatement("select * from Polls").executeQuery();
			
			while(result.next()) {
				String id = result.getString(1);
				String title = result.getString(2);
				
				resp.getOutputStream().write(("<a href=glasanje?pollID=" + id + ">" + title + "</a> <br>").getBytes());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
