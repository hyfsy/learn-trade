package com.hyf.trade.application.db.entity;

import com.hyf.trade.application.db.DataNotFoundException;
import com.hyf.trade.strategy.AfterXDayRangePromptStrategy;
import com.hyf.trade.strategy.BeforeXDayPromptStrategy;
import com.hyf.trade.strategy.BeforeXDayRangePromptStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmbeddedPromptReplacer {

    private static final List<PromptReplacer> embeddedPromptReplacers;

    static {
        embeddedPromptReplacers = load();
    }

    public static PromptReplacer get(String id) {
        for (PromptReplacer replacer : embeddedPromptReplacers) {
            if (replacer.getId().equals(id)) {
                return replacer;
            }
        }
        throw new DataNotFoundException(id);
    }

    public static List<PromptReplacer> list() {
        return new ArrayList<>(embeddedPromptReplacers);
    }

    private static List<PromptReplacer> load() {
        List<PromptReplacer> replacers = new ArrayList<>();
        int i = 0;
        replacers.add(create(++i, "上上上上上个交易日", BeforeXDayPromptStrategy.class.getName(), 5));
        replacers.add(create(++i, "上上上上个交易日", BeforeXDayPromptStrategy.class.getName(), 4));
        replacers.add(create(++i, "上上上个交易日", BeforeXDayPromptStrategy.class.getName(), 3));
        replacers.add(create(++i, "上上个交易日", BeforeXDayPromptStrategy.class.getName(), 2));
        replacers.add(create(++i, "上个交易日", BeforeXDayPromptStrategy.class.getName(), 1));
        replacers.add(create(++i, "当前交易日", BeforeXDayPromptStrategy.class.getName(), 0));
        replacers.add(create(++i, "前三个交易日", BeforeXDayRangePromptStrategy.class.getName(), 3));
        replacers.add(create(++i, "前五个交易日", BeforeXDayRangePromptStrategy.class.getName(), 5));
        replacers.add(create(++i, "前十个交易日", BeforeXDayRangePromptStrategy.class.getName(), 10));
        replacers.add(create(++i, "前二十个交易日", BeforeXDayRangePromptStrategy.class.getName(), 20));
        replacers.add(create(++i, "后一个交易日", AfterXDayRangePromptStrategy.class.getName(), 1));
        replacers.add(create(++i, "后五个交易日", AfterXDayRangePromptStrategy.class.getName(), 5));
        return replacers;
    }

    private static PromptReplacer create(int order, String text, String className, Object... params) {
        PromptReplacer replacer = new PromptReplacer();
        replacer.init();
        replacer.setText(text);
        replacer.setClassName(className);
        if (params.length > 0) {
            replacer.setParams(Arrays.stream(params).map(String::valueOf).collect(Collectors.joining(",")));
        }
        replacer.setOrder(order);
        replacer.setEmbedded(true);
        return replacer;
    }
}
