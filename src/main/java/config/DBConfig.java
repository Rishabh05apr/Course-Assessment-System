package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import constants.ApplicationConstants;
import util.Logger;

/**
 * This is a configuration class which loads database configurations from properties file
 * and stores them into class variables.
 * This is used by JDBCDataUtil class for obtaining connection with database.
 */
public class DBConfig {

	/**
	 * These are the parameters required in order to make connection with database.
	 */
	private static Logger logger = Logger.getLogger(DBConfig.class);
	private String  driverClass;
	private String  url;
	private String  userName;
	private String  password;
	private String isEncrypted ="no";
	private Properties prop = null;


	/**
	 * This method loads configuration parameters from default properties file
	 */
	public DBConfig()
	{
		loadConfig(ApplicationConstants.JDBC_CONFIG);
	}

	/**
	 * This method loads configuration parameters from properties file provided as param.
	 * @param propFile path of the properties file which contains JDBC connection parameters.
	 */
	public DBConfig(String propFile)
	{
		loadConfig(propFile);
	}

	private void loadConfig(String propFile) {
		InputStream fis = null;
		if(propFile == null)
			propFile = ApplicationConstants.JDBC_CONFIG;
		try {
			if(prop == null){	
				prop = new Properties();
				fis = this.getClass().getClassLoader().getResourceAsStream(propFile);
				prop.load(fis);
				logger.info("Database Properties Loaded : " + propFile);
			}
			driverClass=prop.getProperty("driverClass");
			if(driverClass == null)
			{
				logger.error("Database driverClass not configured.");
			}
			url=prop.getProperty("url");
			if(url == null)
			{
				logger.error("Database url not configured.");
			}
			userName=prop.getProperty("username");
			if(userName == null)
			{
				logger.error("Database userName not configured.");
			}
			isEncrypted=prop.getProperty("isEncrypted");
			if(isEncrypted == null || isEncrypted.equalsIgnoreCase("no"))
			{
				password=prop.getProperty("password");
			}
			else
			{
				//password=EncryptPassUtil.decrypt(prop.getProperty("password"));
			}
			if(password == null)
			{
				logger.error("Database password not configured.");
			}

		} catch (FileNotFoundException e) {
			logger.error("DBConfig", e);
		} catch (IOException e) {
			logger.error("DBConfig", e);
		} catch (Exception e) {
			logger.error("DBConfig", e);
		}

	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
