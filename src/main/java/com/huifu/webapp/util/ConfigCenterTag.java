/**
 * 
 *上海汇付网络科技有限公司
 *
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.util;

import java.util.List;

import com.huifu.saturn.runtime.context.SaturnApplicationContext;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * freemarker获取configcenter配置的值
 * @author su.zhang
 * @version $Id: ConfigCenterTag.java, v 0.1 2012-10-12 下午01:14:06 su.zhang Exp $
 */
public class ConfigCenterTag  implements TemplateMethodModel {

    /** 
     * @see freemarker.template.TemplateMethodModel#exec(java.util.List)
     */
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        if (arguments == null || arguments.isEmpty()) {
            throw new RuntimeException("缺少ConfigCenterTag key值");
        }
        return SaturnApplicationContext.getApplicationContext((String )(arguments.get(0)));
    }

}
