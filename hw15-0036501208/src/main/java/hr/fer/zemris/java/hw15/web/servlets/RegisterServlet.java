package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.web.forms.RegistrationForm;

/**
 * Registration servlet that processes the user forms
 * and stores new users to the database if the form
 * is valid.
 *
 * @author Filip Nemec
 */
@WebServlet(name = "register", urlPatterns = "/servleti/register")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 5792286393487637812L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegistrationForm form = new RegistrationForm();
        
        // Creates an empty form.
        form.fillFromBlogUser(new BlogUser());

        req.setAttribute("form", form);
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegistrationForm form = new RegistrationForm();
        form.fillFromHttpRequest(req);
        form.validate();
        
        // Checking if the form was filled in correctly.
        if(form.hasAnyError()) {
        	req.setAttribute("form", form);
        	req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
        	return;
        }
        
        BlogUser user = new BlogUser();
        form.fillInBlogUser(user);

        DAOProvider.getDAO().persistBlogUser(user);

        req.getSession().setAttribute("current.user.id", user.getId());
        req.getSession().setAttribute("current.user.fn", user.getFirstName());
        req.getSession().setAttribute("current.user.ln", user.getLastName());
        req.getSession().setAttribute("current.user.nick", user.getNick());
        req.getSession().setAttribute("current.user.email", user.getEmail());

        req.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(req, resp);
    }
}