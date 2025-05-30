package com.hyf.trade.strategy;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class BeforeXDayRangePromptStrategy implements PromptStrategy {
    PromptStrategy delegate;

    public BeforeXDayRangePromptStrategy(int x) {
        delegate = new DayRangePromptStrategy(-x, -1);
    }

    @Override
    public String getStrategy(PromptStrategyContext context) {
        return delegate.getStrategy(context);
    }
}
