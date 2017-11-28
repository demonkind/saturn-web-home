/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.huifu.saturn.common.log.Logger;
import com.huifu.saturn.common.log.LoggerFactory;
import com.huifu.saturn.web.common.session.request.SessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;

/**
 */
public class DefaultSessionImpl implements SessionImpInterface {

    private HttpSession           tomcatSession;
    private SessionImpl           tdbmSession;

    protected final Logger        log = LoggerFactory.getLogger(getClass());

    private SessionConfig         sessionConfig;
    private SessionRequestContext requestContext;

    /**
     */
    public DefaultSessionImpl() {
    }

    public void clear() {
        tdbmSession.clear();
    }

    public void commit() {
        tdbmSession.commit();
    }

    public String getSessionId() {
        return tdbmSession.getSessionId();
    }

    public boolean isInvalidated() {
        return tdbmSession.isInvalidated();
    }

    public boolean isSessionNew() {
        return tdbmSession.isSessionNew();
    }

    public boolean init(String sessionID, SessionRequestContext requestContext, boolean isNew,
                        boolean create) {
        try {
            tdbmSession = new SessionImpl();
            tdbmSession.init(sessionID, requestContext, isNew, create);
        } catch (Throwable e) {
            log.error("", e);
        }
        HttpServletRequest httpServletRequest = requestContext.getHttpServletRequest();
        tomcatSession = httpServletRequest.getSession(true);
        if (tomcatSession == null) {
            return false;
        }
        sessionConfig = requestContext.getSessionConfig();
        this.requestContext = requestContext;
        return true;
    }

    private HttpSession getTomcatSession() {
        if (tomcatSession == null) {
            log.error("tomcatSession is null  at current request ");
            throw new java.lang.IllegalArgumentException(
                "tomcatSession is null  at current request ");
        }
        return tomcatSession;

    }

    /**
     * 
     * @return request context
     */
    public SessionRequestContext getSessionRequestContext() {
        return requestContext;
    }

    public Object getAttribute(String name) {
        if (isAccessFromLocal(name)) {
            tomcatSession = getTomcatSession();
            return tomcatSession.getAttribute(name);
        }
        if (isAccessFromRemoteServer(name)) {
            name = getRealRemoteSessionName(name);
            return tdbmSession.getAttribute(name);
        }
        return null;

    }

    public Enumeration getAttributeNames() {
        return tomcatSession.getAttributeNames();
    }

    public long getCreationTime() {
        return tomcatSession.getCreationTime();
    }

    public String getId() {
        return tdbmSession.getId();
    }

    public long getLastAccessedTime() {
        return tomcatSession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return tomcatSession.getMaxInactiveInterval();
    }

    public ServletContext getServletContext() {
        return tomcatSession.getServletContext();
    }

    public HttpSessionContext getSessionContext() {
        return tomcatSession.getSessionContext();
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public String[] getValueNames() {
        return tomcatSession.getValueNames();
    }

    public void invalidate() {
        try {
            tdbmSession.invalidate();
        } catch (Throwable e) {
            log.error("", e);
        }
        tomcatSession.invalidate();
    }

    public boolean isNew() {
        return tomcatSession.isNew();
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        if (isAccessFromLocal(name)) {
            log.info("session acess rule is local");
            try {
                tomcatSession = getTomcatSession();
                tomcatSession.setAttribute(name, value);
            } catch (Throwable e) {
                log.error("", e);
            }
        }
        if (isAccessFromRemoteServer(name)) {
            log.info("session acess rule is remote");
            name = getRealRemoteSessionName(name);
            tdbmSession.setAttribute(name, value);
        }

    }

    public void removeAttribute(String name) {
        if (isAccessFromLocal(name)) {
            log.info("session acess rule is local");
            tomcatSession = getTomcatSession();
            tomcatSession.removeAttribute(name);
        }
        if (isAccessFromRemoteServer(name)) {
            try {
                log.info("session acess rule is remote");
                name = getRealRemoteSessionName(name);
                tdbmSession.removeAttribute(name);
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        try {
            tdbmSession.setMaxInactiveInterval(maxInactiveInterval);
        } catch (Throwable e) {
            log.error("", e);
        }

        tomcatSession.setMaxInactiveInterval(maxInactiveInterval);

    }

    /**
    * @param perfix
    * @see com.alipay.sofa.runtime.web.smvc.session.request.internal.role.SessionAccessRule#isAccessFromRemoteServer(java.lang.String)
    */
    public boolean isAccessFromRemoteServer(String name) {
        name = name.toLowerCase();
        if (isLocalNamesOfSession(name)) {
            log.info(name + "is sofa mvc session name ,session access rule is local");
            return false;
        }

        if (name.startsWith(sessionConfig.PREFIX_ACCESS_ROLE_REMOTE)) {
            return true;
        }
        if (name.startsWith(sessionConfig.PREFIX_ACCESS_ROLE_LOCAL)) {
            return false;
        }
        if (sessionConfig.getSessionAccessRule().equals(sessionConfig.SESSION_ACCESS_ROLE_REMOTE)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param name
     * @return
     */
    public boolean isAccessFromLocal(String name) {
        name = name.toLowerCase();
        if (isLocalNamesOfSession(name)) {
            log.info(name + "is sofa mvc session name ,session access rule is local");
            return true;
        }
        if (name.startsWith(sessionConfig.PREFIX_ACCESS_ROLE_LOCAL)) {
            return true;
        }

        if (name.startsWith(sessionConfig.PREFIX_ACCESS_ROLE_REMOTE)) {
            return false;
        }
        if (sessionConfig.getSessionAccessRule().equals(sessionConfig.SESSION_ACCESS_ROLE_LOCAL)) {
            return true;
        }
        return false;
    }

    /**
    * @param name
    * @return
    */
    private boolean isLocalNamesOfSession(String name) {
        String[] localNames = sessionConfig.getSessionLocalNames();
        if (localNames == null || localNames.length == 0) {
            return false;
        }
        int l = localNames.length;
        for (int i = 0; i < l; i++) {
            if (name.equals(localNames[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param name
     * @return
     */
    private String getRealRemoteSessionName(String name) {
        String tempName = name.toLowerCase();
        if (tempName.startsWith(sessionConfig.PREFIX_ACCESS_ROLE_REMOTE)) {
            return name.substring(sessionConfig.PREFIX_ACCESS_ROLE_REMOTE.length(), name.length());
        }
        return name;

    }

}
