package com.rokid.glass.libbase.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class DateUtils {
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd";

    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static Date addMinute(Date date, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    public static String getShortTime(long time) {
        String shortString = "";
        if (time == 0) {
            return shortString;
        }

        long now = Calendar.getInstance().getTimeInMillis();
        long datetime = (now - time) / 1000;
        if (datetime > 7 * 24 * 60 * 60) {
            shortString = "超过一周";
        } else if (datetime > 24 * 60 * 60) {
            shortString = (int) (datetime / (24 * 60 * 60)) + "天前";
        } else if (datetime > 60 * 60) {
            shortString = (int) (datetime / (60 * 60)) + "小时前";
        } else if (datetime > 60) {
            shortString = (int) (datetime / (60)) + "分钟前";
        } else {
            shortString = "刚刚";
        }
        return shortString;
    }

    public static String formatDate(Long time) {
        return formatDate(time, FORMAT_DATE_TIME);
    }

    public static String formatDate(Long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return formatDate(format, time);
    }

    public static String formatDate(SimpleDateFormat format, Long time) {
        if (null == time || time <= 0) {
            return "";
        }
        return format.format(new Date(String.valueOf(time).length() == 13 ? time : time * 1000));
    }

    public static String formatVideoDate(final long time) {
        if (time == 0) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(new Date(time));
    }

    public static String stringForTime(final int time) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = time / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
//        return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        if (totalSeconds > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return "00:00:01";
        }
    }
}
