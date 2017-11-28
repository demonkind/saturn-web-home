/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common.log;


import java.util.ResourceBundle;

import org.apache.commons.logging.Log;

public class LoggerWrapper implements Logger {
    private Log            log;
    private ResourceBundle bundle;

    public LoggerWrapper(Log log) {
        this.log = log;
    }

    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return log.isFatalEnabled();
    }

    public void trace(Object message) {
        log.trace(message);
    }

    public void trace(Object message, Throwable cause) {
        log.trace(message, cause);
    }

    public void trace(Object key, Object[] params) {
        if (log.isTraceEnabled()) {
            log.trace(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void trace(Object key, Object[] params, Throwable cause) {
        if (log.isTraceEnabled()) {
            log.trace(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public void debug(Object message) {
        log.debug(message);
    }

    public void debug(Object message, Throwable cause) {
        log.debug(message, cause);
    }

    public void debug(Object key, Object[] params) {
        if (log.isDebugEnabled()) {
            log.debug(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void debug(Object key, Object[] params, Throwable cause) {
        if (log.isDebugEnabled()) {
            log.debug(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public void info(Object message) {
        log.info(message);
    }

    public void info(Object message, Throwable cause) {
        log.info(message, cause);
    }

    public void info(Object key, Object[] params) {
        if (log.isInfoEnabled()) {
            log.info(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void info(Object key, Object[] params, Throwable cause) {
        if (log.isInfoEnabled()) {
            log.info(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public void warn(Object message) {
        log.warn(message);
    }

    public void warn(Object message, Throwable cause) {
        log.warn(message, cause);
    }

    public void warn(Object key, Object[] params) {
        if (log.isWarnEnabled()) {
            log.warn(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void warn(Object key, Object[] params, Throwable cause) {
        if (log.isWarnEnabled()) {
            log.warn(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public void error(Object message) {
        log.error(message);
    }

    public void error(Object message, Throwable cause) {
        log.error(message, cause);
    }

    public void error(Object key, Object[] params) {
        if (log.isErrorEnabled()) {
            log.error(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void error(Object key, Object[] params, Throwable cause) {
        if (log.isErrorEnabled()) {
            log.error(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public void fatal(Object message) {
        log.fatal(message);
    }

    public void fatal(Object message, Throwable cause) {
        log.fatal(message, cause);
    }

    public void fatal(Object key, Object[] params) {
        if (log.isFatalEnabled()) {
            log.fatal(LoggerUtil.getMessage(this, key, params));
        }
    }

    public void fatal(Object key, Object[] params, Throwable cause) {
        if (log.isFatalEnabled()) {
            log.fatal(LoggerUtil.getMessage(this, key, params), cause);
        }
    }

    public ResourceBundle getResourceBundle() {
        return bundle;
    }

    public void setResourceBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String toString() {
        return log.toString();
    }

}

