/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.data;

import java.util.Map;

public interface CPCacheManager {

	Map getObjectException(String sessionID, int maxInactiveInterval);

	void removeObject(String sessionID); 

	CPCacheReturn putObjectExpire(String sessionID, Map sessionData,
			int maxInactiveInterval);  

}
