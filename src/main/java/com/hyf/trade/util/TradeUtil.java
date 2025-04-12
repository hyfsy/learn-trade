package com.hyf.trade.util;

import com.hyf.trade.settlementday.SettlementDayStrategy;

import java.util.*;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public class TradeUtil {

    public static boolean currentDayIsSettlementDay() {
        return isSettlementDay(Calendar.getInstance());
    }

    public static boolean isSettlementDay(Calendar calendar) {
        String s = CalendarUtil.toYYYY_MM_DD(calendar);
        return getSettlementDays().contains(s);
    }

    public static List<String> getSettlementDays() {

        Set<String> days = new HashSet<>();
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        for (SettlementDayStrategy strategy : strategies) {
            days.addAll(strategy.getSettlementDays());
        }
        days.removeAll(ConfigUtil.getConfig().getHoliday());
        days.removeAll(ConfigUtil.getConfig().getBlack());

        return new ArrayList<>(days);
    }

    public static void printSettlementDays() {
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        strategies.stream().map(SettlementDayStrategy::getSettlementDays).flatMap(Collection::stream)
                .sorted(CalendarUtil::max).forEach(System.out::println);
    }

    public static void printSettlementDaysSeparately() {
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        StringBuilder sb = new StringBuilder();
        for (SettlementDayStrategy strategy : strategies) {
            List<String> settlementDays = strategy.getSettlementDays();
            sb.append(strategy.getName()).append(":").append("\r\n");
            for (String settlementDay : settlementDays) {
                sb.append(" - ").append(settlementDay).append("\r\n");
            }
        }
        System.out.println(sb.toString());
    }
}
