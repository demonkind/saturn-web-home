/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.data;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.huifu.saturn.cache.hessian.HessianSerUtils;
import com.thoughtworks.xstream.XStream;

/**
 * @author su.zhang
 */
public class HessianConvertor extends Convertor {
    private final static Logger logger = Logger.getLogger(HessianConvertor.class);
    
    
    @Override
    Object readObject(byte[] bytes) throws CacheDataException {
        Object obj = null;
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
            Hessian2Input in = new Hessian2Input(bin);
            in.setSerializerFactory(HessianSerUtils.getSerializerFactory());
            in.startMessage();
            obj = in.readObject();
            in.completeMessage();
            in.close();
            bin.close();
        } catch (Exception e) {
            ToStringBuilder sb = new ToStringBuilder(null);  
            sb.append( bytes );
            logger.error(sb.toString(), e);
        }
        return obj;
    }


    @Override
    byte[] writeObject(Object obj) throws CacheDataException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        out.setSerializerFactory(HessianSerUtils.getSerializerFactory());
        try {
            out.startMessage();
            out.writeObject(obj);
            out.completeMessage();
            out.close();
        } catch (Exception e) {
            XStream xstream = new XStream();
            logger.error("Hessianerror!" + xstream.toXML(obj), e);
        }
        return bos.toByteArray();
    }

}

