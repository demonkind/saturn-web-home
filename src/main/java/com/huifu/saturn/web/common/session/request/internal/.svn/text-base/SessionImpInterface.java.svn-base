/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request.internal;

import javax.servlet.http.HttpSession;

import com.huifu.saturn.web.common.session.request.SessionRequestContext;


/**
 * 
 * 
 */
public interface SessionImpInterface extends HttpSession {
	/**
	 * 
	 * @param sessionID
	 * @param requestContext
	 * @param isNew
	 * @param create
	 */
	public boolean init(String sessionID, SessionRequestContext requestContext,
			boolean isNew, boolean create);

	/**
	 * 
	 * @return
	 */
	public boolean isInvalidated();

	/*
	 */
	public void clear();

	/**
	 * 
	 * @return
	 */
	public String getSessionId();

	public boolean isSessionNew();

	/**
	 */
	public void commit();

}
