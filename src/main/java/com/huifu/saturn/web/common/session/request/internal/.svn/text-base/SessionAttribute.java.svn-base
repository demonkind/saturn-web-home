/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;

import com.huifu.saturn.web.common.session.cpcache.SessionStore;


/**
 *
 */
public class SessionAttribute {
    private String                    storeName;
    private SessionStore              store;
    private SessionStore.StatusHolder statusHolder;
    private SessionImpl               session;
    private String                    name;
    private Object                    value;
    private boolean                   loaded;
    private boolean                   modified;

    /**
     */
    public SessionAttribute(String name, SessionImpl session, String storeName,
                            SessionStore.StatusHolder statusHolder) {
        this.name         = name;
        this.session      = session;
        this.storeName    = storeName;
        this.store        = session.getSessionRequestContext().getSessionConfig().getSessionStore(storeName);
        this.statusHolder = statusHolder;
    }

    /**
     *
     */
    public String getName() {
        return name;
    }

    /**
     *
     */
    public Object getValue() {
        if (!loaded && !modified) {
            value  = store.loadAttribute(getName(), session.getId(), statusHolder);
            loaded = true;
        }
        this.modified = true;
        return value;
    }

    /**
     * 
     * <p>
     * </p>
     *
     */
    public void setValue(Object value) {
        this.value    = value;
        this.modified = true;
    }

    /**
     *
     */
    public boolean isModified() {
        return modified;
    }

    /**
     *
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     *
     */
    public SessionStore getStore() {
        return store;
    }
}

