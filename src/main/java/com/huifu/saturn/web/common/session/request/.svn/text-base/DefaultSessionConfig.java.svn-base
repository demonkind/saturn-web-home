/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.Configuration;
import org.apache.oro.text.regex.Pattern;

import com.huifu.saturn.common.ArrayHashMap;
import com.huifu.saturn.common.ArrayUtil;
import com.huifu.saturn.common.ClassLoaderUtil;
import com.huifu.saturn.common.SoftHashMap;
import com.huifu.saturn.common.StringUtil;
import com.huifu.saturn.common.log.Logger;
import com.huifu.saturn.common.log.LoggerFactory;
import com.huifu.saturn.web.common.session.cpcache.SessionStore;
import com.huifu.saturn.web.common.session.idbroker.RandomSessionIDBroker;
import com.huifu.saturn.web.common.session.idbroker.SessionIDBroker;
import com.huifu.saturn.web.common.session.request.internal.SessionRequestContextImpl;

/**
 * @author su.zhang
 */
public class DefaultSessionConfig implements SessionConfig {
    protected static final Logger       log               = LoggerFactory
                                                              .getLogger(DefaultSessionConfig.class);
    private boolean                     sessionCookieEnabled;
    private String                      sessionCookieName;
    private String                      sessionCookieDomain;
    private String                      sessionCookiePath;
    private int                         sessionCookieMaxAge;

    private String                      sessionAccessRule;

    private boolean                     sessionURLEncodeEnabled;
    private String                      sessionURLEncodeName;

    private int                         sessionMaxInactiveInterval;
    private String                      sessionModelName;
    private boolean                     sessionTouchAlways;
    private String                      sessionImplClassName;
    // Session ID Broker
    private SessionIDBroker             idBroker;

    // Session Stores
    private Map                         sessionStores;
    private Map                         activeSessionStores;
    private String                      sessionStoreDefault;
    private List                        sessionAttributeMatchPatterns;
    private Map                         sessionAttributeMatchCache;
    private String[]                    sessionOnlyLocalNames;
    private static DefaultSessionConfig sessionConfig;
    private String                      addr              = null;
    private String                      slaveCacheAddress = null;

    private DefaultSessionConfig() {

    }

    public static DefaultSessionConfig getDefaultSessionConfig() {

        if (sessionConfig == null) {
            sessionConfig = new DefaultSessionConfig();
        }
        return sessionConfig;
    }

    public void initSessionConfiguration(String cookieDomainName, String cpcacheAddr,
                                         String slaveCacheAddress, String sessionCookiePath,
                                         Integer exp) throws Exception {
        //		InputStream in = ClassLoaderUtil
        //				.getResourceAsStream(SATURN_CONFIG_NAME);
        //		Properties p = new Properties();
        //		p.load(in);
        //		String domainName = p.getProperty("cookie_domain");
        //		if (domainName != null) {
        //			sessionCookieDomain = cookieDomainName;
        //		}
        sessionCookieDomain = cookieDomainName;
        //		String tmpaddr = p.getProperty("cpcache_addr");
        //		if (tmpaddr != null) {
        //			addr = tmpaddr;
        //		}
        this.addr = cpcacheAddr;
        this.slaveCacheAddress = slaveCacheAddress;
        this.sessionCookiePath = "/";
        if (StringUtil.isNotEmpty(sessionCookiePath)) {
            this.sessionCookiePath = sessionCookiePath;
        }
        this.sessionCookieMaxAge = SessionConfig.SESSION_COOKIE_MAX_AGE_DEFAULT;
        if (exp != null) {
            this.sessionCookieMaxAge = exp;
        }
        initSessionCookie(null);
        initSessionURLEncoding(null);
        initSessionMisc(null);
        initSessionIDBroker(null);
        initSessionStores(null);
        initSessionOthers(null);
    }

    /**
     * 
     */
    private void initSessionCookie(Configuration configuration) throws Exception {
        sessionCookieEnabled = true;
        sessionCookieName = "CHINAPNRJSESSIONID";
        if (log.isInfoEnabled()) {
            log.info("Session cookie enabled: " + sessionCookieEnabled);

            if (sessionCookieEnabled) {
                log.info("Session cookie name: " + sessionCookieName);
                log.info("Session cookie domain: " + sessionCookieDomain);
                log.info("Session cookie path: " + sessionCookiePath);
                log.info("Session cookie max age: " + sessionCookieMaxAge + " seconds");
            }
        }
    }

    /**
     * 
     */
    private void initSessionOthers(Configuration configuration) throws Exception {
        String localNameStrs = "_forms_in_session_token";
        if (localNameStrs == null || "".equals(localNameStrs)) {
            sessionOnlyLocalNames = new String[] {};
            return;
        }
        sessionOnlyLocalNames = localNameStrs.split(",");
        if (log.isInfoEnabled()) {
            log.info("Session key only local: " + localNameStrs);
        }
    }

