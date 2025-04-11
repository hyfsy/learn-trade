package com.hyf.ths.strategy;

import com.hyf.ths.util.CalendarUtil;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class DayRangePromptStrategy implements PromptStrategy {

    int prevOffset;
    int nextOffset;

    public DayRangePromptStrategy(int prevOffset, int nextOffset) {
        this.prevOffset = prevOffset;
        this.nextOffset = nextOffset;
    }

    @Override
    public String getStrategy() {
        Calendar prevCalendar = (Calendar) CalendarUtil.getBaseCalendar().clone();
        int prevOffsetLoop = prevOffset < 0 ? -prevOffset : prevOffset;
        int prevDelta = prevOffset < 0 ? -1 : 1;
        for (int i = 0; i < prevOffsetLoop; i++) {
            CalendarUtil.addCalendarSkipNotTradeDay(prevCalendar, Calendar.DAY_OF_YEAR, prevDelta);
        }
        int prevYear = prevCalendar.get(Calendar.YEAR);
        int prevMonth = prevCalendar.get(Calendar.MONTH) + 1;
        int prevDayOfMonth = prevCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar nextCalendar = (Calendar) CalendarUtil.getBaseCalendar().clone();
        int nextOffsetLoop = nextOffset < 0 ? -nextOffset : nextOffset;
        int nextDelta = nextOffset < 0 ? -1 : 1;
        for (int i = 0; i < nextOffsetLoop; i++) {
            CalendarUtil.addCalendarSkipNotTradeDay(nextCalendar, Calendar.DAY_OF_YEAR, nextDelta);
        }
        int nextYear = nextCalendar.get(Calendar.YEAR);
        int nextMonth = nextCalendar.get(Calendar.MONTH) + 1;
        int nextDayOfMonth = nextCalendar.get(Calendar.DAY_OF_MONTH);

        return prevYear + "." + prevMonth + "." + prevDayOfMonth + "日到" + nextYear + "." + nextMonth + "." + nextDayOfMonth + "日";
    }
}
