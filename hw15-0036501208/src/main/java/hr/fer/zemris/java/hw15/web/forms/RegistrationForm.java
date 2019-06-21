package hr.fer.zemris.java.hw15.web.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.hw15.crypto.Crypto;
import hr.fer.zemris.java.hw15.dao.DAOProvider;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * Models the registration form.
 *
 * @author Filip Nemec
 */
public class RegistrationForm {

	/** User's first name. */
	private String firstName;

	/** User's last name. */
	private String lastName;

	/** User's nick. */
	private String nick;

	/** User's e-mail. */
	private String email;

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
	 * Based on the HTTP request, fill this user form.
	 *
	 * @param req the request holding the form data
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.firstName = prepare(req.getParameter("firstName"));
		this.lastName = prepare(req.getParameter("lastName"));
		this.nick = prepare(req.getParameter("nick"));
		this.email = prepare(req.getParameter("email"));
		this.password = prepare(req.getParameter("password"));
	}

	/**
	 * Fills this form based on data stored in the provided {@code BlogUser}.
	 *
	 * @param user the {@code BlogUser} storing the data
	 */
	public void fillFromBlogUser(BlogUser user) {
		this.firstName = prepare(user.getFirstName());
		this.lastName = prepare(user.getLastName());
		this.email = prepare(user.getEmail());
		this.nick = prepare(user.getNick());
		this.password = prepare(user.getPasswordHash());
	}

	/**
	 * Fills in the given {@code BlogUser}, based on the data in this form.
	 *
	 * @param user the user to be filled
	 */
	public void fillInBlogUser(BlogUser user) {
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNick(nick);
		user.setPasswordHash(Crypto.getPasswordHash(password));
	}

	/**
	 * Validates this form, searching for any errors that might appear in it.
	 */
	public void validate() {
		errors.clear();
		
		//-----------------------------------------------------
		// 				First name validation
		//-----------------------------------------------------
		
		if (firstName.isEmpty()) {
			errors.put("firstName", "First name is required!");
		}
		
		//-----------------------------------------------------
		// 				Last name validation
		//-----------------------------------------------------
		
		if (lastName.isEmpty()) {
			errors.put("lastName", "Last name is required!");
		}
		
		//-----------------------------------------------------
		// 					Nick validation
		//-----------------------------------------------------
		
		if (nick.isEmpty()) {
			errors.put("nick", "Nick is required!");
			
		} else {
			if(DAOProvider.getDAO().getBlogUser(nick) != null) {
				errors.put("nick", "This nick is already taken.");
			}
		}
		
		//-----------------------------------------------------
		// 					E-mail validation
		//-----------------------------------------------------
		
		if (email.isEmpty()) {
			errors.put("email", "E-mail is required!");
		} else {
			int l = email.length();
			int p = email.indexOf('@');

			if (l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "E-mail is not of valid format.");
			}
		}
		
		//-----------------------------------------------------
		// 					Password validation
		//-----------------------------------------------------
		
		if(password.isEmpty()) {
			errors.put("password", "Password is required!");
		} else {
			if(password.length() < 8) {
				errors.put("password", "Password length must be at least 8 characters.");
			}
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
	 * Returns the first name from this form.
	 *
	 * @return the first name from this form
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of this form.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name from this form.
	 *
	 * @return the last name from this form
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of this form.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * Returns the e-mail of this form.
	 *
	 * @return the e-mail of this form
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the e-mail of this form.
	 *
	 * @param email the new e-mail
	 */
	public void setEmail(String email) {
		this.email = email;
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
