/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;

import java.io.Serializable;

import com.huifu.saturn.web.common.session.request.SessionConfig;

/**
 *
 */
public class SessionModel implements Serializable {
    private static final long       serialVersionUID = 9158363263146288193L;
    private transient SessionConfig sessionConfig;
    private String                  sessionID;
    private long                    creationTime;
    private long                    lastAccessedTime;
    private int                     maxInactiveInterval;

    public SessionModel() {

    }

    public SessionModel(SessionImpl session) {
        setSession(session);
        reset();
    }

    public void reset() {
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        if (sessionConfig != null) {
            this.maxInactiveInterval = sessionConfig.getSessionMaxInactiveInterval();
        }
    }

    public void clean() {
        sessionConfig = null;
    }

    /**
     */
    public void setSession(SessionImpl session) {
        this.sessionConfig = session.getSessionRequestContext().getSessionConfig();
    }

    /**
     *
     * @return session ID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     *
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     *
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     *
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    /**
     *
     */
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    /**
     *
     */
    public boolean isExpired() {
        int interval = getMaxInactiveInterval();

        if (interval <= 0) {
            return false;
        } else {
            long current = System.currentTimeMillis();
            long expires = getLastAccessedTime() + (interval * 1000);

            return expires < current;
        }
    }

    /**
     */
    public void touch() {
        lastAccessedTime = System.currentTimeMillis();
    }
}
