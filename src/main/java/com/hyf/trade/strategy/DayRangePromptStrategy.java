package com.hyf.trade.strategy;

import com.hyf.trade.util.CalendarUtil;

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

        Calendar nextCalendar = (Calendar) CalendarUtil.getBaseCalendar().clone();
        int nextOffsetLoop = nextOffset < 0 ? -nextOffset : nextOffset;
        int nextDelta = nextOffset < 0 ? -1 : 1;
        for (int i = 0; i < nextOffsetLoop; i++) {
            CalendarUtil.addCalendarSkipNotTradeDay(nextCalendar, Calendar.DAY_OF_YEAR, nextDelta);
        }

        return CalendarUtil.toYYYY_MM_DD(prevCalendar) + "日到" + CalendarUtil.toYYYY_MM_DD(nextCalendar) + "日";
    }
}
