/**
 * 
 *上海汇付网络科技有限公司
 *
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


/**
 * 
 * @author su.zhang
 * @version $Id: DownLoadUtil.java, v 0.1 2012-9-22 下午03:57:47 su.zhang Exp $
 */
public class DownLoadUtil {
    
    public static void parseDownLoadRequest(HttpServletResponse response,String fileFullPath,String fileShowName) throws IOException{
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+fileShowName); 
        IOUtils.copy(FileUtils.openInputStream(new File(fileFullPath)), response.getOutputStream());
        response.flushBuffer();
    }

}
