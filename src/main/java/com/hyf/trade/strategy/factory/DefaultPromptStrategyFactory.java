package com.hyf.trade.strategy.factory;

import com.hyf.trade.strategy.AfterXDayRangePromptStrategy;
import com.hyf.trade.strategy.BeforeXDayPromptStrategy;
import com.hyf.trade.strategy.BeforeXDayRangePromptStrategy;
import com.hyf.trade.strategy.PromptStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public class DefaultPromptStrategyFactory implements PromptStrategyFactory {
    @Override
    public Map<String, PromptStrategy> build() {
        Map<String, PromptStrategy> map = new LinkedHashMap<>();
        map.put("上上上上上个交易日", new BeforeXDayPromptStrategy(5));
        map.put("上上上上个交易日", new BeforeXDayPromptStrategy(4));
        map.put("上上上个交易日", new BeforeXDayPromptStrategy(3));
        map.put("上上个交易日", new BeforeXDayPromptStrategy(2));
        map.put("上个交易日", new BeforeXDayPromptStrategy(1));
        map.put("当前交易日", new BeforeXDayPromptStrategy(0));
        map.put("前三个交易日", new BeforeXDayRangePromptStrategy(3));
        map.put("前五个交易日", new BeforeXDayRangePromptStrategy(5));
        map.put("前十个交易日", new BeforeXDayRangePromptStrategy(10));
        map.put("前二十个交易日", new BeforeXDayRangePromptStrategy(20));
        map.put("前六十个交易日", new BeforeXDayRangePromptStrategy(60));
        map.put("后一个交易日", new AfterXDayRangePromptStrategy(1));
        map.put("后五个交易日", new AfterXDayRangePromptStrategy(5));
        return map;
    }
}
