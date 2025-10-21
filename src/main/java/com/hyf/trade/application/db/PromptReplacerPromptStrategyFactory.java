package com.hyf.trade.application.db;

import com.hyf.trade.application.db.entity.PromptReplacer;
import com.hyf.trade.strategy.PromptStrategy;
import com.hyf.trade.strategy.factory.PromptStrategyFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PromptReplacerPromptStrategyFactory implements PromptStrategyFactory {

    private final TradeRepository tradeRepository;

    public PromptReplacerPromptStrategyFactory(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Map<String, PromptStrategy> build() {
        List<PromptReplacer> replacers = tradeRepository.listReplacers();
        Map<String, PromptStrategy> promptStrategyMap = new LinkedHashMap<>();
        for (PromptReplacer replacer : replacers) {
            PromptStrategy promptStrategy = replacer.createPromptStrategy();
            promptStrategyMap.put(replacer.getText(), promptStrategy);
        }
        return promptStrategyMap;
    }
}
