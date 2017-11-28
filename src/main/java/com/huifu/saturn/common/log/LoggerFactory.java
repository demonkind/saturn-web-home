/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class LoggerFactory extends LogFactory {
    public static Logger getLogger(Class clazz) {
        return getLogger(getLog(clazz));
    }

    public static Logger getLogger(String name) {
        return getLogger(getLog(name));
    }

    private static Logger getLogger(Log log) {
        if (log instanceof Logger) {
            return (Logger) log;
        }
        return new LoggerWrapper(log);
    }
}

