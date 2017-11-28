/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common.log;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LoggerUtil {
    public static String getMessage(Logger logger, Object key, Object[] params) {
        ResourceBundle bundle  = logger.getResourceBundle();
        String         keyStr  = String.valueOf(key);
        String         message = null;

        if (bundle == null) {
            logger.error(new StringBuffer().append("Resource bundle not set for logger \"")
                                           .append(logger).append("\"").toString());
        } else {
            try {
                message = MessageUtil.getMessage(bundle, keyStr, params);
            } catch (MissingResourceException e) {
                logger.error(new StringBuffer().append("No resource is associated with key \"")
                                               .append(keyStr).append("\" in logger \"")
                                               .append(logger).append("\"").toString(), e);
            }
        }

        return (message == null) ? keyStr
                                 : message;
    }
}
