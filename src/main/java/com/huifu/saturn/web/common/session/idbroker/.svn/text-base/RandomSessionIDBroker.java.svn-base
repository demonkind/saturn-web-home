/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.idbroker;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;

import com.huifu.saturn.web.common.session.exception.ServiceInitializationException;

/**
 *
 */
public class RandomSessionIDBroker implements SessionIDBroker {
    private int    length;
    private Random rnd;

    /**
     *
     */
    public void init(Configuration configuration) throws ServiceInitializationException {
        length = SESSION_ID_LENGTH_DEFAULT;

        try {
            rnd = new SecureRandom();
        } catch (Throwable e) {
            rnd = new Random();
        }
    }

    public String generateSessionID() {
        byte[] bytes = new byte[((length + 3) / 4) * 3];

        rnd.nextBytes(bytes);

        return (new String(Base64.encodeBase64(bytes), 0, length)).replace('/', '$').replace("+",
            "$");
    }
}
