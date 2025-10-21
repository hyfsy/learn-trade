package com.hyf.trade.application.db.entity;

import com.hyf.trade.application.db.TradeRepository;
import com.hyf.trade.util.CalendarUtil;

import java.util.Calendar;

public class Holidays {

    private TradeRepository tradeRepository = TradeRepository.getInstance();

    public boolean match(String calendar) {
        return match(CalendarUtil.parse_yyyy_MM_dd(calendar));
    }

    public boolean match(Calendar calendar) {
        Holiday holiday = tradeRepository.getHoliday(calendar.get(Calendar.YEAR));
        if (holiday == null) {
            return false;
        }
        return holiday.match(calendar);
    }

}
