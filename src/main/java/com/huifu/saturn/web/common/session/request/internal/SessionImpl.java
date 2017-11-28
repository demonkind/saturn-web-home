/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.functors.AndPredicate;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.apache.commons.collections.functors.UniquePredicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.collections.iterators.IteratorEnumeration;

import com.huifu.saturn.web.common.session.cpcache.SessionStore;
import com.huifu.saturn.web.common.session.request.SessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;


/**
 */
public class SessionImpl implements  SessionImpInterface {

    private HttpSessionInternal   sessionInternal = new HttpSessionInternal();
    private String                sessionID;
    private SessionRequestContext requestContext;
    private String                modelName;
    private SessionModel          model;
    private boolean               isNew;
    private Map<String, SessionAttribute >    attrs          = Collections.synchronizedMap(new HashMap<String, SessionAttribute >());
    private Map<String, Object >    storeStatusMap = Collections.synchronizedMap(new HashMap<String, Object >());
    private boolean               invalidated    = false;
    private boolean               cleared        = false;

    /**
     */
    public boolean init(String sessionID, SessionRequestContext requestContext, boolean isNew,
                       boolean create) {
        this.sessionID      = sessionID;
        this.requestContext = requestContext;
        this.modelName      = requestContext.getSessionConfig().getSessionModelName();
        this.isNew          = isNew;

        if (isNew) {
            sessionInternal.invalidate();
        } else {
            model = (SessionModel) sessionInternal.getAttribute(modelName);

            if (model == null) {
                this.sessionID = requestContext.getSessionConfig().getSessionIDBroker().generateSessionID();
                isNew = true;
                sessionInternal.invalidate();
            } else {
                model.setSession(this);

                if (model.isExpired()) {
                    this.sessionID = requestContext.getSessionConfig().getSessionIDBroker().generateSessionID();
                    isNew = true;
                    sessionInternal.invalidate();
                } else {
                    model.touch();
                }
            }
        }
        sessionInternal.setAttribute(modelName, model);
        return true;
    }

    
    /**
     *
     * @return request context
     */
    public SessionRequestContext getSessionRequestContext() {
        return requestContext;
    }

    /**
     *
     */
    public SessionModel getSessionModel() {
        return model;
    }

    /**
     *
     * @return session ID
     */
    public String getId() {
        return sessionID;
    }
    public String getSessionId() {
        return getId();
    }

    public boolean isSessionNew() {
        return isNew();
    }

    /**
     *
     *
     */
    public long getCreationTime() {
        assertValid("getCreationTime");
        return sessionInternal.getCreationTime();
    }

    /**
     *
     */
    public long getLastAccessedTime() {
        assertModel("getLastAccessedTime");
        return model.getLastAccessedTime();
    }

    /**
     *
     */
    public int getMaxInactiveInterval() {
        assertModel("getMaxInactiveInterval");
        return model.getMaxInactiveInterval();
    }

    /**
     *
     */
    public void setMaxInactiveInterval(int maxInactiveInterval) {
        assertModel("setMaxInactiveInterval");
        model.setMaxInactiveInterval(maxInactiveInterval);
    }

    /**
     *
     */
    public ServletContext getServletContext() {
        return requestContext.getServletContext();
    }

    /**
     *
     */
    public Object getAttribute(String name) {
        assertValid("getAttribute");
        return sessionInternal.getAttribute(name);
    }

    /**
     */
    public Enumeration getAttributeNames() {
        assertValid("getAttributeNames");

        SessionConfig sessionConfig = requestContext.getSessionConfig();
        String[]      storeNames = sessionConfig.getSessionStoreNames();
        IteratorChain iterators  = new IteratorChain();

        for (int i = 0; i < storeNames.length; i++) {
            String       storeName = storeNames[i];
            SessionStore store    = sessionConfig.getSessionStore(storeName);
            Iterator     iterator = store.getAttributeNames(
                getId(),
                new StoreStatusHolder(storeName));

            if (iterator != null) {
                iterators.addIterator(iterator);
            }
        }

        Iterator filterIterator = new FilterIterator(
            iterators,
            new AndPredicate(
                new UniquePredicate(),
                new NotPredicate(new EqualPredicate(modelName))));

        return new IteratorEnumeration(filterIterator);
    }

