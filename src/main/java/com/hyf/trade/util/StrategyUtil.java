package com.hyf.trade.util;

import com.hyf.trade.strategy.Compactor;
import com.hyf.trade.strategy.PromptStrategy;
import com.hyf.trade.strategy.PromptStrategyContext;
import com.hyf.trade.strategy.factory.PromptStrategyFactory;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class StrategyUtil {

    public static String transformPrompt(PromptStrategyFactory promptStrategyFactory, String prompt, Calendar baseCalendar) {
        Map<String, PromptStrategy> strategyMap = promptStrategyFactory.build();
        PromptStrategyContext context = new PromptStrategyContext(baseCalendar);
        for (Map.Entry<String, PromptStrategy> entry : strategyMap.entrySet()) {
            prompt = prompt.replace(entry.getKey(), entry.getValue().getStrategy(context));
        }

        prompt = Compactor.compact(prompt);

        return prompt;
    }

    public static String transformStrategy(String prompt, Calendar baseCalendar) {

        Map<String, PromptStrategy> strategyMap = strategyMap();

        PromptStrategyContext context = new PromptStrategyContext(baseCalendar);
        for (Map.Entry<String, PromptStrategy> entry : strategyMap.entrySet()) {
            prompt = prompt.replace(entry.getKey(), entry.getValue().getStrategy(context));
        }

        prompt = Compactor.compact(prompt);

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

    public static void printTransformStrategyMap(Calendar baseCalendar) {
        PromptStrategyContext context = new PromptStrategyContext(baseCalendar);
        strategyMap().forEach((k, v) -> {
            System.out.println(k + ": " + v.getStrategy(context));
        });
    }
}
