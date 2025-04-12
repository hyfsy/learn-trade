package com.hyf.trade.strategy.factory;

import com.hyf.trade.strategy.PromptStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public interface PromptStrategyFactory {

    static List<PromptStrategyFactory> getStrategies() {
        ServiceLoader<PromptStrategyFactory> strategies = ServiceLoader.load(PromptStrategyFactory.class);
        List<PromptStrategyFactory> promptStrategyFactories = new ArrayList<>();
        strategies.forEach(promptStrategyFactories::add);
        return promptStrategyFactories;
    }

    Map<String, PromptStrategy> build();
}
