package com.hyf.trade.util;

import com.hyf.trade.settlementday.SettlementDayStrategy;

import java.util.*;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public class TradeUtil {

    public static boolean currentDayIsSettlementDay() {
        int currentYear = currentYear();
        Calendar calendar = Calendar.getInstance();
        return isSettlementDay(currentYear, calendar);
    }

    public static boolean isSettlementDay(int year, Calendar calendar) {
        String yyyyMMdd = CalendarUtil.to_yyyy_MM_dd(calendar);
        return getSettlementDays(year).contains(yyyyMMdd);
    }

    public static List<String> getSettlementDays(int year) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);

        Set<String> days = new HashSet<>();
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        for (SettlementDayStrategy strategy : strategies) {
            days.addAll(strategy.getSettlementDays(CalendarUtil.copy(calendar)));
        }
        days.removeAll(ConfigUtil.getConfig().getHoliday());
        days.removeAll(ConfigUtil.getConfig().getBlack());

        return new ArrayList<>(days);
    }

    public static void printSettlementDays() {
        printSettlementDays(currentYear());
    }

    public static void printSettlementDays(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        strategies.stream().map(s -> s.getSettlementDays(CalendarUtil.copy(calendar))).flatMap(Collection::stream)
                .sorted(CalendarUtil::max).forEach(System.out::println);
    }

    public static void printSettlementDaysSeparately() {
        printSettlementDaysSeparately(currentYear());
    }

    public static void printSettlementDaysSeparately(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        StringBuilder sb = new StringBuilder();
        for (SettlementDayStrategy strategy : strategies) {
            List<String> settlementDays = strategy.getSettlementDays(CalendarUtil.copy(calendar));
            sb.append(strategy.getName()).append(":").append("\r\n");
            for (String settlementDay : settlementDays) {
                sb.append(" - ").append(settlementDay).append("\r\n");
            }
        }
        System.out.println(sb.toString());
    }

    public static int currentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
