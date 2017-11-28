/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author su.zhang
 */
public interface SessionRequestContext  {
    /**
     *
     */
    SessionConfig getSessionConfig();

    /**
     *
     */
    boolean isSessionInvalidated();

    /**
     */
    void clear();
    
    
    /**
     *
     */
    //RequestContext getWrappedRequestContext();

    /**
     *
     */
    ServletContext getServletContext();

    /**
     *
     */
    HttpServletRequest getRequest();

    /**
     *
     */
    HttpServletResponse getResponse();

    /**
     */
    void prepare();

    /**
     *
     */
    void commit() throws Exception;
    
    /**
     * @return HttpServletRequest
     */
    HttpServletRequest getHttpServletRequest();
}

