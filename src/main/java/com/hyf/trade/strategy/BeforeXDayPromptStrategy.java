package com.hyf.trade.strategy;

import com.hyf.trade.util.CalendarUtil;

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
    public String getStrategy(PromptStrategyContext context) {
        Calendar calendar = CalendarUtil.copy(context.getBaseCalendar());
        for (int i = 0; i < x; i++) {
            CalendarUtil.addCalendarSupportSkip(calendar, Calendar.DAY_OF_YEAR, -1);
        }
        return CalendarUtil.to_yyyy_MM_dd(calendar) + "日";
    }
}