    /**
     * 
     * @throws Exception
     */
    private void initSessionURLEncoding(Configuration configuration) throws Exception {
        sessionURLEncodeEnabled = false;
        sessionURLEncodeName = "CHINAPNRJSESSIONID";

        if (log.isInfoEnabled()) {
            log.info("Session URL encoding enabled: " + sessionURLEncodeEnabled);

            if (sessionURLEncodeEnabled) {
                log.info("Session URL encoding name: " + sessionURLEncodeName);
            }
        }
    }

    /**
     * 
     * @throws Exception
     */
    private void initSessionMisc(Configuration configuration) throws Exception {
        sessionMaxInactiveInterval = SESSION_MAX_INACTIVE_INTERVAL_DEFAULT;

        sessionModelName = SESSION_MODEL_NAME_DEFAULT;

        sessionTouchAlways = false;

        sessionImplClassName = "com.huifu.saturn.web.common.session.request.internal.DefaultSessionImpl";
        ;

        sessionAccessRule = SESSION_ACCESS_ROLE_REMOTE;

        if (log.isInfoEnabled()) {
            log.info("Session max inactive interval: " + sessionMaxInactiveInterval + " secondes");
            log.info("Session model name: " + sessionModelName);
            log.info("Session touch always: " + sessionTouchAlways);
            log.info("Session impl class name: " + sessionImplClassName);
            log.info("Session default access rule: " + sessionAccessRule);

        }
    }

    /**
     * 
     * @throws Exception
     */
    private void initSessionIDBroker(Configuration configuration) throws Exception {

        String idBrokerClass = RandomSessionIDBroker.class.getName();

        try {
            idBroker = (SessionIDBroker) ClassLoaderUtil.newInstance(idBrokerClass);
        } catch (ClassCastException e) {
            throw new Exception("IDBroker class must implements the SessionIDBroker interface: "
                                + idBrokerClass, e);
        } catch (Exception e) {
            throw new Exception("Failed to instantiant IDBroker class: " + idBrokerClass, e);
        }

        idBroker.init(configuration);
    }

    /**
     * 
     * @throws Exception
     */
    private void initSessionStores(Configuration configuration) throws Exception {
        sessionStores = new ArrayHashMap();
        activeSessionStores = new ArrayHashMap();
        sessionAttributeMatchPatterns = new ArrayList();
        sessionAttributeMatchCache = Collections.synchronizedMap(new SoftHashMap());

        String classSuffix = "." + SESSION_STORE_CLASS;
        String storeName = "com.huifu.saturn.web.common.session.cpcache.CPCacheStore";
        String storeClass = storeName;
        initSessionStore(storeName, storeClass);
    }

    /**
     * 
     * @throws Exception
     */
    private void initSessionStore(String storeName, String storeClass) throws Exception {

        if (storeClass == null) {
            throw new Exception("No class name was specified for session store: " + storeName);
        }

        String[] patterns = new String[] { "*" };
        String[] patternRegexs = null;

        if (patterns == null) {
            patterns = ArrayUtil.EMPTY_STRING_ARRAY;
        }

        if (patternRegexs == null) {
            patternRegexs = ArrayUtil.EMPTY_STRING_ARRAY;
        }

        SessionStore store;

        try {
            store = (SessionStore) ClassLoaderUtil.newInstance(storeClass);
        } catch (ClassNotFoundException e) {
            throw new Exception("Session store class not found: " + storeClass, e);
        } catch (ClassCastException e) {
            throw new Exception(e);
        }

        store.init(storeName, null, this, addr, slaveCacheAddress, sessionCookieMaxAge);

        sessionStores.put(storeName, store);
        activeSessionStores.put(storeName, store);

    }

    /**
     */
    public boolean isSessionCookieEnabled() {
        return sessionCookieEnabled;
    }

    /**
     */
    public String getSessionCookieName() {
        return sessionCookieName;
    }

    /**
     */
    public String getSessionCookieDomain() {
        return sessionCookieDomain;
    }

    /**
     */
    public String getSessionCookiePath() {
        return sessionCookiePath;
    }

    /**
     */
    public int getSessionCookieMaxAge() {
        return sessionCookieMaxAge;
    }

    /**
     */
    public boolean isSessionURLEncodingEnabled() {
        return sessionURLEncodeEnabled;
    }

    /**
     */
    public String getSessionURLEncodingName() {
        return sessionURLEncodeName;
    }

    /**
     */
    public int getSessionMaxInactiveInterval() {
        return sessionMaxInactiveInterval;
    }

    /**
     * 
     * @return session model name
     */
    public String getSessionModelName() {
        return sessionModelName;
    }

