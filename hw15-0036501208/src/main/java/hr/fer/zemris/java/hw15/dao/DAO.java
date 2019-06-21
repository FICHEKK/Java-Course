package hr.fer.zemris.java.hw15.dao;

import java.util.List;

import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * The DAO (Data Access Object) that offers the
 * persistent data saving and loading interface.
 *
 * @author Filip Nemec
 */
public interface DAO {
	
	//---------------------------------------------------------------------------
	//								BLOG USERS
	//---------------------------------------------------------------------------
	
	/**
	 * Returns the user mapped by the provided primary key {@code id}.
	 *
	 * @param id the user's id
	 * @return the user mapped by the provided primary key {@code id}, or
	 * 		   {@code null} if the user does not exist
	 * @throws DAOException if data fetching error occurs
	 */
	BlogUser getBlogUser(Long id) throws DAOException;
	
	/**
	 * Returns the user mapped by the provided nickname.
	 *
	 * @param nick the user's nickname
	 * @return the user mapped by the provided nickname, or
	 * 		   {@code null} if the user does not exist
	 * @throws DAOException if data fetching error occurs
	 */
	BlogUser getBlogUser(String nick) throws DAOException;
	
	/**
	 * Returns the {@code List} of all the existing blog users.
	 *
	 * @return the {@code List} of all the existing blog users
	 * @throws DAOException if data fetching error occurs
	 */
	List<BlogUser> getAllBlogUsers() throws DAOException;
	
	/**
	 * Saves the blog user to persistent memory.
	 *
	 * @param blogUser the blog user to be saved
	 * @throws DAOException if data saving error occurs
	 */
	void persistBlogUser(BlogUser blogUser) throws DAOException;
	
	
	
	//---------------------------------------------------------------------------
	//								BLOG ENTRIES
	//---------------------------------------------------------------------------
	
	/**
	 * Returns the entry mapped by the provided primary key {@code id}.
	 *
	 * @param id the entry id
	 * @return the entry mapped by the provided primary key {@code id}, or
	 * 		   {@code null} if the entry does not exist
	 * @throws DAOException if data fetching error occurs
	 */
	BlogEntry getBlogEntry(Long id) throws DAOException;
	
	/**
	 * Returns the list of all entries written by the given author.
	 *
	 * @param author the author of all the entries
	 * @return the list of all entries written by the given author
	 * @throws DAOException if data fetching error occurs
	 */
	List<BlogEntry> getBlogEntries(BlogUser author) throws DAOException;
	
	/**
	 * Saves the blog entry to persistent memory.
	 *
	 * @param blogEntry the blog entry to be saved
	 * @throws DAOException if data saving error occurs
	 */
	void persistBlogEntry(BlogEntry blogEntry) throws DAOException;
	
	
	
	//---------------------------------------------------------------------------
	//								BLOG COMMENTS
	//---------------------------------------------------------------------------
	
	/**
	 * Saves the blog comment to persistent memory.
	 *
	 * @param blogComment the blog comment to be saved
	 * @throws DAOException if data saving error occurs
	 */
	void persistBlogComment(BlogComment blogComment) throws DAOException;
}