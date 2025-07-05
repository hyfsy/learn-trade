package com.hyf.trade;

import com.hyf.trade.core.SealedOrderAmountCalculator;
import com.hyf.trade.util.*;

import java.util.*;

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
        generateStrategy(config, "/strategy/strategy4.txt");
        generateStrategy(config, "/strategy/strategy5.txt");

        printResentSettlementDay();

        // 2025.7.2
        // calcSealedOrderAmount(CalendarUtil.parse_yyyy_MM_dd("2025.7.1"),
        //         "诚邦股份", "47.49万"
        // );

        // TradeUtil.printSettlementDays(2025);
    }

    private static void generateStrategy(Config config, String strategyPath) {
        String prompt = ConfigUtil.getStrategy(strategyPath);
        AssertUtil.notBlank(prompt);

        System.out.println();
        Calendar base = CalendarUtil.copy(CalendarUtil.getBaseCalendar());
        for (int i = 0; i < config.getLoop(); i++) {
            if (CalendarUtil.needSkip(base)) {
                base.add(Calendar.DAY_OF_YEAR, -1);
                continue;
            }
            String transformedPrompt = StrategyUtil.transformStrategy(prompt, base);
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
            Map<String, List<String>> settlementDayMaps = TradeUtil.getSettlementDayMaps(year);
            String yyyy_mm_dd = CalendarUtil.to_yyyy_MM_dd(calendar);
            List<String> names = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : settlementDayMaps.entrySet()) {
                if (entry.getValue().contains(yyyy_mm_dd)) {
                    names.add(entry.getKey());
                }
            }
            if (!names.isEmpty()) {
                System.out.println("[" + yyyy_mm_dd + "] is settlement day: " + names);
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private static void calcSealedOrderAmount(Calendar calendar, String... interestedStockParam) {
        SealedOrderAmountCalculator.SealedOrderAmountCalcParam param = new SealedOrderAmountCalculator.SealedOrderAmountCalcParam();
        param.setCurrentCalendar(calendar);

        if (interestedStockParam.length % 2 != 0) {
            throw new IllegalArgumentException(Arrays.toString(interestedStockParam));
        }

        for (int i = 0; i < interestedStockParam.length - 1;) {
            param.add(interestedStockParam[i], interestedStockParam[i + 1]);
            i += 2;
        }
        List<SealedOrderAmountCalculator.Result> results = SealedOrderAmountCalculator.calc(param);
        SealedOrderAmountCalculator.print(results);
        System.out.println();
    }
}
