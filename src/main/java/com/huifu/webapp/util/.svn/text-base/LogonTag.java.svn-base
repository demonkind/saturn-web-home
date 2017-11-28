/**
 * 
 *上海汇付网络科技有限公司
 *
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.huifu.saturn.common.context.OperationContext;
import com.huifu.saturn.common.context.OperationContextHolder;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 登录标签
 * @author su.zhang
 * @version $Id: LogonTag.java, v 0.1 2012-10-13 下午02:20:32 su.zhang Exp $
 */
public class LogonTag implements TemplateMethodModel {

    /** 
     * @see freemarker.template.TemplateMethodModel#exec(java.util.List)
     */
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments == null || arguments.isEmpty()) {
            throw new RuntimeException("缺少LogonTag key值");
        }
        String a = (String) arguments.get(0);
        OperationContext op = OperationContextHolder.get();
        try {
            return BeanUtils.getProperty(op, a);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
