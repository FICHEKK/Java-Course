package hr.fer.zemris.java.hw15.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Models a single user.
 *
 * @author Filip Nemec
 */
@Entity
@Table(name = "blog_users")
public class BlogUser {
	
	/** The user's id. */
	private Long id;
	
	/** The user's first name. */
	private String firstName;
	
	/** The user's last name. */
	private String lastName;
	
	/** The user's nickname. */
	private String nick;
	
	/** The user's e-mail. */
	private String email;
	
	/** The user's password hash. */
	private String passwordHash;
	
	/** A collection of user's blogs. */
	private List<BlogEntry> blogs;
	
	//-------------------------------------------------------------
	//					  		GETTERS
	//-------------------------------------------------------------
	
	/**
	 * Constructs a new blog user.
	 *
	 * @param firstName    the users' first name
	 * @param lastName     the user's last name
	 * @param nick         the user's nickname
	 * @param email        the user's e-mail
	 * @param passwordHash the user's password hash
	 */
	public BlogUser(String firstName, String lastName, String nick, String email, String passwordHash) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nick = nick;
		this.email = email;
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Default constructs. Must be defined as this is an 'Entity' class.
	 */
	public BlogUser() {
	}
	
	//-------------------------------------------------------------
	//					  		GETTERS
	//-------------------------------------------------------------
	
	/**
	 * Returns the user's id.
	 *
	 * @return the user's id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Returns the user's first name.
	 *
	 * @return the user's first name
	 */
	@Column(nullable = false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Returns the user's last name.
	 *
	 * @return the user's last name
	 */
	@Column(nullable = false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Returns the user's nickname.
	 *
	 * @return the user's nickname
	 */
	@Column(nullable = false, unique = true)
	public String getNick() {
		return nick;
	}
	
	/**
	 * Returns the user's e-mail.
	 *
	 * @return the user's e-mail
	 */
	@Column(nullable = false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Returns the user's password hash.
	 *
	 * @return the user's password hash
	 */
	@Column(nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Returns the user's blog collection.
	 *
	 * @return the user's blog collection
	 */
	@OneToMany(mappedBy = "creator")
	public List<BlogEntry> getBlogs() {
		return blogs;
	}
	
	
	
	//-------------------------------------------------------------
	//					  		SETTERS
	//-------------------------------------------------------------
	
	/**
	 * Sets the user's id.
	 *
	 * @param id the new user's id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Sets the user's first name.
	 *
	 * @param firstName the new user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Sets the user's last name.
	 *
	 * @param lastName the new user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Sets the user's nick.
	 *
	 * @param nick the new user's nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * Sets the user's e-mail.
	 *
	 * @param email the new user's e-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Sets the user's password hash.
	 *
	 * @param passwordHash the new user's password hash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	/**
	 * Sets the user's blog collection.
	 *
	 * @param blogs the new user's blog collection
	 */
	public void setBlogs(List<BlogEntry> blogs) {
		this.blogs = blogs;
	}
	
	
	
	//-------------------------------------------------------------
	//					  hashCode and equals
	//-------------------------------------------------------------
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(id, other.id);
	}
}
