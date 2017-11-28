/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.huifu.saturn.web.common.session.request.DefaultSessionConfig;
import com.huifu.saturn.web.common.session.request.SessionRequestContext;

public class TestSession {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SessionRequestContext sessionRequestContext = null;

		HttpServletRequest request = null;
		HttpServletResponse response = null;
		ServletContext servletContext = null;
		sessionRequestContext = DefaultSessionConfig.getDefaultSessionConfig()
				.getRequestContextWrapper(request, response, servletContext);
		HttpServletRequest req=sessionRequestContext.getRequest();
		System.out.println(req);

	}

}
