package com.choice.cloud.sysmonitor.common.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.List;

/**
 * 日期工具类，优先使用apache的common库或者huTool等库
 * 工具类中没有的，组合使用工具类的API构建自定义的方法，组合API也无法实现的，参考工具类的源码写自己的方法
 * 因为时间的处理各个场景需求非常不同，日期处理注释必须写的清楚，比如天、自然日等等，否则很容易出bug
 * 
 * @author zhaojufei
 */
public class DmDateUtil {

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

    /**
     * 将日期格式化为字符串时间形式，格式为：yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String format2TimeStr(Date date) {
        return FastDateFormat.getInstance(DATETIME_FORMAT).format(date);
    }

    /**
     * 将日期格式化为字符串时间形式，格式为：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String format2DateStr(Date date) {
        return FastDateFormat.getInstance(DATE_FORMAT).format(date);
    }

    /**
     * 将日期格式化为字符串时间形式，格式为：yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String format2SimpleDateStr(Date date) {
        return FastDateFormat.getInstance(SIMPLE_DATE_FORMAT).format(date);
    }

    /**
     * 将日期格式化为字符串时间形式，格式自定义
     *
     * @param date
     * @return
     */
    public static String format2Str(Date date, String format) {
        return FastDateFormat.getInstance(format).format(date);
    }

    /**
     * 将字符串入参转化为日期，格式为：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static Date formatStr2DateTime(String date) {
        return DateUtil.parse(date, DATETIME_FORMAT);
    }

    /**
     * 将字符串入参转化为日期，格式为：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static Date formatStr2Date(String date) {
        return DateUtil.parse(date, DATE_FORMAT);
    }

    /**
     * 将入参格式化为日期形式（舍掉时分秒），格式为：yyyy-MM-dd，返回类型认为Date
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        return DateUtil.parse(DateUtil.format(date, DATE_FORMAT), DATE_FORMAT);
    }

    /**
     * 将入参格式化为指定格式的日期，返回类型认为Date
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date, String format) {
        return DateUtil.parse(DateUtil.format(date, format), format);
    }

    /**
     * 从开始时间开始，返回days天数的一段日期（包含开始日期）
     *
     * @param beginDate
     * @param days 天数
     * @return
     */
    public static List<Date> getDayPriod(Date beginDate, int days) {
        return getDayPriod(beginDate, DateUtil.offsetDay(beginDate, days), true);
    }

    /**
     * 返回开始日期和结束日之间的日期时间段（不包含时间，包含开始、结束日期）
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Date> getDayPriod(Date beginDate, Date endDate) {
        return getDayPriod(beginDate, endDate, true);
    }

    /**
     * 返回开始日期和结束日之间的日期时间段（不包含时间，包含开始、结束时间）
     * 
     * @param beginDate
     * @param endDate
     * @param isAbs 是否互换，如果开始日期晚于结束日期，则互换后再计算时间段
     * @return
     */
    public static List<Date> getDayPriod(Date beginDate, Date endDate, boolean isAbs) {

        Date begin;
        Date end;

        if (isAbs && beginDate.after(endDate)) {
            begin = endDate;
            end = beginDate;
        } else {
            begin = beginDate;
            end = endDate;
        }

        List<Date> dates = Lists.newArrayList();
        // 首先把开始时间放进去
        dates.add(formatDate(begin));
        // 计算日期差
        long between = DateUtil.between(begin, end, DateUnit.DAY);

        for (int i = 0; i < between; i++) {
            Date cur = DateUtil.offsetDay(beginDate, i + 1);
            dates.add(formatDate(cur));
        }
        return dates;
    }

    /**
     * 从开始时间开始，返回days天数的一段日期（包含开始日期）
     *
     * @param beginDate
     * @param days 天数
     * @return
     */
    public static List<String> getStrDayPriod(Date beginDate, int days, String format) {
        return getStrDayPriod(beginDate, DateUtil.offsetDay(beginDate, days), true, format);
    }

    /**
     * 返回开始日期和结束日之间的日期时间段（不包含时间，包含开始、结束日期）
     *
     * @param beginDate
     * @param endDate
     * @param format
     * @return
     */
    public static List<String> getStrDayPriod(Date beginDate, Date endDate, String format) {
        return getStrDayPriod(beginDate, endDate, true, format);
    }

    /**
     * 返回开始日期和结束日之间的字符串日期时间段（不包含时间，包含开始、结束时间）
     *
     * @param beginDate
     * @param endDate
     * @param isAbs 是否互换，如果开始日期晚于结束日期，则互换后再计算时间段
     * @param format
     * @return
     */
    public static List<String> getStrDayPriod(Date beginDate, Date endDate, boolean isAbs, String format) {

        Date begin;
        Date end;

        if (isAbs && beginDate.after(endDate)) {
            begin = endDate;
            end = beginDate;
        } else {
            begin = beginDate;
            end = endDate;
        }

        List<String> dateList = Lists.newArrayList();

        // 首先把开始时间放进去
        dateList.add(format2Str(begin, format));
        // 计算日期差
        long between = DateUtil.between(begin, end, DateUnit.DAY);

        for (int i = 0; i < between; i++) {
            Date cur = DateUtil.offsetDay(beginDate, i + 1);
            dateList.add(format2Str(cur, format));
        }
        return dateList;
    }
}
