package loggly.log4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import loggly.log4j.threading.LogglyAppenderPool;

import org.apache.log4j.spi.LoggingEvent;

public class LogglyAppender extends LogglyAppenderBase {

	public static final String ENDPOINT_URL_PATH = "inputs/";
	public static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Override
	public void append(LoggingEvent eventObject) {
		String msg = this.layout.format(eventObject);
		logglyThreadPool.addTask(new LogglyAppenderPool(msg,
				endpointUrl,
				proxy,
				this.layout.getContentType()));
	}
	
	@Override
    protected String getEndpointPrefix() {
        return ENDPOINT_URL_PATH;
    }
	
		@Override
	public void close() {
		logglyThreadPool.shutdown();
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}
	
}
