/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request;


import com.huifu.saturn.web.common.session.cpcache.SessionStore;
import com.huifu.saturn.web.common.session.idbroker.RandomSessionIDBroker;
import com.huifu.saturn.web.common.session.idbroker.SessionIDBroker;

/**
 */
public interface SessionConfig {
    String  SESSION_configFile          = "session.properties.file";
  
    String  SESSION_COOKIE_ENABLED         = "session.cookie.enabled";
    boolean SESSION_COOKIE_ENABLED_DEFAULT = true;
    String  SESSION_COOKIE_NAME            = "session.cookie.name";
    String  SESSION_COOKIE_NAME_DEFAULT    = "CHINAPNRJSESSIONID";
    String  SESSION_COOKIE_DOMAIN          = "session.cookie.domain";
    String  SESSION_COOKIE_DOMAIN_DEFAULT  = null;
    String  SESSION_COOKIE_PATH            = "session.cookie.path";
    String  SESSION_COOKIE_PATH_DEFAULT    = "/";
    String  SESSION_COOKIE_MAX_AGE         = "session.cookie.maxAge";
    int     SESSION_COOKIE_MAX_AGE_DEFAULT = 0;

    String  SESSION_URL_ENCODING_ENABLED         = "session.urlencode.enabled";
    boolean SESSION_URL_ENCODING_ENABLED_DEFAULT = false;
    String  SESSION_URL_ENCODING_NAME            = "session.urlencode.name";
    String  SESSION_URL_ENCODING_NAME_DEFAULT    = "CHINAPNRJSESSIONID";

    String SESSION_MAX_INACTIVE_INTERVAL         = "session.maxInactiveInterval";
    int    SESSION_MAX_INACTIVE_INTERVAL_DEFAULT = 0;
    String SESSION_MODEL_NAME                    = "session.model.name";
    String SESSION_MODEL_NAME_DEFAULT            = "SESSION_MODEL";
    String SESSION_IMPL_CLASS_NAME               = "session.implClassName";
    String SESSION_IMPL_CLASS_NAME_DEFAULT                     = "com.alipay.common.webx.session.request.internal.DefaultSessionImpl";
    String  SESSION_TOUCH_ALWAYS         = "session.touch.always";
    boolean SESSION_TOUCH_ALWAYS_DEFAULT = false;

    String SESSION_ID_BROKER_PREFIX        = "session.idbroker";
    String SESSION_ID_BROKER_CLASS         = "class";
    String SESSION_ID_BROKER_CLASS_DEFAULT = RandomSessionIDBroker.class.getName();
    String SESSION_ID_LENGTH               = "length";

    String SESSION_STORE_PREFIX      = "session.store";
    String SESSION_STORE_CLASS       = "class";
    String SESSION_STORE_MATCH       = "match";
    String SESSION_STORE_MATCH_REGEX = "matchRegex";
    String SESSION_STORE_MATCH_ALL   = "*";
    
    String SESSION_ACCESS_ROLE = "session.accessRule";
    String SESSION_ACCESS_ROLE_LOCAL = "local";
    String SESSION_ACCESS_ROLE_REMOTE = "remote";
    String SESSION_ACCESS_ROLE_BETWEEN="between";
    
    public static final String PREFIX_ACCESS_ROLE_LOCAL="local_";
    public static final String PREFIX_ACCESS_ROLE_REMOTE="remote_";
    
    String SESSION_KEY_ONLY_LOCAL_Names="session.onlyLocalNames";
    

    /**
     */
    boolean isSessionCookieEnabled();

    /**
     */
    String getSessionCookieName();

    /**
     */
    String getSessionCookieDomain();

    /**
     */
    String getSessionCookiePath();

    /**
     */
    int getSessionCookieMaxAge();

    /**
     */
    boolean isSessionURLEncodingEnabled();

    /**
     */
    String getSessionURLEncodingName();

    /**
     */
    int getSessionMaxInactiveInterval();

    /**
     *
     * @return session model name
     */
    String getSessionModelName();

    /**
     */
    boolean isSessionTouchAlways();

    /**
     *
     */
    SessionIDBroker getSessionIDBroker();

    /**
     *
     */
    String[] getSessionStoreNames();

    /**
     */
    String getSessionStoreName(String attrName);

    /**
     */
    SessionStore getSessionStore(String storeName);
    
    /**
     */
    String getSessionImplClassName();
    
    /**
     * @return
     */
    String getSessionAccessRule();
    
    /**
     * @return
     */
    String[] getSessionLocalNames();
    
}

