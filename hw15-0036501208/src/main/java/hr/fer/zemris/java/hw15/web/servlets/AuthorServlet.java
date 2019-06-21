package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Processes every single request made on {@code /servleti/author}.
 *
 * @author Filip Nemec
 */
@WebServlet(name = "author", urlPatterns = "/servleti/author/*")
public class AuthorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 643258528965771447L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(req.getServletPath());
		System.out.println(req.getPathInfo());
		
		String afterAuthor = req.getPathInfo();
		
		// Check if the path is empty.
		if(afterAuthor == null || afterAuthor.equals("/")) {
			req.setAttribute("errorMessage", "This page does not contain any data. Did you mean <b>/author/author_nick</b> ?");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
		
		String[] parts = afterAuthor.substring(1).split("/");
		
		for(String s : parts)
			System.out.println(s);
		
		if(parts.length == 1) {
			listAuthorEntries(parts[0], req, resp);
			
		} else if(parts.length == 2) {
			if(parts[1].equals("new")) {
				req.setAttribute("actionTitle", "Create a new blog entry!");
				req.setAttribute("action", "new");
				req.getRequestDispatcher("/WEB-INF/pages/new_edit.jsp").forward(req, resp);
				
			} else {
				displayEntry(parts[0], parts[1], req, resp);
				
			}
		} else if(parts.length == 3) {
			if(parts[2].equals("edit")) {
				req.setAttribute("actionTitle", "Edit this blog entry!");
				req.setAttribute("action", "edit");
				req.setAttribute("entry", DAOProvider.getDAO().getBlogEntry(Long.parseLong(parts[1])));
				req.getRequestDispatcher("/WEB-INF/pages/new_edit.jsp").forward(req, resp);
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String afterAuthor = req.getPathInfo().substring(1);
		
		String[] parts = afterAuthor.split("/");
		
		String author = parts[0];
		String action = parts[1];
		
		if(action.equals("comment")) {
			postComment(req.getParameter("message"), req.getParameter("email"), req.getParameter("entryID"));
			resp.sendRedirect(req.getParameter("entryID"));
			
		} else if(action.equals("new")) {
			Long entryID = newEntry(req.getParameter("title"), req.getParameter("text"), author);
			resp.sendRedirect(String.valueOf(entryID));
			
		} else if(action.equals("edit")) {
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.parseLong(req.getParameter("entryID")));
			entry.setTitle(req.getParameter("title"));
			entry.setText(req.getParameter("text"));
			
			resp.sendRedirect(req.getParameter("entryID"));
		}
	}
	
	/**
	 * Creates a new entry and saves it to the database.
	 *
	 * @param title   the entry's title
	 * @param text    the entry's text
	 * @param creator the entry's creator
	 * @return
	 */
	private Long newEntry(String title, String text, String creator) {
		BlogEntry entry = new BlogEntry();
		
		entry.setTitle(title);
		entry.setText(text);
		entry.setCreatedAt(new Date());
		entry.setLastModifiedAt(new Date());
		entry.setCreator(DAOProvider.getDAO().getBlogUser(creator));
		entry.setComments(new LinkedList<BlogComment>());
		
		DAOProvider.getDAO().persistBlogEntry(entry);
		return entry.getId();
	}
	
	/**
	 * Creates a new comment and saves it to the database.
	 *
	 * @param message the comment's message
	 * @param email the e-mail of the user posting the comment
	 * @param entryID the ID of the blog entry this comment is being posted to
	 */
	private void postComment(String message, String email, String entryID) {
		BlogComment comment = new BlogComment();
		
		comment.setMessage(message);
		comment.setUsersEMail(email);
		comment.setPostedOn(new Date());
		comment.setBlogEntry(DAOProvider.getDAO().getBlogEntry(Long.parseLong(entryID)));
		
		DAOProvider.getDAO().persistBlogComment(comment);
	}

	/**
	 * Prepares the data for the displaying of the blog entry.
	 *
	 * @param author  the author of the entry
	 * @param entryID the id of the entry
	 * @param req     the HTTP request
	 * @param resp    the HTTP response
	 * @throws ServletException if a servlet error occurs
	 * @throws IOException      if an IO error occurs
	 */
	private void displayEntry(String author, String entryID, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser authorUser = DAOProvider.getDAO().getBlogUser(author);
		
		if(authorUser == null) {
			req.setAttribute("errorMessage", "Author '" + author + "' was not found.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			return;
		}
		
		try {
			Long id = Long.parseLong(entryID);
			BlogEntry entry = DAOProvider.getDAO().getBlogEntry(id);
			
			if(entry == null) {
				req.setAttribute("errorMessage", "Entry with ID " + id + " was not found.");
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
				
			} else {
				if(!authorUser.getNick().equals(entry.getCreator().getNick())) {
					req.setAttribute("errorMessage", "Entry with ID " + id + " was not created by '" + authorUser.getNick() + "'.");
					req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
					
				} else {
					req.setAttribute("author", authorUser);
					req.setAttribute("entry", entry);
					req.setAttribute("comments", entry.getComments());
					req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);
					
				}
			}
			
		} catch(NumberFormatException e) {
			req.setAttribute("errorMessage", entryID + " is not a valid entry ID.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
		}
	}

	/**
	 * Retrieves a list of author's entries and passes those entries to the
	 * {@code authorEntries.jsp}.
	 *
	 * @param author the author
	 * @param req    the HTTP request
	 * @param resp   the HTTP response
	 * @throws ServletException if a servlet error occurs
	 * @throws IOException      if an IO error occurs
	 */
	private void listAuthorEntries(String author, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BlogUser authorUser = DAOProvider.getDAO().getBlogUser(author);
		
		if(authorUser == null) {
			req.setAttribute("errorMessage", "Author '" + author + "' was not found.");
			req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			
		} else {
			req.setAttribute("author", authorUser);
			req.setAttribute("entries", DAOProvider.getDAO().getBlogEntries(authorUser));
			req.getRequestDispatcher("/WEB-INF/pages/authorEntries.jsp").forward(req, resp);
		}
	}
}
