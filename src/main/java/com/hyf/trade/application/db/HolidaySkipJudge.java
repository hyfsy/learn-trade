package com.hyf.trade.application.db;

import com.hyf.trade.application.db.entity.Holidays;
import com.hyf.trade.util.CalendarUtil;

import java.util.Calendar;

public class HolidaySkipJudge implements CalendarSkipJudge {

    private Holidays holidays;

    public HolidaySkipJudge(Holidays holidays) {
        this.holidays = holidays;
    }

    @Override
    public boolean needSkip(Calendar calendar) {
        if (CalendarUtil.isWeekend(calendar)) {
            return true;
        }
        if (holidays == null) {
            return false;
        }
        return holidays.match(calendar);
    }
}
