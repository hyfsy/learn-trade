package com.hyf.trade;

import com.hyf.trade.util.*;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/03
 */
public class Main {

    public static void main(String[] args) {
        // System.setProperty("jarMode", "true");

        Config config = ConfigUtil.getConfig();
        initBaseCalendar(config);

        generateStrategy(config, "/strategy/strategy.txt");
        generateStrategy(config, "/strategy/strategy2.txt");

        printResentSettlementDay();

        // TradeUtil.printSettlementDaysSeparately(2024);
    }

    private static void generateStrategy(Config config, String strategyPath) {
        String prompt = ConfigUtil.getStrategy(strategyPath);
        AssertUtil.notBlank(prompt);

        System.out.println();
        Calendar base = CalendarUtil.copy(CalendarUtil.base);
        for (int i = 0; i < config.getLoop(); i++) {
            if (CalendarUtil.needSkip(base)) {
                base.add(Calendar.DAY_OF_YEAR, -1);
                continue;
            }
            String transformedPrompt = StrategyUtil.transformStrategy(prompt);
            System.out.println(transformedPrompt);
            base.add(Calendar.DAY_OF_YEAR, -1);
        }
    }

    private static void initBaseCalendar(Config config) {
        String baseString = config.getBase();
        if (baseString.contains(".")) {
            CalendarUtil.setBaseCalendar(baseString);
        }
        else {
            int delta = Integer.parseInt(baseString);
            CalendarUtil.setBaseCalendarByDelta(delta);
        }
    }

    private static void printResentSettlementDay() {
        System.out.println();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            boolean settlementDay = TradeUtil.isSettlementDay(year, calendar);
            if (settlementDay) {
                System.out.println("[" + CalendarUtil.to_yyyy_MM_dd(calendar) + "] is settlement day");
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
