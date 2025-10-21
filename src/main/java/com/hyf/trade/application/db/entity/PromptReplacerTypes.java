package com.hyf.trade.application.db.entity;

import com.hyf.trade.strategy.AfterXDayRangePromptStrategy;
import com.hyf.trade.strategy.BeforeXDayPromptStrategy;
import com.hyf.trade.strategy.BeforeXDayRangePromptStrategy;
import com.hyf.trade.strategy.DayRangePromptStrategy;

public enum PromptReplacerTypes {

    BEFORE(BeforeXDayPromptStrategy.class), //
    BEFORE_RANGE(BeforeXDayRangePromptStrategy.class), //
    AFTER(AfterXDayRangePromptStrategy.class), //
    RANGE(DayRangePromptStrategy.class), //
    ;

    private final Class<?> klass;
    private final int paramCount;

    PromptReplacerTypes(Class<?> klass) {
        this.klass = klass;
        paramCount = klass.getConstructors()[0].getParameterCount();
    }

    public Class<?> getKlass() {
        return klass;
    }

    public int getParamCount() {
        return paramCount;
    }
}
