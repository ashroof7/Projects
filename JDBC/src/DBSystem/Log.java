package DBSystem;

import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

public class Log {
	private Logger logs;
	private static  Log logger;
	
	private Log() {
		logs = Logger.getLogger(Log.class);
		DOMConfigurator.configure("Configuration.xml");
	}

	public static Logger getLogger() {
		if(logger==null){
			logger = new Log();
		}
		return logger.logs;
	}

}
