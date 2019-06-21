package hr.fer.zemris.java.hw15.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.hw15.dao.DAO;
import hr.fer.zemris.java.hw15.dao.DAOException;
import hr.fer.zemris.java.hw15.model.BlogComment;
import hr.fer.zemris.java.hw15.model.BlogEntry;
import hr.fer.zemris.java.hw15.model.BlogUser;

/**
 * The implementation of the DAO (Data Access Object). This implementation uses
 * the JPA (Java Persistence API) technology to manipulate data from database
 * using ORM (Object-relational mapping).
 *
 * @author Filip Nemec
 */
public class JPADAOImpl implements DAO {
	
	//---------------------------------------------------------------------------
	//								BLOG USERS
	//---------------------------------------------------------------------------

	@Override
	public BlogUser getBlogUser(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}
	
	@Override
	public BlogUser getBlogUser(String nick) throws DAOException {
		List<BlogUser> users = JPAEMProvider.getEntityManager()
								.createQuery("SELECT user FROM BlogUser AS user WHERE user.nick = :n", BlogUser.class)
								.setParameter("n", nick)
								.getResultList();
		
		return users.isEmpty() ? null : users.get(0);
	}
	
	@Override
	public List<BlogUser> getAllBlogUsers() throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createQuery("SELECT user FROM BlogUser AS user", BlogUser.class)
				.getResultList();
	}
	
	@Override
	public void persistBlogUser(BlogUser blogUser) throws DAOException {
		JPAEMProvider.getEntityManager().persist(blogUser);
	}
	
	
	
	//---------------------------------------------------------------------------
	//								BLOG ENTRIES
	//---------------------------------------------------------------------------
	
	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}
	
	@Override
	public void persistBlogEntry(BlogEntry blogEntry) throws DAOException {
		JPAEMProvider.getEntityManager().persist(blogEntry);
	}
	
	@Override
	public List<BlogEntry> getBlogEntries(BlogUser author) throws DAOException {
		return JPAEMProvider.getEntityManager()
				.createQuery("SELECT entry FROM BlogEntry AS entry WHERE entry.creator = :a", BlogEntry.class)
				.setParameter("a", author)
				.getResultList();
	}
	
	//---------------------------------------------------------------------------
	//								BLOG COMMENTS
	//---------------------------------------------------------------------------

	@Override
	public void persistBlogComment(BlogComment blogComment) throws DAOException {
		JPAEMProvider.getEntityManager().persist(blogComment);
	}
}