package com.hyf.trade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class CalendarUtil {

    private static final Calendar base = Calendar.getInstance();

    public static Calendar getBaseCalendar() {
        return base;
    }

    public static void setBaseCalendar(String date) {
        String[] split = date.split("\\.");
        String year = split[0];
        String month = split[1];
        String dayOfMonth = split[2];
        base.set(Calendar.YEAR, Integer.parseInt(year));
        base.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        base.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonth));
    }

    public static void setBaseCalendarToTomorrow() {
        setBaseCalendarByDelta(1);
    }

    public static void setBaseCalendarByDelta(int delta) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, delta);
        String date = to_yyyy_MM_dd(instance);
        setBaseCalendar(date);
    }

    public static boolean needSkip(Calendar calendar) {
        return !isTradeDay(calendar)
                || ConfigUtil.getConfig().getHoliday().contains(to_yyyy_MM_dd(calendar))
                || ConfigUtil.getConfig().getBlack().contains(to_yyyy_MM_dd(calendar));
    }

    public static boolean isTradeDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean notTradeDay = dayOfWeek == Calendar.SATURDAY || (dayOfWeek == Calendar.SUNDAY);
        return !notTradeDay;
    }

    public static String to_yyyy_MM_dd(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String to_simple_yyyy_MM_dd(Calendar calendar) {
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        month = month.length() == 1 ? "0" + month : month;
        date = date.length() == 1 ? "0" + date : date;
        return year + month + date;
    }

    public static Calendar parse_yyyy_MM_dd(String yyyy_MM_dd) {
        String[] yyyy_MM_dd_array = yyyy_MM_dd.split("\\.");
        if (yyyy_MM_dd_array.length != 3) {
            throw new IllegalArgumentException(yyyy_MM_dd);
        }

        int year = Integer.parseInt(yyyy_MM_dd_array[0]);
        int month = Integer.parseInt(yyyy_MM_dd_array[1]) - 1;
        int date = Integer.parseInt(yyyy_MM_dd_array[2]);

        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, year);
        instance.set(Calendar.MONTH, month);
        instance.set(Calendar.DAY_OF_MONTH, date);

        return instance;
    }

    public static Calendar parse_simple_yyyy_MM_dd(String simple_yyyy_MM_dd) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(simple_yyyy_MM_dd);
        } catch (ParseException e) {
            throw new IllegalArgumentException(simple_yyyy_MM_dd);
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    public static int max(String day, String another) {
        String[] daySection = day.split("\\.");
        String[] anotherSection = another.split("\\.");
        int dayY = Integer.parseInt(daySection[0]);
        int anotherY = Integer.parseInt(anotherSection[0]);
        int y = Integer.compare(dayY, anotherY);
        if (y != 0) {
            return y;
        }
        int dayM = Integer.parseInt(daySection[1]);
        int anotherM = Integer.parseInt(anotherSection[1]);
        int m = Integer.compare(dayM, anotherM);
        if (m != 0) {
            return m;
        }
        int dayD = Integer.parseInt(daySection[2]);
        int anotherD = Integer.parseInt(anotherSection[2]);
        int d = Integer.compare(dayD, anotherD);
        return d;
    }

    public static void addCalendarSupportSkip(Calendar calendar, int field, int value) {
        do {
            calendar.add(field, value);
        }
        // 非交易日继续前进一次
        while (needSkip(calendar));
    }

    public static void addCalendarForwardOneByOneSupportSkip(Calendar calendar, int field, int value) {
        int time = value > 0 ? value : value / -1;
        boolean neg = value < 0;
        for (int i = 0; i < time; i++) {
            do {
                calendar.add(field, neg ? -1 : 1);
            }
            // 非交易日继续前进一次
            while (needSkip(calendar));
        }
    }

    public static Calendar copy(Calendar calendar) {
        return (Calendar) calendar.clone();
    }
}
