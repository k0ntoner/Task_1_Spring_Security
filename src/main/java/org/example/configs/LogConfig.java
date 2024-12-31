package org.example.configs;
import ch.qos.logback.classic.*;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "org.example")
public class LogConfig {
    @Value("${log.info.file.path}")
    String infoFilePath;

    @Value("${log.warn.file.path}")
    String warnFilePath;

    @Value("${log.error.file.path}")
    String errorFilePath;

    @Bean
    public Logger infoLogger() {
        return configureLogger("com.example.info", Level.INFO, infoFilePath,false);
    }
    @Bean
    public Logger warnLogger() {
        return configureLogger("com.example.warn", Level.WARN, warnFilePath, false);
    }
    @Bean
    public Logger errorLogger() {
        return configureLogger("com.example.error", Level.ERROR, errorFilePath, true);
    }
    private Logger configureLogger(String LoggerName, Level level, String filePath, boolean logToConsole){
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(LoggerName);

        ((ch.qos.logback.classic.Logger) logger).setAdditive(false);

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(filePath);
        fileAppender.setAppend(true);

        PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
        fileEncoder.setContext(context);
        fileEncoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        fileEncoder.start();

        fileAppender.setEncoder(fileEncoder);
        fileAppender.setContext(context);
        fileAppender.start();

        ((ch.qos.logback.classic.Logger) logger).addAppender(fileAppender);

        if (logToConsole) {
            ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
            PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
            consoleEncoder.setContext(context);
            consoleEncoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
            consoleEncoder.start();

            consoleAppender.setEncoder(consoleEncoder);
            consoleAppender.setContext(context);
            consoleAppender.start();

            ((ch.qos.logback.classic.Logger) logger).addAppender(consoleAppender);
        }

        ((ch.qos.logback.classic.Logger) logger).setLevel(level);


        return logger;
    }
}
