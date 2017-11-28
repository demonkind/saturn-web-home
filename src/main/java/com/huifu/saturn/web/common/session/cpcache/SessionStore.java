/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.cpcache;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.configuration.Configuration;

import com.huifu.saturn.web.common.session.request.SessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;

/**
 * 
 * @author su.zhang
 */
public interface SessionStore {
    /**
     *
     */
    void init(String storeName, Configuration configuration, SessionConfig sessionConfig,
              String addr, String slaveCacheAddress, Integer sessionExpiredTime);

    Iterator getAttributeNames(String sessionID, StatusHolder statusHolder);

    Object loadAttribute(String attrName, String sessionID, StatusHolder statusHolder);

    void invaldiate(String sessionID, StatusHolder statusHolder);

    void commit(Map attrs, String sessionID, StatusHolder statusHolder);

    /**
     */
    interface StatusHolder {
        /**
         * 
         * @return ״ֵ̬
         */
        Object getStatus();

        /**
         *
         */
        void setStatus(Object status);

        /**
         *
         *
         */
        StatusHolder getStatusHolder(String storeName);

        /**
         *
         */
        SessionRequestContext getSessionRequestContext();

        /**
         *
         */
        HttpSession getHttpSession();
    }
}
