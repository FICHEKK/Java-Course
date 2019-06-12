package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;

/**
 * Provider of the local thread connections.
 *
 * @author Filip Nemec
 */
public class SQLConnectionProvider {

	private static ThreadLocal<Connection> connections = new ThreadLocal<>();
	
	/**
	 * Set a new local thread connection.
	 *
	 * @param con the local thread connection
	 */
	public static void setConnection(Connection con) {
		if(con==null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}
	
	/**
	 * Returns the local thread connection.
	 *
	 * @return the local thread connection
	 */
	public static Connection getConnection() {
		return connections.get();
	}
}