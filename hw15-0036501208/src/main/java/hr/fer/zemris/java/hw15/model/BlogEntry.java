package hr.fer.zemris.java.hw15.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Models a single blog entry.
 *
 * @author Filip Nemec
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {
	
	/** The id of this blog entry. */
	private Long id;
	
	/** The collection of comments for this blog entry. */
	private List<BlogComment> comments = new ArrayList<>();
	
	/** The time-stamp of creation. */
	private Date createdAt;
	
	/** The time-stamp of the last modification. */
	private Date lastModifiedAt;
	
	/** The title. */
	private String title;
	
	/** The text of this blog entry. */
	private String text;
	
	/** The creator of this blog entry. */
	private BlogUser creator;
	
	//-------------------------------------------------------------
	//					  		GETTERS
	//-------------------------------------------------------------
	
	/**
	 * Returns the id of this blog entry.
	 *
	 * @return the id of this blog entry
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Returns the list of comments for this blog entry.
	 *
	 * @return the list of comments for this blog entry
	 */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Returns the creation timestamp.
	 *
	 * @return the creation timestamp
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * Returns the last modification timestamp.
	 *
	 * @return the last modification timestamp
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}
	
	/**
	 * Returns the title of this blog entry.
	 *
	 * @return the title of this blog entry
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the text of this blog entry.
	 *
	 * @return the text of this blog entry
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}
	
	/**
	 * Returns the creator of this blog entry.
	 *
	 * @return the creator of this blog entry
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getCreator() {
		return creator;
	}
	
	//-------------------------------------------------------------
	//					  		SETTERS
	//-------------------------------------------------------------
	
	/**
	 * Sets the id of this blog entry.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the list of comments for this blog entry.
	 *
	 * @param comments the new list of comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}
	
	/**
	 * Sets the creation timestamp.
	 *
	 * @param createdAt the new creation timestamp.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Sets the last modification timestamp.
	 *
	 * @param lastModifiedAt the new last modification timestamp
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Sets the title of this blog entry.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the text for this blog entry.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Sets the creator for this blog entry.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}