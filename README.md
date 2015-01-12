# loggly-log4j
Log4j Appender for Loggly

A Simple Log4j appender to send logs to Loggly via Http or Https asynchronously. 

This uses a custom Threadpool to keep the threads in a pool, increasing the performance of the program. This library supports multiple tags and proxy setup for the network connection. 

Usage:
-----
-----
<b>log4j.properties:</b>
```
  log4j.appender.loggly=loggly.log4j.LogglyAppender
  log4j.appender.loggly.layout=org.apache.log4j.PatternLayout
  log4j.appender.loggly.layout.ConversionPattern=%d{ISO8601} %p %t %c{1}.%M - %m%n
  log4j.appender.loggly.endpointUrl=http://logs-01.loggly.com/inputs/TOKEN/tag/log4j,customTag1,customTag2
```

<b>MainProgram.java</b>
```
  import org.apache.log4j.Logger;
  
  private static final Logger logger = Logger.getLogger(MainProgram.class);
  logger.info("Test log");
```
  
