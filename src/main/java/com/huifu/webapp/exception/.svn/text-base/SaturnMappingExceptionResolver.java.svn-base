/**
 * 
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.huifu.saturn.common.utils.SaturnLoggerUtils;

/**
 * 
 * @author zhanghaijiejava.lang.Exception
 * @version $Id: SaturnExceptionResolver.java, v 0.1 2012-10-16 下午05:03:53 zhanghaijie Exp $
 */
public class SaturnMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = Logger.getLogger(SaturnMappingExceptionResolver.class);

    /**
     * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that
     * represents a specific error page if appropriate. <p>May be overridden in subclasses, in order to apply specific
     * exception checks. Note that this template method will be invoked <i>after</i> checking whether this resolved applies
     * ("mappedHandlers" etc), so an implementation may simply proceed with its actual exception handling.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for example,
     * if multipart resolution failed)
     * @param ex the exception that got thrown during handler execution
     * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
     */
    @Override
    public ModelAndView doResolveException(HttpServletRequest request,
                                           HttpServletResponse response, Object handler,
                                           Exception ex) {
        SaturnLoggerUtils.error(logger, ex, "【web请求异常】");
        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(ex, request);
        if (viewName != null) {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode(request, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode);
            }
            return getModelAndView(viewName, ex, request);
        } else {
            return getModelAndView("500err", ex, request);
        }
    }
}
