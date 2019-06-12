package hr.fer.zemris.java.p12.dao;

import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * A singleton object which returns the defined DAO
 * implementation.
 *
 * @author Filip Nemec
 */
public class DAOProvider {

	/** The SQL implementation of DAO. */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Returns the Data Access Object.
	 *
	 * @return
	 */
	public static DAO getDao() {
		return dao;
	}
}