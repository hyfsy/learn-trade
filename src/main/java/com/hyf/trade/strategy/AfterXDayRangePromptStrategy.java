package com.hyf.trade.strategy;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */

public class AfterXDayRangePromptStrategy implements PromptStrategy {
    PromptStrategy delegate;

    public AfterXDayRangePromptStrategy(int x) {
        delegate = new DayRangePromptStrategy(1, x);
    }

    @Override
    public String getStrategy(PromptStrategyContext context) {
        return delegate.getStrategy(context);
    }
}