    /**
     * 
     */
    public String getSessionImplClassName() {
        return sessionImplClassName;
    }

    /**
     * 
     */
    public boolean isSessionTouchAlways() {
        return sessionTouchAlways;
    }

    /**
     */
    public SessionIDBroker getSessionIDBroker() {
        return idBroker;
    }

    /**
     * 
     */
    public String[] getSessionStoreNames() {
        Set storeNames = activeSessionStores.keySet();

        return (String[]) storeNames.toArray(new String[storeNames.size()]);
    }

    /**
     * 
     * @return
     */
    public String[] getSessionLocalNames() {
        return sessionOnlyLocalNames;
    }

    /**
     * 
     * @param storeName
     * 
     */
    public String getSessionStoreName(String attrName) {
        return "com.huifu.saturn.web.common.session.cpcache.CPCacheStore";
        /*
         * if (attrName == null) { throw new IllegalArgumentException(
         * "Session attribute cannot be null"); }
         * 
         * String matchedStoreName = null;
         * 
         * if (sessionAttributeMatchCache.containsKey(attrName)) {
         * matchedStoreName = (String) sessionAttributeMatchCache
         * .get(attrName); } else { List matches = new
         * ArrayList(sessionAttributeMatchPatterns.size());
         * 
         * for (Iterator i = sessionAttributeMatchPatterns.iterator(); i
         * .hasNext();) { AttributeMatchPattern pattern =
         * (AttributeMatchPattern) i .next();
         * 
         * if (pattern.patternName == null) { matches.add(new
         * AttributeMatchPattern(pattern, 0)); } else if (pattern.pattern ==
         * null) { if (pattern.patternName.equals(attrName)) { matches.add(new
         * AttributeMatchPattern(pattern, pattern.patternName.length())); } }
         * else { PatternMatcher matcher = new Perl5Matcher();
         * 
         * if (matcher.contains(attrName, pattern.pattern)) { matches.add(new
         * AttributeMatchPattern(pattern, matcher .getMatch().length())); } } }
         * 
         * Collections.sort(matches);
         * 
         * if (log.isDebugEnabled() && (matches.size() > 1)) { StringBuffer
         * buffer = new StringBuffer();
         * 
         * buffer.append("Attribute \"").append(attrName).append("\" ");
         * 
         * if (matches.isEmpty()) { buffer.append("does not match any pattern");
         * } else {
         * buffer.append("matches the following CANDIDATED patterns:\n");
         * 
         * for (Iterator i = matches.iterator(); i.hasNext();) {
         * AttributeMatchPattern pattern = (AttributeMatchPattern) i .next();
         * 
         * buffer.append("    ").append(pattern);
         * 
         * if (i.hasNext()) { buffer.append("\n"); } } }
         * 
         * log.debug(buffer.toString()); }
         * 
         * if (!matches.isEmpty()) { matchedStoreName = ((AttributeMatchPattern)
         * matches.get(0)).storeName; }
         * 
         * sessionAttributeMatchCache.put(attrName, matchedStoreName); }
         * 
         * if (log.isDebugEnabled()) { log.debug("Session attribute " + attrName
         * + " is handled by session store: " + matchedStoreName); }
         * 
         * return matchedStoreName;
         */}

    public String getSessionAccessRule() {
        return sessionAccessRule;
    }

    /**
     * 
     * 
     */
    public SessionStore getSessionStore(String storeName) {
        return (SessionStore) sessionStores.get(storeName);
    }

    /**
     */
    public SessionRequestContextImpl getRequestContextWrapper(HttpServletRequest httpServletRequest,
                                                              HttpServletResponse httpServletResponse,
                                                              ServletContext servletContext) {
        return new SessionRequestContextImpl(httpServletRequest, httpServletResponse,
            servletContext, this);
    }

    /**
     */
    private class AttributeMatchPattern implements Comparable {
        public final String  storeName;
        public final String  patternName;
        public final Pattern pattern;
        public final int     matchLength;

        public AttributeMatchPattern(String storeName, String patternName, Pattern pattern) {
            this.storeName = storeName;
            this.patternName = patternName;
            this.pattern = pattern;
            this.matchLength = -1;
        }

        public AttributeMatchPattern(AttributeMatchPattern pattern, int matchLength) {
            this.storeName = pattern.storeName;
            this.patternName = pattern.patternName;
            this.pattern = pattern.pattern;
            this.matchLength = matchLength;
        }

        public int compareTo(Object o) {
            return ((AttributeMatchPattern) o).matchLength - matchLength;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();

            buffer.append("AttributeMatchPattern[store=").append(storeName);
            buffer.append(", ").append("pattern=").append(patternName);

            if (matchLength >= 0) {
                buffer.append(", ").append("matchLength=").append(matchLength);
            }

            buffer.append("]");

            return buffer.toString();
        }
    }

}
