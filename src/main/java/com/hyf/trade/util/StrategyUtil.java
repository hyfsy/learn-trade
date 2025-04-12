package com.hyf.trade.util;

import com.hyf.trade.strategy.PromptStrategy;
import com.hyf.trade.strategy.factory.PromptStrategyFactory;

import java.util.LinkedHashMap;
import java.util.List;
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

        List<PromptStrategyFactory> strategies = PromptStrategyFactory.getStrategies();
        for (PromptStrategyFactory strategy : strategies) {
            Map<String, PromptStrategy> build = strategy.build();
            map.putAll(build);
        }

        return map;
    }

    public static void printTransformStrategyMap() {
        strategyMap().forEach((k, v) -> {
            System.out.println(k + ": " + v.getStrategy());
        });
    }
}
