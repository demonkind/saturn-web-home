/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.common.log;


import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtil {
    public static String getMessage(ResourceBundle bundle, String key, Object[] params) {
        if ((bundle == null) || (key == null)) {
            return key;
        }

        try {
            String message = bundle.getString(key);

            return formatMessage(message, params);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getMessage(ResourceBundle bundle, String key, Object param1) {
        return getMessage(bundle, key, new Object[] { param1 });
    }

    public static String getMessage(ResourceBundle bundle, String key, Object param1, Object param2) {
        return getMessage(bundle, key, new Object[] { param1, param2 });
    }

    public static String getMessage(ResourceBundle bundle, String key, Object param1,
        Object param2, Object param3) {
        return getMessage(bundle, key, new Object[] { param1, param2, param3 });
    }

    public static String getMessage(ResourceBundle bundle, String key, Object param1,
        Object param2, Object param3, Object param4) {
        return getMessage(bundle, key, new Object[] { param1, param2, param3, param4 });
    }

    public static String getMessage(ResourceBundle bundle, String key, Object param1,
        Object param2, Object param3, Object param4, Object param5) {
        return getMessage(bundle, key, new Object[] { param1, param2, param3, param4, param5 });
    }

    public static String formatMessage(String message, Object[] params) {
        if ((message == null) || (params == null) || (params.length == 0)) {
            return message;
        }

        return MessageFormat.format(message, params);
    }

    public static String formatMessage(String message, Object param1) {
        return formatMessage(message, new Object[] { param1 });
    }

    public static String formatMessage(String message, Object param1, Object param2) {
        return formatMessage(message, new Object[] { param1, param2 });
    }

    public static String formatMessage(String message, Object param1, Object param2, Object param3) {
        return formatMessage(message, new Object[] { param1, param2, param3 });
    }

    public static String formatMessage(String message, Object param1, Object param2, Object param3,
        Object param4) {
        return formatMessage(message, new Object[] { param1, param2, param3, param4 });
    }

    public static String formatMessage(String message, Object param1, Object param2, Object param3,
        Object param4, Object param5) {
        return formatMessage(message, new Object[] { param1, param2, param3, param4, param5 });
    }
}

