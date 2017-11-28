/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.cpcache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.huifu.saturn.cache.data.CPCacheManager;
import com.huifu.saturn.cache.data.CPCacheManagerImpl;
import com.huifu.saturn.cache.data.CPCacheReturn;
import com.huifu.saturn.cache.data.HessianConvertor;
import com.huifu.saturn.common.Collections;
import com.huifu.saturn.web.common.session.exception.ServiceInitializationException;
import com.huifu.saturn.web.common.session.request.SessionConfig;
import com.thoughtworks.xstream.XStream;

public class CPCacheStore implements SessionStore {
    private Map                     sessions                 = Collections
                                                                 .synchronizedMap(new HashMap());

    private Map                     unSerializableMap        = Collections
                                                                 .synchronizedMap(new HashMap());
    private Map<String, Boolean>    sessionsIsGet            = Collections
                                                                 .synchronizedMap(new HashMap());

    protected static CPCacheManager cpCacheManager           = null;

    /**  
     */
    protected int                   tdbmServerConnectTimeout = 2000;

    protected final static Logger   log                      = Logger.getLogger(SessionStore.class);

    protected int                   maxInactiveInterval      = 600;

    /**
     *
     */
    public void init(String storeName, Configuration configuration, SessionConfig sessionConfig,
                     String addr, String slaveCacheAddr, Integer sessionExpiredTime)
                                                                                    throws ServiceInitializationException {

        /**
         */
        if (cpCacheManager != null) {
            return;
        }
        this.maxInactiveInterval = sessionExpiredTime;
        cpCacheManager = new CPCacheManagerImpl(addr, slaveCacheAddr, sessionExpiredTime);

    }

    protected Map getSession(String sessionID) {
        Map sessionData = (Map) sessions.get(sessionID);
        if (sessionData == null) {
            Boolean isRemoteGet = sessionsIsGet.get(sessionID);
            if (isRemoteGet == null || !isRemoteGet) {
                try {
                    sessionData = (Map) cpCacheManager.getObjectException(sessionID,
                        maxInactiveInterval);
                    if (sessionData != null) {
                        sessionData = Collections.synchronizedMap(sessionData);
                    }
                    if (log.isDebugEnabled()) {
                        if (sessionData != null) {
                            XStream xstream = new XStream();
                            String s = xstream.toXML(sessionData);
                            log.debug("getsession ." + s);
                        } else {
                            log.debug("session is null!id=" + sessionID);
                        }
                    }
                } catch (Exception e) {
                    log.error("tdbm getSession error!", e);
                }
                if (sessionData != null) {
                    sessions.put(sessionID, sessionData);
                }
                sessionsIsGet.put(sessionID, true);
            }
        }
        return sessionData;
    }

    public Iterator getAttributeNames(String sessionID, StatusHolder statusHolder) {
        Map sessionData = getSession(sessionID);

        if (sessionData == null) {
            return EmptyIterator.INSTANCE;
        } else {
            return sessionData.keySet().iterator();
        }
    }

    public Object loadAttribute(String attrName, String sessionID, StatusHolder statusHolder) {
        Map sessionData = (Map) unSerializableMap.get(sessionID);
        if (sessionData == null) {
            sessionData = Collections.synchronizedMap(new HashMap());
            unSerializableMap.put(sessionID, sessionData);
        }
        Object cacheObj = sessionData.get(attrName);
        if (cacheObj == null) {
            Map fromData = getSession(sessionID);
            if (fromData == null)
                return null;
            byte[] datas = (byte[]) fromData.get(attrName);
            if (datas == null) {
                return null;
            }

            try {
                HessianConvertor convertor = new HessianConvertor();
                cacheObj = convertor.bytes2Object(datas);
                sessionData.put(attrName, cacheObj);
            } catch (Exception e) {
                log.error("key=" + attrName + ",存放失败!SessionID:=" + sessionID + e);
            }

        }
        return cacheObj;
    }

    public void invaldiate(String sessionID, StatusHolder statusHolder) {
        remove(sessionID);
        cpCacheManager.removeObject(sessionID);

    }

    /**
     */
    private void remove(String sessionID) {
        sessionsIsGet.remove(sessionID);
        sessions.remove(sessionID);
        unSerializableMap.remove(sessionID);
    }

    /**
     */
    public void commit(Map attrs, String sessionID, StatusHolder statusHolder) {
        Map sessionData;
        CPCacheReturn cpCacheReturn = null;

        synchronized (sessions) {
            sessionData = getSession(sessionID);
            if (sessionData == null) {
                sessionData = Collections.synchronizedMap(new HashMap());
                sessions.put(sessionID, sessionData);
            }
        }

        for (Iterator i = attrs.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String attrName = (String) entry.getKey();
            Object attrValue = entry.getValue();

            if (attrValue == null) {
                sessionData.remove(attrName);
            } else {
                try {
                    HessianConvertor convertor = new HessianConvertor();
                    byte[] datas = convertor.convertData(attrValue);
                    sessionData.put(attrName, datas);
                } catch (Exception e) {
                    log.error("key=" + attrName + ",value=" + attrValue + "���л��쳣!SessionID:"
                              + sessionID + e);
                }
            }
        }
        synchronized (sessionData) {
            cpCacheReturn = cpCacheManager.putObjectExpire(sessionID, sessionData,
                maxInactiveInterval);
        }
        if (!cpCacheReturn.isSucceed()) {
            log.error("session:put��Զ��cache���?sessionid=" + sessionID);
        } else {
            if (log.isDebugEnabled()) {
                XStream xstream = new XStream();
                String s = xstream.toXML(sessionData);
                log.debug("commit session. " + s);
            }
        }
        remove(sessionID);
    }

    /**
     * @return the tbCacheManager
     */
    public static CPCacheManager getTbCacheManager() {
        return cpCacheManager;
    }

}
