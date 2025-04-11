package com.hyf.ths;

import com.hyf.ths.util.AssertUtil;
import com.hyf.ths.util.CalendarUtil;
import com.hyf.ths.util.ConfigUtil;
import com.hyf.ths.util.StrategyUtil;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/04/03
 */
public class Main {

    public static void main(String[] args) {

        String prompt = ConfigUtil.getStrategy();
        AssertUtil.notBlank(prompt);

        Config config = ConfigUtil.getConfig();
        initBaseCalendar(config);

        Calendar base = CalendarUtil.base;
        for (int i = 0; i < config.getLoop(); i++) {
            if (!CalendarUtil.isTradeDay(base)) {
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
