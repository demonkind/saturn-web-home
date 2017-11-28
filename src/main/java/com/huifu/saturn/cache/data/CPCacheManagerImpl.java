/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.data;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huifu.cache.client.CPCacheClient;
import com.huifu.cache.client.impl.CPCacheClientImpl;
import com.huifu.cache.client.impl.CPMasterSlaveCacheClientImpl;

public class CPCacheManagerImpl implements CPCacheManager {

    private final Log log = LogFactory.getLog(CPCacheManagerImpl.class);
    CPCacheClient     c   = null;

    public CPCacheManagerImpl(String addr, String slaveSessionAddr, Integer sessionExpiredTime) {
        c = new CPMasterSlaveCacheClientImpl(addr, slaveSessionAddr, sessionExpiredTime);
    }

    public Map getObjectException(String sessionID, int maxInactiveInterval) {

        return (Map) c.getObject(sessionID);
    }

    public void removeObject(String sessionID) {
        c.removeObject(sessionID);

    }

    public CPCacheReturn putObjectExpire(String sessionID, Map sessionData, int maxInactiveInterval) {
        log.info("you win!!!putObjectExpire");
        CPCacheReturn res = new CPCacheReturn();
        res.setSucceed(true);
        c.putObject(sessionID, sessionData, maxInactiveInterval);
        return res;
    }

}
