/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.huifu.saturn.common.ClassLoaderUtil;
import com.huifu.saturn.common.StringUtil;
import com.huifu.saturn.web.common.session.request.SessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;

/**
 */
public class SessionRequestContextImpl implements SessionRequestContext {
    private SessionConfig       sessionConfig;
    private boolean             requestedSessionIDParsed;
    private String              requestedSessionID;
    private boolean             requestedSessionIDFromCookie;
    private boolean             requestedSessionIDFromURL;
    private SessionImpInterface session;
    private boolean             sessionReturned;
    protected final Logger      logger = Logger.getLogger(SessionRequestContextImpl.class);
    private HttpServletRequest  httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private ServletContext      servletContext;

    /**
     */
    public SessionRequestContextImpl(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     ServletContext servletContext, SessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.servletContext = servletContext;

    }

    /**
     */
    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    /**
     */
    public boolean isSessionInvalidated() {
        return session.isInvalidated();
    }

    /**
     */
    public void clear() {
        session.clear();
    }

    /**
     * 
     * @return session ID
     */
    public String getRequestedSessionID() {
        ensureRequestedSessionID();
        return requestedSessionID;
    }

    /**
     * 
     */
    public boolean isRequestedSessionIDFromCookie() {
        ensureRequestedSessionID();
        return requestedSessionIDFromCookie;
    }

    /**
     * 
     */
    public boolean isRequestedSessionIDFromURL() {
        ensureRequestedSessionID();
        return requestedSessionIDFromURL;
    }

    /**
     * 
     */
    public boolean isRequestedSessionIDValid() {
        HttpSession session = getSession(false);

        return (session != null) && session.getId().equals(requestedSessionID);
    }

    /**
     */
    private void ensureRequestedSessionID() {
        if (!requestedSessionIDParsed) {
            if (sessionConfig.isSessionCookieEnabled()) {
                requestedSessionID = decodeSessionIDFromCookie();
                requestedSessionIDFromCookie = requestedSessionID != null;
            }

            if ((requestedSessionID == null) && sessionConfig.isSessionURLEncodingEnabled()) {
                requestedSessionID = decodeSessionIDFromURL();
                requestedSessionIDFromURL = requestedSessionID != null;
            }
        }
    }

    /**
     */
    public void encodeSessionIDIntoCookie() {
        writeCookie(session.getSessionId());
    }

    /**
     */
    public void clearSessionIDFromCookie() {
        writeCookie("");
    }

    /**
     */
    private void writeCookie(String cookieValue) {
        Cookie cookie = new Cookie(sessionConfig.getSessionCookieName(), cookieValue);
        String cookieDomain = sessionConfig.getSessionCookieDomain();

        if (StringUtil.isNotEmpty(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }

        String cookiePath = sessionConfig.getSessionCookiePath();

        if (StringUtil.isNotEmpty(cookiePath)) {
            cookie.setPath(cookiePath);
        }

        int cookieMaxAge = sessionConfig.getSessionCookieMaxAge();

        if (cookieMaxAge > 0) {
            cookie.setMaxAge(-1);
        }

        if (logger.isInfoEnabled()) {
            logger.info("Set cookie[committed=" + getResponse().isCommitted() + ", length="
                         + cookie.getValue().length() + "]: " + cookie.getName() + "="
                         + StringUtil.defaultIfNull(cookie.getValue()) + "; domain="
                         + StringUtil.defaultIfNull(cookie.getDomain()) + "; path="
                         + StringUtil.defaultIfNull(cookie.getPath()));
        }

        getResponse().addCookie(cookie);
    }

    /**
     * 
     */
    public String decodeSessionIDFromCookie() {
        Cookie[] cookies = getRequest().getCookies();

        if (cookies != null) {
            String sessionCookieName = sessionConfig.getSessionCookieName();

            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];

                if (cookie.getName().equals(sessionCookieName)) {
                    String sessionID = StringUtil.trimToNull(cookie.getValue());

                    if (sessionID != null) {
                        return sessionID;
                    }
                }
            }
        }

