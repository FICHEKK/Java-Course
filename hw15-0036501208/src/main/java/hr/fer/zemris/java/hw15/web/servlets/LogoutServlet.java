package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple servlet used for logging the user out.
 * It simply invalidates the session and redirects the
 * user to the front page.
 *
 * @author Filip Nemec
 */
@WebServlet(name = "logout", urlPatterns = "/servleti/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 4125202816143884966L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect("main");
    }
}