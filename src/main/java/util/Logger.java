package util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.MDC;

/**
 * This is a Wrapper class for Logger class from log4j so that we can separate the Logging functionality
 * from its dependent class.
 * Configuration for the logs can be obtained from log4j.properties file.
 *
 */
public class Logger extends org.apache.log4j.Logger {

	private org.apache.log4j.Logger logger;

	public static Logger getLogger(Class clazz){
		return new Logger(org.apache.log4j.Logger.getLogger(clazz));
	}

	public static Logger getLogger(String name){
		return new Logger(org.apache.log4j.Logger.getLogger(name));
	}


	protected Logger(String name) {
		super(name);
	}

	public Logger(org.apache.log4j.Logger logger) {
		super("");
		this.logger=logger;
	}


	public void debug(String message) {

		logger.debug(message);

	}

	public void error(String message, Throwable e) {
		e.printStackTrace();
		logger.error(message,e);
		StringWriter stack = new StringWriter();
		e.printStackTrace(new PrintWriter(stack));
	}

	public void exception(Throwable e) {
		logger.error("Exception",e);
		e.printStackTrace();
	}

	public void error(String message) {
		logger.error(message);
	}

	public void info(String message) {
		logger.info(message);

	}

	public void setIdentifier(String key, String value)
	{
		MDC.put(key, value);	
	}
}
