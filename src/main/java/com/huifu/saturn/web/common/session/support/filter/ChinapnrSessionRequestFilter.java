/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.support.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import com.huifu.saturn.web.common.session.request.DefaultSessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;

public class ChinapnrSessionRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(ChinapnrSessionRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        SessionRequestContext sessionRequestContext = null;
        try {
            if ((request instanceof HttpServletRequest)
                && (response instanceof HttpServletResponse)) {
                sessionRequestContext = DefaultSessionConfig.getDefaultSessionConfig()
                    .getRequestContextWrapper((HttpServletRequest) request,
                        (HttpServletResponse) response, getFilterConfig().getServletContext());
                filterChain.doFilter(sessionRequestContext.getRequest(), response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sessionRequestContext != null) {
                try {
                    sessionRequestContext.commit();
                } catch (Exception e) {
                    logger.error("session commit error, sessionID" + request.getSession().getId(),
                        e);
                }
            }
        }

    }

}
