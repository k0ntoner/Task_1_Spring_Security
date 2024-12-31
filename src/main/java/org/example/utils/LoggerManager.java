package org.example.utils;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LoggerManager {
    private Logger warnLogger;
    private Logger infoLogger;
    private Logger errorLogger;
    @Autowired
    public LoggerManager(@Qualifier("infoLogger") Logger infoLogger, @Qualifier("warnLogger") Logger warnLogger, @Qualifier("errorLogger") Logger errorLogger ) {
        this.warnLogger = warnLogger;
        this.infoLogger = infoLogger;
        this.errorLogger = errorLogger;
    }

    public void warn(String message) {
        warnLogger.warn(message);
    }
    public void warn(String message, Object... args) {
        warnLogger.warn(message, args);
    }
    public void info(String message) {
        infoLogger.info(message);
    }
    public void info(String message, Object... args) {
        infoLogger.info(message, args);
    }
    public void error(String message) {
        errorLogger.error(message);
    }
    public void error(String message, Object... args) {
        errorLogger.error(message, args);
    }
}
