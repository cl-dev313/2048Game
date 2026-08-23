package com.example.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 * 常用的日期格式化和计算方法
 */
public class DateUtils {

    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_HMS = "HH:mm:ss";

    /**
     * 获取当前日期字符串
     * @param format 日期格式，如 yyyy-MM-dd
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 获取当前完整日期时间
     */
    public static String getCurrentDateTime() {
        return getCurrentDate(FORMAT_YMD_HMS);
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date date, String format) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 解析日期字符串
     */
    public static Date parseDate(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算两个日期之间的天数差
     * @return 天数差（正数表示 date2 在 date1 之后）
     */
    public static long daysBetween(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取指定日期是星期几
     * @return 1=星期日, 2=星期一, ..., 7=星期六
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期几的中文名称
     */
    public static String getWeekDayCN(Date date) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        int day = getDayOfWeek(date);
        return "星期" + weekDays[day - 1];
    }

    /**
     * 判断是否是今天
     */
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.setTime(date);
        return today.get(Calendar.YEAR) == target.get(Calendar.YEAR)
                && today.get(Calendar.MONTH) == target.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期增加天数
     */
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 获取今天是今年的第几天
     */
    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }
}
