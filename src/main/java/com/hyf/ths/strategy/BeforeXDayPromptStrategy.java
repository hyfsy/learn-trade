package com.hyf.ths.strategy;

import com.hyf.ths.util.CalendarUtil;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class BeforeXDayPromptStrategy implements PromptStrategy {
    int x;

    public BeforeXDayPromptStrategy(int x) {
        this.x = x;
    }

    @Override
    public String getStrategy() {
        Calendar calendar = (Calendar) CalendarUtil.getBaseCalendar().clone();
        for (int i = 0; i < x; i++) {
            CalendarUtil.addCalendarSkipNotTradeDay(calendar, Calendar.DAY_OF_YEAR, -1);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "." + month + "." + dayOfMonth + "日";
    }
}
