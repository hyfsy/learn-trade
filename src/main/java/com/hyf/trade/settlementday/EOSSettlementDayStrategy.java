package com.hyf.trade.settlementday;

import com.hyf.trade.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 季末最后一周，一周只有两天时，额外取上周整周
 * <p>
 * end of season
 *
 * @author baB_hyf
 * @date 2025/04/14
 */
public class EOSSettlementDayStrategy implements SettlementDayStrategy {

    private static final List<Integer> quarters = Arrays.asList(3, 6, 9, 12); // 四季度底

    @Override
    public String getName() {
        return "四季度的最后一周";
    }

    @Override
    public List<String> getSettlementDays(Calendar calendar) {
        List<String> settlementDays = new ArrayList<>();
        for (Integer month : quarters) {
            calendar.set(Calendar.MONTH, month - 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1); // 得到下一月的月初
            calendar.add(Calendar.DAY_OF_YEAR, -1); // 当月月底

            // 过滤周末或一周只有两天的情况
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
                    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                    || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                            || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        settlementDays.add(CalendarUtil.to_yyyy_MM_dd(calendar));
                    }
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                }
            }

            for (int i = 0; i < 5; i++) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    continue;
                }
                settlementDays.add(CalendarUtil.to_yyyy_MM_dd(calendar));
                calendar.add(Calendar.DAY_OF_YEAR, -1);
            }

        }
        return settlementDays;
    }
}
