package com.hyf.trade.strategy;

import java.util.Calendar;

/**
 * @author baB_hyf
 * @date 2025/05/24
 */
public class PromptStrategyContext {

    private final Calendar baseCalendar;

    public PromptStrategyContext(Calendar baseCalendar) {
        this.baseCalendar = baseCalendar;
    }

    public Calendar getBaseCalendar() {
        return baseCalendar;
    }
}
