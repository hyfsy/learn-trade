package com.hyf.trade.util;

import com.hyf.trade.strategy.AfterXDayRangePromptStrategy;
import com.hyf.trade.strategy.BeforeXDayPromptStrategy;
import com.hyf.trade.strategy.BeforeXDayRangePromptStrategy;
import com.hyf.trade.strategy.PromptStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class StrategyUtil {

    public static String transformStrategy(String prompt) {

        Map<String, PromptStrategy> strategyMap = strategyMap();

        for (Map.Entry<String, PromptStrategy> entry : strategyMap.entrySet()) {
            prompt = prompt.replace(entry.getKey(), entry.getValue().getStrategy());
        }

        return prompt;
    }

    public static Map<String, PromptStrategy> strategyMap() {
        Map<String, PromptStrategy> map = new LinkedHashMap<>();

        map.put("当前交易日", new BeforeXDayPromptStrategy(0));
        map.put("上上个交易日", new BeforeXDayPromptStrategy(2));
        map.put("上个交易日", new BeforeXDayPromptStrategy(1));
        map.put("前三个交易日", new BeforeXDayRangePromptStrategy(3));
        map.put("后五个交易日", new AfterXDayRangePromptStrategy(5));
        return map;
    }

    public static void printTransformStrategyMap() {
        strategyMap().forEach((k, v) -> {
            System.out.println(k + ": " + v.getStrategy());
        });
    }
}