    /**
     */
    public void setAttribute(String name, Object value) {
        assertValid("setAttribute");

        sessionInternal.setAttribute(name, value);
    }

    /**
     */
    public void removeAttribute(String name) {
        assertValid("removeAttribute");

        setAttribute(name, null);
    }

    /**
     */
    public void invalidate() {
        assertValid("invalidate");
        sessionInternal.invalidate();
        invalidated = true;
    }

    /**
     */
    public void clear() {
        assertValid("clear");
        sessionInternal.invalidate();
    }

    /**
     */
    public boolean isInvalidated() {
        return invalidated;
    }

    /**
     */
    public boolean isNew() {
        assertValid("isNew");
        return isNew;
    }

    /**
     */
    protected void assertModel(String methodName) {
        if (model == null) {
            throw new IllegalStateException(
                "Cannot call method " + methodName + ": the session has not been initialized");
        }
    }

    /**
     */
    protected void assertValid(String methodName) {
        assertModel(methodName);

        if (invalidated) {
        }
    }

    /**
     * 
     */
    public void commit() {
        String[] storeNames = requestContext.getSessionConfig().getSessionStoreNames();
        Map      stores = new HashMap();

        boolean modified = false;
        
        for (Iterator i = attrs.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String attrName = (String) entry.getKey();
            SessionAttribute attr = (SessionAttribute) entry.getValue();

            if (attr.isModified()) {
                String storeName = attr.getStoreName();
                SessionStore store = attr.getStore();
                Object[] storeInfo = (Object[]) stores.get(storeName);

                if (storeInfo == null) {
                    storeInfo = new Object[] { store, new HashMap() };
                    stores.put(storeName, storeInfo);
                }

                Map storeAttrs = (Map) storeInfo[1];
                Object attrValue = attr.getValue();

                if (attrValue instanceof SessionModel) {
                    ((SessionModel) attrValue).clean();
                } else {
                    modified = true;
                }

                storeAttrs.put(attrName, attrValue);
            }
        }
        
        if (!modified && !cleared && !requestContext.getSessionConfig().isSessionTouchAlways()) {
            return;
        }

        for (Iterator i = stores.entrySet().iterator(); i.hasNext();) {
            Map.Entry    entry      = (Map.Entry) i.next();
            String       storeName  = (String) entry.getKey();
            SessionStore store      = (SessionStore) ((Object[]) entry.getValue())[0];
            Map          storeAttrs = (Map) ((Object[]) entry.getValue())[1];
            
            store.commit(storeAttrs, getId(), new StoreStatusHolder(storeName));

        }

        if (storeNames.length > stores.size()) {
            for (int i = 0; i < storeNames.length; i++) {
                String storeName = storeNames[i];

                if (!stores.containsKey(storeName)) {
                    SessionStore store = requestContext.getSessionConfig().getSessionStore(
                        storeName);

                    store.commit(
                        Collections.EMPTY_MAP,
                        sessionID,
                        new StoreStatusHolder(storeName));
                }
            }
        }
    }

