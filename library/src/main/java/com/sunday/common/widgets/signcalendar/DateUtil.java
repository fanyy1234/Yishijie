package com.sunday.common.widgets.signcalendar;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Title: DateUtil
 * @Package cn.wecoder.signcalendar.library
 * @Description: 日期处理帮助类
 * @Author Jim
 * @Date 15/10/20
 * @Time 下午2:50
 * @Version
 */
public class DateUtil {

    /**
     * 比较两个日期的大小
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return  1.同一天返回0；2.之前返回－1；3.之后返回1
    */
    public static int compareDateDay(Date date1, Date date2) {
        int year1 = date1.getYear() + 1900;
        int year2 = date2.getYear() + 1900;

        int month1 = date1.getMonth();
        int month2 = date2.getMonth();

        int day1 = date1.getDate();
        int day2 = date2.getDate();
        //Log.d("infoo", year1+"--"+month1+"---"+day1);

        if(year1 > year2) {
            return 1;
        }else if(year1 < year2) {
            return -1;
        }

        if(month1 > month2) {
            return 1;
        }else if(month1 < month2) {
            return -1;
        }

        if(day1 > day2) {
            return 1;
        }else if(day1 < day2) {
            return -1;
        }

        return 0;
    }

    /**
     * 计算某年某月有多少天
     * @param year 年份
     * @param month 月份 0-11
     * @return
     */
    public static int getDateNum(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year + 1900);
        time.set(Calendar.MONTH, month);
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date strToDate(String str){
        Date date=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        try {
            date=sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToDate1(String str){
        Date date=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        try {
            date=sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime());
        return dayAfter;
    }

    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result <= 0)
            return true;
        else
            return false;
    }

    public static Date getYesterDay(){
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-1);
        date=calendar.getTime();
        return date;
    }

    public static Date getLastMonthDay(){
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.MONTH,-1);
        date=calendar.getTime();
        return date;
    }

    public static String dateToStr(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        return sdf.format(date);
    }


}
