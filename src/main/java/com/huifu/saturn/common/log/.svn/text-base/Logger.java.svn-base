/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common.log;


import org.apache.commons.logging.Log;

import java.util.ResourceBundle;

public interface Logger extends Log {
    void trace(Object key, Object[] params);

    void trace(Object key, Object[] params, Throwable cause);

    void debug(Object key, Object[] params);

    void debug(Object key, Object[] params, Throwable cause);

    void info(Object key, Object[] params);

    void info(Object key, Object[] params, Throwable cause);

    void warn(Object key, Object[] params);

    void warn(Object key, Object[] params, Throwable cause);

    void error(Object key, Object[] params);

    void error(Object key, Object[] params, Throwable cause);

    void fatal(Object key, Object[] params);

    void fatal(Object key, Object[] params, Throwable cause);

    ResourceBundle getResourceBundle();

    void setResourceBundle(ResourceBundle bundle);

    String toString();
}
