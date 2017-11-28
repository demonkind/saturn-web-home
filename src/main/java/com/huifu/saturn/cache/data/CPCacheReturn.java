/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.cache.data;
	/**
	 * @author liang.chenl
	 *
	 */
	public class CPCacheReturn {
		private boolean succeed = false;
		private int serializedLen = 0;
		
		public CPCacheReturn() {
			
		}
		
		public int getSerializedLen() {
			return serializedLen;
		}
		public void setSerializedLen(int serializedLen) {
			this.serializedLen = serializedLen;
		}
		public boolean isSucceed() {
			return succeed;
		}
		public void setSucceed(boolean succeed) {
			this.succeed = succeed;
		}
	}

