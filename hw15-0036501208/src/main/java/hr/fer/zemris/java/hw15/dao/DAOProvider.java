package hr.fer.zemris.java.hw15.dao;

import hr.fer.zemris.java.hw15.dao.jpa.JPADAOImpl;

/**
 * The provider of the DAO implementation.
 *
 * @author Filip Nemec
 */
public class DAOProvider {

	/** The implementation of the DAO. */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Returns the DAO implementation.
	 *
	 * @return the DAO implementation
	 */
	public static DAO getDAO() {
		return dao;
	}
}