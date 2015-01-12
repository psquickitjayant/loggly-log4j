package loggly.log4j.sample;

import org.apache.log4j.Logger;

public class LogglySample {

	private static final Logger logger = Logger.getLogger(LogglySample.class);
	
	public static void main(String[] args) {
		
		logger.info("This is my first log");
		
		for(int i = 0; i<=400;i++){
			logger.info("log is: "+ i);
			System.out.println("sysout is: "+i);
		}
		
		logger.shutdown();
	}
}
