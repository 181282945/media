package com.aisino.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by 为 on 2017-6-3.
 */
public class CalendarUtil {

    public static GregorianCalendar parseGregorianCalendar(Date date){
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar;
    }

    /**
     * 计算一小时前
     */
    public static GregorianCalendar getHourAgo(GregorianCalendar origin,int hourAgo){
        GregorianCalendar gCal = (GregorianCalendar)origin.clone();
        gCal.set(Calendar.HOUR,gCal.get(Calendar.HOUR)-hourAgo);
        return gCal;
    }

    /**
     * 根据date计算?天前
     */
    public static GregorianCalendar getDayAgo(GregorianCalendar origin,int dayAgo){
        GregorianCalendar gCal = (GregorianCalendar)origin.clone();
        gCal.set(Calendar.DAY_OF_MONTH,gCal.get(Calendar.DAY_OF_MONTH)-dayAgo);
        return gCal;
    }

    /**
     * 	把分秒毫秒清零
     */
    public static GregorianCalendar removeMSM(GregorianCalendar target){
        target.set(Calendar.MINUTE,0);
        target.set(Calendar.SECOND,0);
        target.set(Calendar.MILLISECOND,0);
        return target;
    }

    /**
     * 	把时分秒毫秒清零
     */
    public static GregorianCalendar removeHMSM(GregorianCalendar target){
        target.set(Calendar.HOUR_OF_DAY,0);
        target.set(Calendar.MINUTE,0);
        target.set(Calendar.SECOND,0);
        target.set(Calendar.MILLISECOND,0);
        return target;
    }

    /**
     * 返回当前日期,0分0秒0毫秒
     */
    public static GregorianCalendar nowWithZeroMSM(){
        return removeMSM(new GregorianCalendar(Locale.CHINA));
    }

    /**
     * 返回当前日期,0时0分0秒0毫秒
     */
    public static GregorianCalendar nowWithZeroHMSM(){
        return removeHMSM(new GregorianCalendar(Locale.CHINA));
    }
}