        return null;
    }

    /**
     * 
     */
    public String encodeSessionIDIntoURL(String url) {
        HttpSession session = getRequest().getSession(false);

        if ((session != null)
            && (session.isNew() || (isRequestedSessionIDFromURL() && !isRequestedSessionIDFromCookie()))) {
            String sessionID = session.getId();
            String keyName = getSessionConfig().getSessionURLEncodingName();
            int keyNameLength = keyName.length();
            int urlLength = url.length();
            int urlQueryIndex = url.indexOf('?');

            if (urlQueryIndex >= 0) {
                urlLength = urlQueryIndex;
            }

            boolean found = false;

            for (int keyBeginIndex = url.indexOf(';'); (keyBeginIndex >= 0)
                                                       && (keyBeginIndex < urlLength); keyBeginIndex = url
                .indexOf(';', keyBeginIndex + 1)) {
                keyBeginIndex++;

                if (((urlLength - keyBeginIndex) <= keyNameLength)
                    || !url.regionMatches(keyBeginIndex, keyName, 0, keyNameLength)
                    || (url.charAt(keyBeginIndex + keyNameLength) != '=')) {
                    continue;
                }

                int valueBeginIndex = keyBeginIndex + keyNameLength + 1;
                int valueEndIndex = url.indexOf(';', valueBeginIndex);

                if (valueEndIndex < 0) {
                    valueEndIndex = urlLength;
                }

                if (!url.regionMatches(valueBeginIndex, sessionID, 0, sessionID.length())) {
                    url = url.substring(0, valueBeginIndex) + sessionID
                          + url.substring(valueEndIndex);
                }

                found = true;
                break;
            }

            if (!found) {
                url = url.substring(0, urlLength) + ';' + keyName + '=' + sessionID
                      + url.substring(urlLength);
            }
        }

        return url;
    }

    /**
     * 
     */
    public String decodeSessionIDFromURL() {
        String uri = getRequest().getRequestURI();
        String keyName = sessionConfig.getSessionURLEncodingName();
        int uriLength = uri.length();
        int keyNameLength = keyName.length();

        for (int keyBeginIndex = uri.indexOf(';'); keyBeginIndex >= 0; keyBeginIndex = uri.indexOf(
            ';', keyBeginIndex + 1)) {
            keyBeginIndex++;

            if (((uriLength - keyBeginIndex) <= keyNameLength)
                || !uri.regionMatches(keyBeginIndex, keyName, 0, keyNameLength)
                || (uri.charAt(keyBeginIndex + keyNameLength) != '=')) {
                continue;
            }

            int valueBeginIndex = keyBeginIndex + keyNameLength + 1;
            int valueEndIndex = uri.indexOf(';', valueBeginIndex);

            if (valueEndIndex < 0) {
                valueEndIndex = uriLength;
            }

            return uri.substring(valueBeginIndex, valueEndIndex);
        }

        return null;
    }

    /**
     */
    public HttpSession getSession(boolean create) {
        boolean bIsNewSessioinId = false;
        try {

            if ((session != null) && sessionReturned) {
                if (session.isInvalidated()) {
                    if (create) {
                        session = null;
                        bIsNewSessioinId = true;
                    } else {
                        return null;
                    }
                } else {
                    return session;
                }
            }

            if (session == null) {

                ensureRequestedSessionID();

                String sessionID = null;
                if (!bIsNewSessioinId) {
                    sessionID = requestedSessionID;
                }
                boolean isNew = false;
                if (logger.isInfoEnabled()) {
                    if (sessionID != null) {
                        logger.info("create session sessionId=" + sessionID);
                    } else {
                        logger.info("create session sessionId= null");
                    }
                }

                if (sessionID == null) {
                    if (!create) {
                        return null; //
                    }

                    sessionID = sessionConfig.getSessionIDBroker().generateSessionID();
                    isNew = true;
                }

                String sessionImplClassName = sessionConfig.getSessionImplClassName();
                // session = new SessionImpl();
                try {
                    session = (SessionImpInterface) ClassLoaderUtil
                        .newInstance(sessionImplClassName);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("session impl class not found: "
                                                    + sessionImplClassName, e);
                } catch (ClassCastException e) {
                    throw new ClassCastException(
                        "session impl class must implements the SessionImpInterface interface: "
                                + sessionImplClassName);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to instantiant Session impl class: "
                                                    + sessionImplClassName, e);
                }

                if (!session.init(sessionID, this, isNew, create)) {
                    sessionReturned = false;
                    session = null;
                    return null;
                }
            }

            if (session.isSessionNew() && !create) {
                // logger.error("getSession: return session session.isSessionNew() && !create null");
                return null;
            }

            if (sessionConfig.isSessionCookieEnabled()
                && !session.getSessionId().equals(requestedSessionID)) {
                if (getResponse().isCommitted()) {
                    throw new IllegalStateException(
                        "Failed to create a new session because the responseWrapper was already committed");
                }

                encodeSessionIDIntoCookie();
            }
        } catch (Throwable e) {
            session = null;
            return httpServletRequest.getSession(create);
        }
        sessionReturned = true;
        return session;
    }

    /**
     */
    public void prepare() {
    }

    /**
     */
    public void commit() {
        try {
            if (!sessionReturned) {
                return;
            }

            if (session.isInvalidated()) {
                clearSessionIDFromCookie();
            }

            session.commit();
        } catch (Throwable e) {
            logger.error("session commit�쳣!", e);
        }
    }

    /**
     */
    public HttpServletRequest getRequest() {
        return new SessionRequestWrapper(this.httpServletRequest);
    }

    public HttpServletResponse getResponse() {
        return new SessionResponseWrapper(this.httpServletResponse);
    }

    public ServletContext getServletContext() {

        return servletContext;
    }

    /**
     */
    public HttpServletRequest getHttpServletRequest() {

        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {

        return httpServletResponse;
    }

    public class SessionRequestWrapper extends HttpServletRequestWrapper {
        /**
         * 
         * @param request
         */
        public SessionRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        /**
         * 
         * @return session ID
         */
        public String getRequestedSessionId() {
            return SessionRequestContextImpl.this.getRequestedSessionID();
        }

        /**
         */
        public boolean isRequestedSessionIdFromCookie() {
            return SessionRequestContextImpl.this.isRequestedSessionIDFromCookie();
        }

        /**
         */
        public boolean isRequestedSessionIdFromURL() {
            return SessionRequestContextImpl.this.isRequestedSessionIDFromURL();
        }

        /**
         */
        public boolean isRequestedSessionIdValid() {
            return SessionRequestContextImpl.this.isRequestedSessionIDValid();
        }

        /**
         */
        public HttpSession getSession() {
            return getSession(true);
        }

        /**
         */
        public HttpSession getSession(boolean create) {

            return SessionRequestContextImpl.this.getSession(create);
        }

        /**
         * @deprecated use isRequestedSessionIdFromURL instead
         */
        public boolean isRequestedSessionIdFromUrl() {
            return isRequestedSessionIdFromURL();
        }

    }

    /**
     */
    public class SessionResponseWrapper extends HttpServletResponseWrapper {
        /**
         */
        public SessionResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        /**
         */
        public String encodeURL(String url) {
            if (sessionConfig.isSessionURLEncodingEnabled()) {
                url = SessionRequestContextImpl.this.encodeSessionIDIntoURL(url);
            }

            return url;
        }

        /**
         */
        public String encodeRedirectURL(String url) {
            if (sessionConfig.isSessionURLEncodingEnabled()) {
                url = SessionRequestContextImpl.this.encodeSessionIDIntoURL(url);
            }

            return url;
        }

        /**
         * @deprecated use encodeURL instead
         */
        public String encodeUrl(String url) {
            return encodeURL(url);
        }

        /**
         * @deprecated use encodeRedirectURL instead
         */
        public String encodeRedirectUrl(String url) {
            return encodeRedirectURL(url);
        }
    }

}
