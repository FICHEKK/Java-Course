package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Established the database connection and sets up the
 * database tables for multiple voting polls.
 *
 * @author Filip Nemec
 */
@WebListener
public class InitializationListener implements ServletContextListener {
	
	/** An array of available polls. */
	private static final String[] POLLS = {
			"'Vote for your favourite band!', 'Click to vote!'",
			"'Choose your favourite color!', 'Click on color to vote!'"
	};

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String connectionURL = getConnectionURL(Paths.get(sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties")));

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
			
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pool initialization error.", e1);
			
		}
		
		cpds.setJdbcUrl(connectionURL);
		
		try {
			createPollsTable(cpds);
			
			int[] pollIDs = new int[POLLS.length];
			
			Connection connection = cpds.getConnection();
			PreparedStatement statement = connection.prepareStatement("select id from Polls");
			ResultSet result = statement.executeQuery();
			
			for(int i = 0; result.next(); i++) {
				pollIDs[i] = result.getInt(1);
			}
			
			createPollOptionsTable(cpds, pollIDs);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Creates and fills the "Polls" database table if it does not exist, or
	 * if it is empty (contains 0 rows).
	 *
	 * @param cpds the data source
	 * @throws SQLException if an SQL error occurs
	 */
	private void createPollsTable(ComboPooledDataSource cpds) throws SQLException {
		boolean wasCreated = createTable(cpds, "CREATE TABLE Polls (\r\n" + 
											   " id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + 
											   " title VARCHAR(150) NOT NULL,\r\n" + 
											   " message CLOB(2048) NOT NULL\r\n" + 
											   ")");
		
		Connection connection = cpds.getConnection();
		
		if(wasCreated) {
			fillPollsTable(connection);
			
		} else {
			ResultSet result = connection.prepareStatement("select count(*) from Polls").executeQuery();
			result.next();
			if(result.getInt(1) == 0) {
				fillPollsTable(connection);
			}
		}
	}
	
	/**
	 * Creates and fills the "PollOptions" database table if it does not exist, or
	 * if it is empty (contains 0 rows).
	 *
	 * @param cpds the data source
	 * @throws SQLException if an SQL error occurs
	 */
	private void createPollOptionsTable(ComboPooledDataSource cpds, int[] pollIDs) throws SQLException {
		boolean wasCreated = createTable(cpds, "CREATE TABLE PollOptions (\r\n" + 
											   " id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + 
											   " optionTitle VARCHAR(100) NOT NULL,\r\n" + 
											   " optionLink VARCHAR(150) NOT NULL,\r\n" + 
											   " pollID BIGINT,\r\n" + 
											   " votesCount BIGINT,\r\n" + 
											   " FOREIGN KEY (pollID) REFERENCES Polls(id)\r\n" + 
											   ")");
		
		Connection connection = cpds.getConnection();
		
		if(wasCreated) {
			fillPollOptionsTable(connection, pollIDs);
			
		} else {
			ResultSet result = connection.prepareStatement("select count(*) from PollOptions").executeQuery();
			result.next();
			if(result.getInt(1) == 0) {
				fillPollOptionsTable(connection, pollIDs);
			}
		}
	}
	
	/**
	 * Fills the "Polls" database table with polls defined in
	 * the {@linkplain #POLLS} array.
	 *
	 * @param conn the database connection
	 * @throws SQLException if an SQL error occurs
	 */
	private void fillPollsTable(Connection conn) throws SQLException {
		insert(conn, "Polls", "title, message", POLLS);
	}
	
	/**
	 * Fills the "PollOptions" database table with some predefined
	 * options.
	 *
	 * @param conn the database connection
	 * @param pollID an array of pollIDs used in the table filling process
	 * @throws SQLException if an SQL error occurs
	 */
	private void fillPollOptionsTable(Connection conn, int[] pollID) throws SQLException {
		int bandPollID  = pollID[0];
		int colorPollID = pollID[1];
		
		String[] VALUES = {
				"'The Imagine Dragons', 'https://www.youtube.com/watch?v=7wtfhZwyrcc', " + bandPollID + ", 0" ,
				"'twenty one pilots'  , 'https://www.youtube.com/watch?v=UprcpdwuwCg', " + bandPollID + ", 0" ,
				"'Queen'			  , 'https://www.youtube.com/watch?v=0AIlz08fZos', " + bandPollID + ", 0" ,
				"'The Beatles'		  , 'https://www.youtube.com/watch?v=A_MjCqQoLLA', " + bandPollID + ", 0" ,
				"'Guns N'' Roses'	  , 'https://www.youtube.com/watch?v=1w7OgIMMRc4', " + bandPollID + ", 0" ,
				"'Coldplay'			  , 'https://www.youtube.com/watch?v=1G4isv_Fylg', " + bandPollID + ", 0" ,
				"'Panic! At The Disco', 'https://www.youtube.com/watch?v=7qFF2v8VsaA', " + bandPollID + ", 0" ,

				"'Red'		, 'https://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Color_icon_red.svg/2000px-Color_icon_red.svg.png', " + colorPollID + ", 0" ,
				"'Green'  	, 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Color_icon_green.svg/2000px-Color_icon_green.svg.png', " + colorPollID + ", 0" ,
				"'Blue'		, 'https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Color_icon_blue.svg/1024px-Color_icon_blue.svg.png', " + colorPollID + ", 0" ,
				"'Yellow'	, 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Color_icon_yellow.svg/2000px-Color_icon_yellow.svg.png', " + colorPollID + ", 0" 
		};
		
		insert(conn, "PollOptions", "optionTitle, optionLink, pollID, votesCount", VALUES);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//============================================================
	//						HELPER METHODS
	//============================================================
	
	/**
	 * Performs the insert SQL operation on a batch of data.
	 *
	 * @param conn the connection
	 * @param table the table to be inserted into
	 * @param attrs the attributes
	 * @param valuesArr the array of values
	 * @throws SQLException if an SQL error occurs
	 */
	private void insert(Connection conn, String table, String attrs, String[] valuesArr) throws SQLException {
		for(String values : valuesArr) {
			conn.prepareStatement("INSERT INTO " + table + " (" + attrs + ") " + "VALUES (" + values + ")").executeUpdate();
		}
	}
	
	/**
	 * Attempts to create a new database table. If the creation was
	 * successful, {@code true} is returned, otherwise {@code false}
	 * is returned.
	 *
	 * @param cpds the data-source
	 * @param sql the sql statement
	 * @return {@code true} if the table was created, {@code false} otherwise
	 */
	private boolean createTable(ComboPooledDataSource cpds, String sql) {
		try {
			cpds.getConnection().prepareStatement(sql).execute();
			return true;
			
		} catch (SQLException e) {
			if(e.getSQLState().equals("X0Y32")) {
				System.out.println("This table already exists.");
				return false;
			}

			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates the connection URL from the provided properties
	 * file and returns it.
	 *
	 * @param propertiesPath the path to the properties file
	 * @return the connection URL
	 */
	private String getConnectionURL(Path propertiesPath) {
		Properties properties = new Properties();
		
		try {
			properties.load(Files.newInputStream(propertiesPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String host = properties.getProperty("host");
		String port = properties.getProperty("port");
		String name = properties.getProperty("name");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		
		if(host 	== null) throw new RuntimeException("Missing property 'host'");
		if(port 	== null) throw new RuntimeException("Missing property 'port'");
		if(name 	== null) throw new RuntimeException("Missing property 'name'");
		if(user 	== null) throw new RuntimeException("Missing property 'user'");
		if(password == null) throw new RuntimeException("Missing property 'password'");
		
		return "jdbc:derby://" + host + ":" + port + "/" + name + ";user=" + user + ";password=" + password;
	}
}