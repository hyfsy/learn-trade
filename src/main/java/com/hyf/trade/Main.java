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

        generateStrategy();
        printResentSettlementDay();
    }

    private static void generateStrategy() {
        String prompt = ConfigUtil.getStrategy();
        AssertUtil.notBlank(prompt);

        Config config = ConfigUtil.getConfig();
        initBaseCalendar(config);

        Calendar base = CalendarUtil.base;
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
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            boolean settlementDay = TradeUtil.isSettlementDay(TradeUtil.currentYear(), calendar);
            if (settlementDay) {
                System.out.println("[" + CalendarUtil.to_yyyy_MM_dd(calendar) + "] is settlement day");
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
