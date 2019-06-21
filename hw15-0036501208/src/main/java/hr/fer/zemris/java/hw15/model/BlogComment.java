package hr.fer.zemris.java.hw15.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Models a single blog comment.
 *
 * @author Filip Nemec
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/** The comment id. Used as a primary key in the database. */
	private Long id;
	
	/** The blog entry that this comment belongs to. */
	private BlogEntry blogEntry;
	
	/** The e-mail of the user that posted this comment. */
	private String usersEMail;
	
	/** The message this comment contains. */
	private String message;
	
	/** The timestamp that this comment was posted on. */
	private Date postedOn;
	
	//-------------------------------------------------------------
	//					  		GETTERS
	//-------------------------------------------------------------
	
	/**
	 * Returns the id of this comment.
	 *
	 * @return the id of this comment
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Returns the blog entry that this comment belongs to.
	 *
	 * @return the blog entry that this comment belongs to
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Returns the e-mail of the user posting this comment.
	 *
	 * @return the e-mail of the user posting this comment
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Returns the message of this comment.
	 * 
	 * @return the message of this comment
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the timestamp of when this comment was posted.
	 *
	 * @return the timestamp of when this comment was posted
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	//-------------------------------------------------------------
	//					  		SETTERS
	//-------------------------------------------------------------
	
	/**
	 * Sets the id for this comment.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Sets the blog entry that this comment belongs to.
	 *
	 * @param blogEntry the new blog entry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}
	
	/**
	 * Sets the user e-mail.
	 *
	 * @param usersEMail the new user e-mail
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}
	
	/**
	 * Sets the message for this comment.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Sets the timestamp of when this comment was posted
	 *
	 * @param postedOn the new timestamp
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}
	
	//-------------------------------------------------------------
	//					  hashCode and equals 
	//-------------------------------------------------------------

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}