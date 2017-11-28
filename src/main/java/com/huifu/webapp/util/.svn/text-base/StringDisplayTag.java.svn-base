/**
 * 
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.util;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 
 * @author zhanghaijie
 * @version $Id: StringDisplayTag.java, v 0.1 2012-10-19 上午10:29:29 zhanghaijie Exp $
 */
public class StringDisplayTag implements TemplateMethodModel {

    /** 
     * @see freemarker.template.TemplateMethodModel#exec(java.util.List)
     */
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments == null || arguments.isEmpty()) {
            throw new RuntimeException("缺少标签值");
        }
        String text = (String) arguments.get(0);
        Integer beginIndex = Integer.valueOf((String) arguments.get(1));
        Integer endIndex = Integer.valueOf((String) arguments.get(2));
        char[] cts = text.toCharArray();
        StringBuffer encrptString = new StringBuffer();
        for (int i = 0; i < cts.length; i++) {
            if (i >= beginIndex && i <= endIndex) {
                encrptString.append("*");
                continue;
            }
            encrptString.append(cts[i]);
        }
        return encrptString.toString();
    }
}
