package hr.fer.zemris.java.hw15.web.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Models the registration form.
 *
 * @author Filip Nemec
 */
public class LoginForm {

	/** User's nick. */
	private String nick;

	/** The password. */
	private String password;

	/**
	 * Map of errors. Keys are properties, while values are error messages.
	 */
	private Map<String, String> errors = new HashMap<>();

	/**
	 * Returns the error for the specified property.
	 *
	 * @param property the property
	 * @return the error message for the given property, or {@code null} if there is
	 *         no error
	 */
	public String getError(String property) {
		return errors.get(property);
	}

	/**
	 * Returns whether this form has any errors.
	 *
	 * @return {@code true} if there are any errors, {@code false} otherwise
	 */
	public boolean hasAnyError() {
		return !errors.isEmpty();
	}

	/**
	 * Checks if the given property contains any errors.
	 *
	 * @param property the property to be checked
	 * @return {@code true} if property has an error, {@code false} otherwise
	 */
	public boolean hasError(String property) {
		return errors.containsKey(property);
	}
	
	/**
	 * Sets the form error for the given property.
	 *
	 * @param property the property
	 * @param message the error message
	 */
	public void setError(String property, String message) {
		errors.put(property, message);
	}

	/**
	 * Based on the HTTP request, fill this user form.
	 *
	 * @param req the request holding the form data
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.nick = prepare(req.getParameter("nick"));
		this.password = prepare(req.getParameter("password"));
	}

	/**
	 * Fills this form based on data stored in the provided {@code BlogUser}.
	 *
	 * @param user the {@code BlogUser} storing the data
	 */
	public void fillFromBlogUser(BlogUser user) {
		this.nick = prepare(user.getNick());
		this.password = prepare(user.getPasswordHash());
	}

	/**
	 * Fills in the given {@code BlogUser}, based on the data in this form.
	 *
	 * @param user the user to be filled
	 */
	public void fillInBlogUser(BlogUser user) {
		user.setNick(nick);
		user.setPasswordHash(password);
	}

	/**
	 * Validates this form, searching for any errors that might appear in it.
	 */
	public void validate() {
		errors.clear();
		
		//-----------------------------------------------------
		// 					Nick validation
		//-----------------------------------------------------
		
		if (nick.isEmpty()) {
			errors.put("nick", "Nick is required!");
		}
		
		//-----------------------------------------------------
		// 					Password validation
		//-----------------------------------------------------
		
		if(password.isEmpty()) {
			errors.put("password", "Password is required!");
		}
	}

	/**
	 * Helper method for converting {@code null String} to an empty {@code String.}
	 * 
	 * @param s the {@code String} to be converted
	 * @return given {@code String} (but trimmed) if it is not {@code null}, empty
	 *         {@code String} otherwise
	 */
	private String prepare(String s) {
		if (s == null)
			return "";
		return s.trim();
	}

	/**
	 * Returns the nick from this form.
	 *
	 * @return the nick from this form
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the nick of this form.
	 *
	 * @param nick the new nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Returns the password from this form.
	 *
	 * @return the password from this form
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of this form.
	 *
	 * @param passwordHash the new password
	 */
	public void setPassword(String passwordHash) {
		this.password = passwordHash;
	}
}
