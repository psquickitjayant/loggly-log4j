package loggly.log4j;

import java.net.InetSocketAddress;
import java.net.Proxy;
import loggly.log4j.threading.LogglyThreadPool;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;

public abstract class LogglyAppenderBase extends AppenderSkeleton {

	public static final String DEFAULT_ENDPOINT_PREFIX = "https://logs-01.loggly.com/";
    public static final String DEFAULT_LAYOUT_PATTERN = "%d{ISO8601} %p %t %c{1}.%M - %m%n";
    protected String endpointUrl;
    protected String inputKey;
    protected Layout layout;
    protected boolean layoutCreatedImplicitly = false;
    private String pattern;
    private int proxyPort;
    private String proxyHost;
    protected Proxy proxy;
    private int httpReadTimeoutInMillis = 1000;
    protected LogglyThreadPool logglyThreadPool;
	
    
    public void activateOptions(){
		logglyThreadPool = new LogglyThreadPool(50);
		
		ensureLayout();
        
		if (this.endpointUrl == null) {
            if (this.inputKey == null) {
                System.out.println("inputKey (or alternatively, endpointUrl) must be configured");
            } else {
                this.endpointUrl = buildEndpointUrl(this.inputKey);
            }
        }

        if (this.proxyHost == null || this.proxyHost.isEmpty()) {
            // don't set it to Proxy.NO_PROXY (i.e. Proxy.Type.DIRECT) as the meaning is different (user-jvm-proxy-config vs. don't use proxy)
            this.proxy = null;
        } else {
            this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
        }
	}
	
   protected final void ensureLayout() {
        if (this.layout == null) {
          this.layout = createLayout();
            this.layoutCreatedImplicitly = true;
        }
    }

    protected Layout createLayout() {
        PatternLayout layout = new PatternLayout();
        String pattern = getPattern();
        if (pattern == null) {
            pattern = DEFAULT_LAYOUT_PATTERN;
        }
        layout.setConversionPattern(pattern);
        return (Layout) layout;
    }

    protected String buildEndpointUrl(String inputKey) {
        return new StringBuilder(DEFAULT_ENDPOINT_PREFIX).append(getEndpointPrefix())
                .append(inputKey).toString();
    }
    
    /**
     * Returns the URL path prefix for the Loggly endpoint to which the 
     * implementing class will send log events. This path prefix varies
     * for the different Loggly services. The final endpoint URL is built
     * by concatenating the {@link #DEFAULT_ENDPOINT_PREFIX} with the
     * endpoint prefix from {@link #getEndpointPrefix()} and the 
     * {@link #inputKey}.
     *
     * @return the URL path prefix for the Loggly endpoint
     */
    protected abstract String getEndpointPrefix();

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        String cleaned = inputKey;
        if (cleaned != null) {
            cleaned = cleaned.trim();
        }
        if ("".equals(cleaned)) {
            cleaned = null;
        }
        this.inputKey = cleaned;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }
    public void setProxyPort(String proxyPort) {
        if(proxyPort == null || proxyPort.trim().isEmpty()) {
            proxyPort = "0";
        }
        this.proxyPort = Integer.parseInt(proxyPort);
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        if(proxyHost == null || proxyHost.trim().isEmpty()) {
            proxyHost = null;
        }
        this.proxyHost = proxyHost;
    }

    public int getHttpReadTimeoutInMillis() {
        return httpReadTimeoutInMillis;
    }

    public void setHttpReadTimeoutInMillis(int httpReadTimeoutInMillis) {
        this.httpReadTimeoutInMillis = httpReadTimeoutInMillis;
    }
}
