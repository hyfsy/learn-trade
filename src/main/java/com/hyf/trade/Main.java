package com.hyf.trade;

import com.hyf.trade.util.AssertUtil;
import com.hyf.trade.util.CalendarUtil;
import com.hyf.trade.util.ConfigUtil;
import com.hyf.trade.util.StrategyUtil;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/03
 */
public class Main {

    public static void main(String[] args) {
        // System.setProperty("jarMode", "true");

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

        // printTransformStrategyMap();
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
}
