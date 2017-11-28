/**
 * 
 *汇付天下有限公司
 * Copyright (c) 2006-2012 ChinaPnR,Inc.All Rights Reserved.
 */
package com.huifu.webapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.huifu.saturn.common.utils.SaturnLoggerUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 *  日期显示
 * 
 * @author zhanghaijie
 * @version $Id: DateDisplayTag.java, v 0.1 2012-10-22 下午01:05:49 zhanghaijie Exp $
 */
public class DateDisplayTag implements TemplateMethodModel {

    private static final Logger logger = Logger.getLogger(DateDisplayTag.class);

    /** 
     * 日期显示控件
     * <br>
     * 第一个串为要格式化的Date串，如果为空，表示使用当前时间
     * <br>
     * 第二个串为传入的Date串的格式
     * <br>
     * 第三个串为需要显示的格式 
     * 
     * @see freemarker.template.TemplateMethodModel#exec(java.util.List)
     */
    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        String dateString = (String) arguments.get(0);
        String orignDateFormate = (String) arguments.get(1);
        String displayDateFormat = (String) arguments.get(2);

        Date date = null;
        try {
            date = getDate(dateString, orignDateFormate);
        } catch (ParseException e) {
            SaturnLoggerUtils.error(logger, e, "【日期格式化】日期传入参数格式非法,传入日期:", dateString, "传入日期格式:",
                orignDateFormate, "输入格式:", displayDateFormat);
            throw new RuntimeException(e);
        }

        String displayDate = formatDate(date, displayDateFormat);
        return displayDate;
    }

    /**
     * 
     * @param date
     * @param displayDateFormat
     * @return
     */
    private String formatDate(Date date, String displayDateFormat) {
        SimpleDateFormat dateFm = new SimpleDateFormat(displayDateFormat, Locale.CHINA);
        return dateFm.format(date);
    }

    /**
     * 
     * @param dateString
     * @param orignDateFormate
     * @return
     * @throws ParseException
     */
    private Date getDate(String dateString, String orignDateFormate) throws ParseException {
        if (StringUtils.isBlank(dateString)) {
            Calendar cal = Calendar.getInstance();
            return cal.getTime();
        }
        SimpleDateFormat dateFm = new SimpleDateFormat(orignDateFormate);
        return dateFm.parse(dateString);
    }

}
