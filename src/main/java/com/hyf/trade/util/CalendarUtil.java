package com.hyf.trade.util;

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
        String date = toYYYY_MM_DD(instance);
        setBaseCalendar(date);
    }

    public static boolean needSkip(Calendar calendar) {
        return !isTradeDay(calendar)
                || ConfigUtil.getConfig().getHoliday().contains(toYYYY_MM_DD(calendar))
                || ConfigUtil.getConfig().getBlack().contains(toYYYY_MM_DD(calendar));
    }

    public static boolean isTradeDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean notTradeDay = dayOfWeek == Calendar.SATURDAY || (dayOfWeek == Calendar.SUNDAY);
        return !notTradeDay;
    }

    public static String toYYYY_MM_DD(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
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
}
