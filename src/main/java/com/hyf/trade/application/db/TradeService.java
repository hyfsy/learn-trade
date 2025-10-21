package com.hyf.trade.application.db;

import com.hyf.trade.application.Constants;
import com.hyf.trade.application.rest.request.prompt.PromptGenerateRequest;
import com.hyf.trade.strategy.factory.PromptStrategyFactory;
import com.hyf.trade.util.StrategyUtil;

import java.util.*;

public class TradeService {

    private final TradeRepository tradeRepository = TradeRepository.getInstance();

    private final PromptStrategyFactory promptStrategyFactory = new PromptReplacerPromptStrategyFactory(tradeRepository);

    public Map<String, List<String>> generatePrompt(PromptGenerateRequest request) {
        if (request.getIds() == null) {
            return new LinkedHashMap<>();
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (String id : request.getIds()) {
            String prompt = tradeRepository.getPrompt(id);
            if (prompt == null) {
                result.put(id, new ArrayList<>());
                continue;
            }

            List<Calendar> calendars = getCalendarsBetween(request.getEnd(), request.getStart());

            List<String> strategies = new ArrayList<>();
            for (Calendar calendar : calendars) {
                String transformedPrompt = StrategyUtil.transformPrompt(promptStrategyFactory, prompt, calendar);
                strategies.add(transformedPrompt);
            }

            result.put(id, strategies);
        }

        return result;
    }

    private List<Calendar> getCalendarsBetween(String start, String end) {
        List<Calendar> calendars = new ArrayList<>();
        CalendarTuning endCalendar;
        if (end == null || end.isEmpty()) {
            endCalendar = CalendarTuning.create().dayAdd(Constants.BASE);
        }
        else {
            endCalendar = CalendarTuning.with(end);
        }
        CalendarTuning baseCalendar;
        if (start == null || start.isEmpty()) {
            baseCalendar = endCalendar.copy().rollDay(-Constants.LOOP);
        }
        else {
            baseCalendar = CalendarTuning.with(start);
        }

        while (baseCalendar.compare_yyyy_MM_dd(endCalendar) <= 0) {
            calendars.add(baseCalendar.copyAndGet());
            baseCalendar.rollDay(1);
        }

        return calendars;
    }

}
