package com.hyf.trade.settlementday;

import com.hyf.trade.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public abstract class AbstractStockIndexSettlementDayStrategy implements SettlementDayStrategy {

    @Override
    public List<String> getSettlementDays() {
        Calendar calendar = Calendar.getInstance();
        List<String> settlementDays = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 得到当月的月初
            int time = getMonthTime();
            int curTime = 0;
            while (calendar.get(Calendar.MONTH) == i) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == getWeekTime()) {
                    curTime++;
                    if (time == curTime) {
                        break;
                    }
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            if (time != curTime) {
                throw new IllegalStateException("Not found the forth wednesday");
            }
            while (CalendarUtil.needSkip(calendar)) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            String yyyy_mm_dd = CalendarUtil.toYYYY_MM_DD(calendar);
            settlementDays.add(yyyy_mm_dd);
        }
        return settlementDays;
    }

    protected abstract int getMonthTime();

    protected abstract int getWeekTime();

}
