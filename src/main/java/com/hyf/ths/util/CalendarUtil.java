package com.hyf.ths.util;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class CalendarUtil {

    public static Calendar base = Calendar.getInstance();


    public static Calendar getBaseCalendar() {
        return base;
    }

    public static void setBaseCalendarToTomorrow() {
        setBaseCalendarByDelta(1);
    }

    public static void setBaseCalendarByDelta(int delta) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, delta);
        String date = instance.get(Calendar.YEAR) + "." + (instance.get(Calendar.MONTH) + 1) + "." + instance.get(Calendar.DAY_OF_MONTH);
        setBaseCalendar(date);
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

    public static boolean isTradeDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean notTradeDay = dayOfWeek == Calendar.SATURDAY || (dayOfWeek == Calendar.SUNDAY);
        return !notTradeDay;
    }

    public static void addCalendarSkipNotTradeDay(Calendar calendar, int field, int value) {
        do {
            calendar.add(field, value);
        }
        // 非交易日继续前进一次
        while (!isTradeDay(calendar));
    }
}
