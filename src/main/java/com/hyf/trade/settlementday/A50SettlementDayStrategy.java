package com.hyf.trade.settlementday;

import com.hyf.trade.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 新华富时A50指数期货的交割日是每年的3、6、9、12月以及这些月份其后的两个延展月的倒数第二个工作日，
 * 即每年的2、3、5、6、8、9、11、12月每个月份的倒数第二个工作日。
 *
 * @author baB_hyf
 * @date 2025/04/12
 */
public class A50SettlementDayStrategy implements SettlementDayStrategy {

    private static final List<Integer> months = Arrays.asList(3, 6, 9, 12);
    private static final int           day    = 2;

    @Override
    public String getName() {
        return "A50指数期货交割日";
    }

    @Override
    public List<String> getSettlementDays() {
        List<Integer> realMonths = new ArrayList<>();
        for (Integer month : months) {
            realMonths.add(month - 1);
            realMonths.add(month);
        }
        Calendar calendar = Calendar.getInstance();
        List<String> settlementDays = new ArrayList<>();
        for (Integer realMonth : realMonths) {
            calendar.set(Calendar.MONTH, realMonth - 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 得到下一月的月初
            for (int i = 0; i < day; i++) {
                CalendarUtil.addCalendarSupportSkip(calendar, Calendar.DAY_OF_YEAR, -1);
            }
            String yyyy_mm_dd = CalendarUtil.toYYYY_MM_DD(calendar);
            settlementDays.add(yyyy_mm_dd);
        }
        return settlementDays;
    }
}
