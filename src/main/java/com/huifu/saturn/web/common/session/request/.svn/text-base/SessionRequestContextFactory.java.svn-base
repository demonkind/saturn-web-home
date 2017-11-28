/**
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.saturn.web.common.session.request;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用来创建和初始化<code>SessionRequestContext</code>的工厂
 * @author su.zhang
 * @version $Id$
 */
public class SessionRequestContextFactory {

    protected static final Log logger = LogFactory.getLog(SessionRequestContextFactory.class);
    // private Resource sessionConfigFile;
    private String             cookieDomainName;
    private String             cpcacheAddr;
    private String             slaveCacheAddr;
    private String             sessionCookiePath;
    private Integer            exp;

    /**
     * 初始化方法，检查sessionConfigFile并解析成{@link Configuration} 
     * @throws IOException 
     * @throws ConfigurationException 
     */
    public void init() throws ConfigurationException, IOException {
        try {
            //  XMLConfiguration configurationXml = new XMLConfiguration();
            // configurationXml.load(sessionConfigFile.getInputStream());
            logger.info("初始化分布式session工厂...");
            DefaultSessionConfig.getDefaultSessionConfig().initSessionConfiguration(
                cookieDomainName, cpcacheAddr, slaveCacheAddr, sessionCookiePath, exp);
            logger.info("初始化分布式session工厂成功...");
        } catch (Exception e) {
            logger.error("加载session.xml报错.", e);
        }
    }

    /**
     * Getter method for property <tt>cookieDomainName</tt>.
     * 
     * @return property value of cookieDomainName
     */
    public String getCookieDomainName() {
        return cookieDomainName;
    }

    /**
     * Setter method for property <tt>cookieDomainName</tt>.
     * 
     * @param cookieDomainName value to be assigned to property cookieDomainName
     */
    public void setCookieDomainName(String cookieDomainName) {
        this.cookieDomainName = cookieDomainName;
    }

    /**
     * Getter method for property <tt>cpcacheAddr</tt>.
     * 
     * @return property value of cpcacheAddr
     */
    public String getCpcacheAddr() {
        return cpcacheAddr;
    }

    /**
     * Setter method for property <tt>cpcacheAddr</tt>.
     * 
     * @param cpcacheAddr value to be assigned to property cpcacheAddr
     */
    public void setCpcacheAddr(String cpcacheAddr) {
        this.cpcacheAddr = cpcacheAddr;
    }

    /**
     * Getter method for property <tt>sessionCookiePath</tt>.
     * 
     * @return property value of sessionCookiePath
     */
    public String getSessionCookiePath() {
        return sessionCookiePath;
    }

    /**
     * Setter method for property <tt>sessionCookiePath</tt>.
     * 
     * @param sessionCookiePath value to be assigned to property sessionCookiePath
     */
    public void setSessionCookiePath(String sessionCookiePath) {
        this.sessionCookiePath = sessionCookiePath;
    }

    /**
     * Getter method for property <tt>exp</tt>.
     * 
     * @return property value of exp
     */
    public Integer getExp() {
        return exp;
    }

    /**
     * Setter method for property <tt>exp</tt>.
     * 
     * @param exp value to be assigned to property exp
     */
    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getSlaveCacheAddr() {
        return slaveCacheAddr;
    }

    public void setSlaveCacheAddr(String slaveCacheAddr) {
        this.slaveCacheAddr = slaveCacheAddr;
    }

}
