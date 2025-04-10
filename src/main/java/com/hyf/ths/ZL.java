package com.hyf.ths;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author baB_hyf
 * @date 2025/04/03
 */
public class ZL {

    public static Calendar base = Calendar.getInstance();

    public static void main(String[] args) {

        String prompt = "";
        checkPrompt(prompt);

        setBaseCalendarToTomorrow();
        // setBaseCalendar("2025.4.10");

        for (int i = 0; i < 10; i++) {
            if (!isTradeDay(base)) {
                base.add(Calendar.DAY_OF_YEAR, -1);
                continue;
            }
            String transformedPrompt = transformStrategy(prompt);
            System.out.println(transformedPrompt);
            base.add(Calendar.DAY_OF_YEAR, -1);
        }

        // printTransformStrategyMap();
    }

    public static Calendar getBaseCalendar() {
        return base;
    }

    public static void setBaseCalendar(String date) {
        String[] split = date.split("\\.");
        String year = split[0];
        String month = split[1];
        String dayOfMonth = split[2];
        base.set(Calendar.YEAR, Integer.parseInt(year));
        base.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        base.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOfMonth));
    }

    public static void setBaseCalendarToTomorrow() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, 1);
        String date = instance.get(Calendar.YEAR) + "." + (instance.get(Calendar.MONTH) + 1) + "." + instance.get(Calendar.DAY_OF_MONTH);
        setBaseCalendar(date);
    }

    public static boolean isTradeDay(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean notTradeDay = dayOfWeek == Calendar.SATURDAY || (dayOfWeek == Calendar.SUNDAY);
        return !notTradeDay;
    }

    public static void addCalendarSkipNotTradeDay(Calendar calendar, int field, int value) {
        do {
            calendar.add(field, value);
        }
        // 非交易日继续前进一次
        while (!isTradeDay(calendar));
    }

    public static void printTransformStrategyMap() {
        strategyMap().forEach((k, v) -> {
            System.out.println(k + ": " + v.getStrategy());
        });
    }

    public static String transformStrategy(String prompt) {

        Map<String, PromptStrategy> strategyMap = strategyMap();

        for (Map.Entry<String, PromptStrategy> entry : strategyMap.entrySet()) {
            prompt = prompt.replace(entry.getKey(), entry.getValue().getStrategy());
        }

        return prompt;
    }

    public static Map<String, PromptStrategy> strategyMap() {
        Map<String, PromptStrategy> map = new LinkedHashMap<>();

        map.put("当前交易日", new BeforeXDayPromptStrategy(0));
        map.put("上上个交易日", new BeforeXDayPromptStrategy(2));
        map.put("上个交易日", new BeforeXDayPromptStrategy(1));
        map.put("前三个交易日", new BeforeXDayRangePromptStrategy(3));
        map.put("后五个交易日", new AfterXDayRangePromptStrategy(5));
        return map;
    }

    private static void checkPrompt(String prompt) {
        if (prompt == null || prompt.isEmpty() || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("prompt is empty");
        }
    }

    public static interface PromptStrategy {
        String getStrategy();
    }

    public static class BeforeXDayPromptStrategy implements PromptStrategy {
        int x;

        public BeforeXDayPromptStrategy(int x) {
            this.x = x;
        }

        @Override
        public String getStrategy() {
            Calendar calendar = (Calendar) getBaseCalendar().clone();
            for (int i = 0; i < x; i++) {
                addCalendarSkipNotTradeDay(calendar, Calendar.DAY_OF_YEAR, -1);
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            return year + "." + month + "." + dayOfMonth + "日";
        }
    }

    public static class DayRangePromptStrategy implements PromptStrategy {

        int prevOffset;
        int nextOffset;

        public DayRangePromptStrategy(int prevOffset, int nextOffset) {
            this.prevOffset = prevOffset;
            this.nextOffset = nextOffset;
        }

        @Override
        public String getStrategy() {
            Calendar prevCalendar = (Calendar) getBaseCalendar().clone();
            int prevOffsetLoop = prevOffset < 0 ? -prevOffset : prevOffset;
            int prevDelta = prevOffset < 0 ? -1 : 1;
            for (int i = 0; i < prevOffsetLoop; i++) {
                addCalendarSkipNotTradeDay(prevCalendar, Calendar.DAY_OF_YEAR, prevDelta);
            }
            int prevYear = prevCalendar.get(Calendar.YEAR);
            int prevMonth = prevCalendar.get(Calendar.MONTH) + 1;
            int prevDayOfMonth = prevCalendar.get(Calendar.DAY_OF_MONTH);

            Calendar nextCalendar = (Calendar) getBaseCalendar().clone();
            int nextOffsetLoop = nextOffset < 0 ? -nextOffset : nextOffset;
            int nextDelta = nextOffset < 0 ? -1 : 1;
            for (int i = 0; i < nextOffsetLoop; i++) {
                addCalendarSkipNotTradeDay(nextCalendar, Calendar.DAY_OF_YEAR, nextDelta);
            }
            int nextYear = nextCalendar.get(Calendar.YEAR);
            int nextMonth = nextCalendar.get(Calendar.MONTH) + 1;
            int nextDayOfMonth = nextCalendar.get(Calendar.DAY_OF_MONTH);

            return prevYear + "." + prevMonth + "." + prevDayOfMonth + "日到" + nextYear + "." + nextMonth + "." + nextDayOfMonth + "日";
        }
    }

    public static class BeforeXDayRangePromptStrategy implements PromptStrategy {
        PromptStrategy delegate;

        public BeforeXDayRangePromptStrategy(int x) {
            delegate = new DayRangePromptStrategy(-x, -1);
        }

        @Override
        public String getStrategy() {
            return delegate.getStrategy();
        }
    }

    public static class AfterXDayRangePromptStrategy implements PromptStrategy {
        PromptStrategy delegate;

        public AfterXDayRangePromptStrategy(int x) {
            delegate = new DayRangePromptStrategy(1, x);
        }

        @Override
        public String getStrategy() {
            return delegate.getStrategy();
        }
    }

}
