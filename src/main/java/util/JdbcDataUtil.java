package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import config.DBConfig;
import constants.ApplicationConstants;

/**
 * @author Pinakin Abhyankar (psabhyan)
 *
 * This is a utility class which handles the connections with database. This should be used by the dao layer classes
 * to get new connections and close the existing connection.
 * It uses DBConfig class which is used to store database configuration parameters read from
 * properties file.
 */
public class JdbcDataUtil {

	private static Connection con=null;
	public static DBConfig dbConfig;
	private static Logger loggers = Logger.getLogger(JdbcDataUtil.class);
	private static String propsFileName;
	private Statement stm;


	/**
	 * This is default constructor which uses default properties file name set in Application Constants.
	 */
	public JdbcDataUtil()
	{
		this.propsFileName= ApplicationConstants.JDBC_CONFIG;

	}

	public JdbcDataUtil(String propsfilename)
	{
		this.propsFileName=propsfilename;
	}

	/**
	 * Checks database connection.
	 * @return boolean whether connection with database is live or not.
	 */
	public static boolean checkConnection(  )
	{
		Statement stmt = null;
		ResultSet rst = null;
		boolean isConnected = false;
		try {
			if(con != null && !con.isClosed())
			{
				stmt = con.createStatement();

				stmt.executeUpdate("CREATE TABLE Greetings (Message CHAR(20))");
				stmt.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!')");

				rst = stmt.executeQuery("SELECT * FROM Greetings");
				if (rst.next())
					isConnected = true;
				stmt.executeUpdate("DROP TABLE Greetings");
			}
		} catch (Exception e) {
			con=null;
			loggers.error("JDBCDATAUtil error ", e);
			isConnected = false;
		}
		finally
		{
			try {
				if(rst != null)
				{
					rst.close();
				}
				if(stmt != null)
				{
					stmt.close();
				}
			} catch (SQLException e) {
				loggers.exception(e);
			}
		}
		return isConnected;
	}

	/**
	 * This method provides a database connection object which can be used to perform any database operation.
	 * @return JDBC Connection object.
	 */
	public static Connection getConnection()
	{

		try {
			if(con != null && !con.isClosed())
			{
				return con;
			}
			if(dbConfig == null)
			{
				dbConfig = new DBConfig();
			}

			Class.forName(dbConfig.getDriverClass());
			con = null;
			loggers.debug("Initiating connection to database");
			con = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUserName(), dbConfig.getPassword());			
		} catch (Exception e) {
				loggers.error("JDBCDATAUtil error ", e);
		}
		return con;
	}

	/**
	 * This method is used to close the existing JDBC connection.
	 * It is a good practice to close the connection once the DB operations are completed.
	 */
	public void closeConnection()
	{
		loggers.debug("Closing connection to database");
		try {
			if(con != null)
			{
				con.close();
			}
		} catch (Exception e) {
			loggers.error("JDBCDATAUtil error ", e);
		} 
		loggers.debug("Connection closed successfully.");
	}
}
