package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.crypto.Crypto;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;
import hr.fer.zemris.java.hw15.web.forms.LoginForm;

/**
 * Servlet that prepares the data for the main (front)
 * page mapped on {@code /servleti/main}.
 *
 * @author Filip Nemec
 */
@WebServlet(name = "main", urlPatterns = "/servleti/main")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 6607437870071424831L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAuthorsToAttributes(req);
		
		LoginForm form = new LoginForm();
		form.fillFromBlogUser(new BlogUser());
		req.setAttribute("form", form);
		
		req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
	}

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setAuthorsToAttributes(req);
		
    	LoginForm form = new LoginForm();
    	form.fillFromHttpRequest(req);
    	form.validate();
        
        // Checking if the form was filled in correctly.
        if(form.hasAnyError()) {
        	req.setAttribute("form", form);
        
        // The form was filled in correctly.
        } else {
            BlogUser user = new BlogUser();
            form.fillInBlogUser(user);
            
            BlogUser userFromDB = DAOProvider.getDAO().getBlogUser(user.getNick());
            
            // Provided nick does not exist.
            if(userFromDB == null) {
            	form.setError("loginFail", "Invalid nick and/or password");
            	req.setAttribute("form", form);
            
            // Provided nick does exist! Does the password match?
            } else {
                String providedPasswordHash = Crypto.getPasswordHash(user.getPasswordHash());
                
                // Success!
                if(providedPasswordHash.equals(userFromDB.getPasswordHash())) {
                    req.getSession().setAttribute("current.user.id", userFromDB.getId());
                    req.getSession().setAttribute("current.user.fn", userFromDB.getFirstName());
                    req.getSession().setAttribute("current.user.ln", userFromDB.getLastName());
                    req.getSession().setAttribute("current.user.nick", userFromDB.getNick());
                    req.getSession().setAttribute("current.user.email", userFromDB.getEmail());
                	
                } else {
                	form.setError("loginFail", "Invalid nick and/or password");
                	req.setAttribute("form", form);
                	
                }
            }
        }
        
        req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
    }
	
    private void setAuthorsToAttributes(HttpServletRequest req) {
    	List<BlogUser> authors = DAOProvider.getDAO().getAllBlogUsers();
		req.setAttribute("authors", authors);
	}
}
