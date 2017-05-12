package com.aisino.core.util;

import com.aisino.core.dto.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {


    private static ThreadLocal<Date> now = new ThreadLocal<Date>();// 测试时设置以便知道产生的单号
    private static ThreadLocal<Date> now1 = new ThreadLocal<Date>();

    public static final String C_TIME_PATTON_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String C_DATE_PATTON_DEFAULT = "yyyy-MM-dd";
    public static final String C_DATE_PATTON_YEAR = "yyyy";
    public static final String C_DATE_PATTON_NO_TRAVERSE = "yyyyMMdd";
    public static final String C_DATE_PATTON_YEARMONTH = "yyyy-MM";
    public static final String C_TIME_PATTON_HHMMSS = "HH:mm:ss";
    public static final String C_TIME_PATTON_MM = "yyyy-MM-dd HH:mm";
    public static final String C_TIME_MM_DD_MM = "MM-dd HH:mm";
    public static final String C_TIME_YYMMDDHHMM="yyyy年MM月dd日 HH:mm";
    public static final String KPRQ_PATTON = "yyyyMMddHHmmss";


    public static Date parseDate(String strFormat, String dateValue) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
            return dateFormat.parse(dateValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Date convert2String(long time) {
        return new Date(time);
    }

    public static String convert2String(long time, String format) {

        SimpleDateFormat sf = new SimpleDateFormat(format);

        Date date = new Date(time);

        return sf.format(date);
    }

    public static String format(Date aTs_Datetime, String as_Pattern){

        SimpleDateFormat dateFromat = new SimpleDateFormat();
        dateFromat.applyPattern(as_Pattern);
        return dateFromat.format(aTs_Datetime);
    }

    public static String format(Date aTs_Datetime){

        return format(aTs_Datetime,C_DATE_PATTON_DEFAULT);

    }

    public static Date add(Date date, int field, int amount) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 取得指定日期N天后的日期
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    public static Date addMonth(Date date, int month){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date computeStartDate(Date date) {
        Date d = DateUtil.parseDate(DateUtil.C_DATE_PATTON_DEFAULT, DateUtil.format(date, DateUtil.C_DATE_PATTON_DEFAULT));
        return d;
    }
    public static Date computeEndDate(Date date) {
        Date d = DateUtil.parseDate(DateUtil.C_DATE_PATTON_DEFAULT, DateUtil.format(date, DateUtil.C_DATE_PATTON_DEFAULT));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date newDate() {
        if (null!=now.get()){
            return now.get();
        }
        return new Date();
    }
    public static Date newDate1() {
        if (null!=now1.get()){
            return now1.get();
        }
        return newDate();
    }


    /**
     * 如：  10月3日        --   12月25日
     * 结果：
     *      Pair< 10月3日, 10月31日   >
     *      Pair< 11月1日, 11月30日   >
     *      Pair< 12月1日, 12月25日   >
     */
    @SuppressWarnings("deprecation")
    public static List<Pair<Date, Date>> splitByMonth(Date sDate, Date eDate) {
        List<Pair<Date, Date>> list = new ArrayList<Pair<Date,Date>>();
        Integer monthTotle = eDate.getMonth()-sDate.getMonth()+1;
        //跨年情况考虑
        if (sDate.getYear() != eDate.getYear()) {
            monthTotle = 12 - sDate.getMonth() + ( eDate.getYear() - sDate.getYear() -1 ) * 12 + 1;
            monthTotle += eDate.getMonth();
        }
        if (1 == monthTotle) {
            list.add(new Pair<Date, Date>(sDate, eDate));
            return list;
        }
        Date lastDate = null;
        for (int i = 0; i < monthTotle; i++) {
            if (0 == i) {
                list.add(new Pair<Date, Date>(sDate, getLastMonthDay(sDate)));
                lastDate = getLastMonthDay(sDate);
                continue;
            }
            if (i+1 == monthTotle) {
                list.add(new Pair<Date, Date>(getFirstMonthDay(eDate), eDate));
                continue;
            }
            Date monthFirstDay = getFirstDayOfNextMonth(lastDate);
            list.add(new Pair<Date, Date>(monthFirstDay, getLastMonthDay(monthFirstDay)));
            lastDate = monthFirstDay;
        }
        return list;
    }

    /**
     * 如：  10月21日      ,  往前3个月
     * 结果：
     *      Pair<  8月1日,  8月31日   >
     *      Pair<  9月1日,  9月30日   >
     *      Pair< 10月1日, 10月21日   >
     */
    public static List<Pair<Date, Date>> beforeByMonth(Date date, int beforeMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);
        calendar.add(Calendar.MONTH,-beforeMonth+1);
        Date sDate = calendar.getTime();
        return splitByMonth(sDate, date);
    }

    /**
     * 获取当月第一天
     * @param date
     * @return
     */
    public static Date getFirstMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);//设为当前月的1号
        return calendar.getTime();
    }

    /**
     * 获取当月最后一天
     * @param date
     * @return
     */
    public static Date getLastMonthDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);//设为当前月的1号
        calendar.add(Calendar.MONTH,1);//加一个月，变为下月的1号
        calendar.add(Calendar.DATE,-1);//减去一天，变为当月最后一天
        return calendar.getTime();
    }

    /**
     * 获取下月第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);
        calendar.add(Calendar.MONTH,+1);
        return calendar.getTime();
    }

    /**
     * 获取下月最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfNextMonth(Date date) {
        Date firstDayOfNextMonth = getFirstDayOfNextMonth(date);
        return getLastMonthDay(firstDayOfNextMonth);
    }

    /**
     * 获取上月第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);
        calendar.add(Calendar.MONTH,-1);
        return calendar.getTime();
    }

    /**
     * 上月最后一个工作日
     */
    @SuppressWarnings("deprecation")
    public static Date getLastMonthWeekdays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,1);//设为当前月的1号
        calendar.add(Calendar.DATE,-1);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        while (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE,-1);
            day = calendar.get(Calendar.DAY_OF_WEEK);
        }

        Date reDate = calendar.getTime();
        reDate.setHours(0);
        reDate.setMinutes(0);
        reDate.setSeconds(0);

        return reDate;
    }
    /**
     * 取得年份
     * @param date
     * @return
     */
    public static String getYear(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return df.format(date);
    }
    /**
     * 取得月份
     * @param date
     * @return
     */
    public static String getMonth(Date date){
        SimpleDateFormat df = new SimpleDateFormat("MM");
        return df.format(date);
    }

    /**
     * 获取上月最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Date firstDayOfLastMonth = getFirstDayOfLastMonth(date);
        return getLastMonthDay(firstDayOfLastMonth);
    }

    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat(C_DATE_PATTON_DEFAULT);
        Date endDate;
        Date beginDate;
        try {
            endDate = format.parse(endDateStr);
            beginDate = format.parse(beginDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    public static void setNow(Date date) {
        now.set(date);
    }
    public static Date getNow() {
        return now.get();
    }
    public static void setNow1(Date date1) {
        now1.set(date1);
    }
    public static Date getNow1() {
        return now1.get();
    }
    public static void clear() {
        now.set(null);
        now1.set(null);
    }

    public static boolean after(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.after(c2);
    }

    public static boolean before(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return c1.before(c2);
    }

    /**
     * 计算时间差
     * @param beginTime  开始时间
     * @param endTime  结束时间
     * @return  endTime - beginTime (返回秒数)
     * @throws ParseException
     */
    public static long findTimeDifference(Date beginTime, Date endTime) throws ParseException {

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String timeStr1 = sdf.format(beginTime);
        String timeStr2 = sdf.format(endTime);

        long date1=sdf.parse(timeStr1).getTime();
        long date2=sdf.parse(timeStr2).getTime();

        long timeDifference = date1-date2;//为差值

        return timeDifference/1000;
    }
}