    /**
     * @deprecated no replacement
     */
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("No longer supported method: getSessionContext");
    }

    /**
     * @deprecated use getAttribute instead
     */
    public Object getValue(String name) {
        return getAttribute(name);
    }

    /**
     * @deprecated use getAttributeNames instead
     */
    public String[] getValueNames() {
        List names = new ArrayList();

        for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
            names.add(e.nextElement());
        }

        return (String[]) names.toArray(new String[names.size()]);
    }

    /**
     * @deprecated use setAttribute instead
     */
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    /**
     * @deprecated use removeAttribute instead
     */
    public void removeValue(String name) {
        removeAttribute(name);
    }

    /**
     */
    private class StoreStatusHolder implements SessionStore.StatusHolder {
        private String storeName;

        public StoreStatusHolder(String storeName) {
            this.storeName = storeName;
        }

        public Object getStatus() {
            return storeStatusMap.get(storeName);
        }

        public void setStatus(Object status) {
            if (status == null) {
                storeStatusMap.remove(storeName);
            } else {
                storeStatusMap.put(storeName, status);
            }
        }

        public SessionStore.StatusHolder getStatusHolder(String storeName) {
            return new StoreStatusHolder(storeName);
        }

        public SessionRequestContext getSessionRequestContext() {
            return SessionImpl.this.getSessionRequestContext();
        }

        public HttpSession getHttpSession() {
            return sessionInternal;
        }
    }

    /**
     */
    private class HttpSessionInternal implements HttpSession {
        public String getId() {
            return SessionImpl.this.getId();
        }

        public long getCreationTime() {
            return (model == null) ? 0
                                   : model.getCreationTime();
        }

        public long getLastAccessedTime() {
            return SessionImpl.this.getLastAccessedTime();
        }

        public int getMaxInactiveInterval() {
            return SessionImpl.this.getMaxInactiveInterval();
        }

        public void setMaxInactiveInterval(int maxInactiveInterval) {
            SessionImpl.this.setMaxInactiveInterval(maxInactiveInterval);
        }

        public ServletContext getServletContext() {
            return SessionImpl.this.getServletContext();
        }

        public Object getAttribute(String name) {
            SessionAttribute attr          = (SessionAttribute) attrs.get(name);
            SessionConfig    sessionConfig = requestContext.getSessionConfig();
            Object           value;

            if (attr == null) {
                String storeName = sessionConfig.getSessionStoreName(name);

                if (storeName == null) {
                    return null;
                } else {
                    attr = new SessionAttribute(
                        name,
                        SessionImpl.this,
                        storeName,
                        new StoreStatusHolder(storeName));
                    value = attr.getValue();

                    if (value != null) {
                        attrs.put(name, attr);
                    }
                }
            } else {
                value = attr.getValue();
            }

            return value;
        }

        public Enumeration getAttributeNames() {
            return SessionImpl.this.getAttributeNames();
        }

        public void setAttribute(String name, Object value) {
            SessionAttribute attr          = (SessionAttribute) attrs.get(name);
            SessionConfig    sessionConfig = requestContext.getSessionConfig();

            if (attr == null) {
                String storeName = sessionConfig.getSessionStoreName(name);

                if (storeName == null) {
                    throw new IllegalArgumentException(
                        "No storage configured for session attribute: " + name);
                } else {
                    attr = new SessionAttribute(
                        name,
                        SessionImpl.this,
                        storeName,
                        new StoreStatusHolder(storeName));
                    attrs.put(name, attr);
                }
            }

            attr.setValue(value);
        }

        public void removeAttribute(String name) {
            SessionImpl.this.removeAttribute(name);
        }

        public void invalidate() {
            attrs.clear();
            cleared = true;

            SessionConfig sessionConfig = requestContext.getSessionConfig();
            String[]      storeNames = sessionConfig.getSessionStoreNames();

            for (int i = 0; i < storeNames.length; i++) {
                String       storeName = storeNames[i];
                SessionStore store = sessionConfig.getSessionStore(storeName);

                store.invaldiate(sessionID, new StoreStatusHolder(storeName));
            }

            if (model == null) {
                model = new SessionModel(SessionImpl.this);
            } else {
                model.reset();
            }
        }

        public boolean isNew() {
            return SessionImpl.this.isNew();
        }

        /**
         * @deprecated
         */
        public javax.servlet.http.HttpSessionContext getSessionContext() {
            return SessionImpl.this.getSessionContext();
        }

        /**
         * @deprecated
         */
        public Object getValue(String name) {
            return SessionImpl.this.getValue(name);
        }

        /**
         * @deprecated
         */
        public String[] getValueNames() {
            return SessionImpl.this.getValueNames();
        }

        /**
         * @deprecated
         */
        public void putValue(String name, Object value) {
            SessionImpl.this.putValue(name, value);
        }

        /**
         * @deprecated
         */
        public void removeValue(String name) {
            SessionImpl.this.removeValue(name);
        }
    }
}

