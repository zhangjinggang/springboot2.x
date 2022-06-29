package com.choice.cloud.sysmonitor.config;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = -7712649608066970212L;

    public DateFormat() {
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source) throws ParseException {
        return this.parse(source, (ParsePosition) null);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        if (source != null) {
            try {
                if (source.matches("[-]{0,1}\\d+")) {
                    return new Date(Long.valueOf(source).longValue());
                }

                if (source.matches("\\d{1,4}-\\d{1,2}-\\d{1,2}")) {
                    return (new SimpleDateFormat("yyyy-MM-dd")).parse(source);
                }
                if (source.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                    //return getDateTimeInstance().parse(source);//这种会按照系统时间格式进行
                	return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(source);
                }

                if (source.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}")) {
                    return (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(source);
                }

                if (source.matches("\\d{1,4}-\\d{1,2}-\\d{1,2} \\d{1,2}")) {
                    return (new SimpleDateFormat("yyyy-MM-dd HH")).parse(source);
                }

                if(source.contains("T")) {
                    return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(source);
                }

                if (source.lastIndexOf(".") > -1) {
                    return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")).parse(source);
                }
            } catch (ParseException var4) {
                var4.printStackTrace();
            }
        }

        return null;
    }
}
