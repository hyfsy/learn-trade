package com.hyf.trade.application.rest;

import com.hyf.trade.application.db.CalendarTuning;
import com.hyf.trade.application.db.TradeRepository;
import com.hyf.trade.application.db.TradeService;
import com.hyf.trade.application.db.dto.StrategyDTO;
import com.hyf.trade.application.db.entity.*;
import com.hyf.trade.application.rest.request.holiday.HolidayAddRequest;
import com.hyf.trade.application.rest.request.holiday.HolidayDeleteRequest;
import com.hyf.trade.application.rest.request.holiday.HolidayGetRequest;
import com.hyf.trade.application.rest.request.prompt.PromptGenerateRequest;
import com.hyf.trade.application.rest.request.SettlementDayGetRequest;
import com.hyf.trade.application.rest.request.prompt.replacer.*;
import com.hyf.trade.application.rest.request.strategy.*;
import com.hyf.trade.application.rest.response.holiday.HolidayGetResponse;
import com.hyf.trade.application.rest.response.prompt.PromptGenerateResponse;
import com.hyf.trade.application.rest.response.SettlementDayGetResponse;
import com.hyf.trade.application.rest.response.SettlementDayGetResponseV2;
import com.hyf.trade.application.rest.response.prompt.replacer.PromptReplacerGetResponse;
import com.hyf.trade.application.rest.response.prompt.replacer.PromptReplacerListResponse;
import com.hyf.trade.application.rest.response.prompt.replacer.PromptReplacerTypeListResponse;
import com.hyf.trade.application.rest.response.strategy.StrategyGetResponse;
import com.hyf.trade.application.rest.response.strategy.StrategyListResponse;
import com.hyf.trade.settlementday.SettlementDayStrategy;
import com.hyf.trade.util.CalendarUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/trade")
public class RestApi {

    private TradeService tradeService = new TradeService();
    private TradeRepository tradeRepository = TradeRepository.getInstance();

    @RequestMapping("/strategy/get")
    public StrategyGetResponse getStrategy(@RequestBody @Validated StrategyGetRequest request) {
        Strategy strategy = tradeRepository.getStrategy(request.getId());
        return new StrategyGetResponse().setId(strategy.getId()).setName(strategy.getName()).setQuestion(strategy.load());
    }

    @RequestMapping("/strategy/list")
    public StrategyListResponse listStrategy(@RequestBody @Validated StrategyListRequest request) {
        List<Strategy> strategies = tradeRepository.listStrategy();
        if (request.getName() != null && !request.getName().isEmpty()) {
            strategies.removeIf(s -> !s.getName().contains(request.getName()));
        }
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, strategies.size());
        if (end < start) {
            strategies.clear();
        } else {
            strategies = strategies.subList(start, end);
        }
        StrategyListResponse response = new StrategyListResponse();
        for (Strategy strategy : strategies) {
            response.getItems().add(new StrategyListResponse.Item().setId(strategy.getId()).setName(strategy.getName()));
        }
        return response;
    }

    @RequestMapping("/strategy/add")
    public void addStrategy(@RequestBody @Validated StrategyAddRequest request) {
        tradeRepository.saveStrategy(new StrategyDTO().setName(request.getName()).setQuestion(request.getQuestion()));
    }

    @RequestMapping("/strategy/update")
    public void updateStrategy(@RequestBody @Validated StrategyUpdateRequest request) {
        tradeRepository.saveStrategy(new StrategyDTO().setId(request.getId()).setName(request.getName()).setQuestion(request.getQuestion()));
    }

    @RequestMapping("/strategy/delete")
    public void deleteStrategy(@RequestBody @Validated StrategyDeleteRequest request) {
        tradeRepository.deleteStrategy(request.getId());
    }


    // =========================

    @RequestMapping("/replacer/type")
    public PromptReplacerTypeListResponse getPromptReplacerTypes() {
        PromptReplacerTypes[] values = PromptReplacerTypes.values();
        PromptReplacerTypeListResponse response = new PromptReplacerTypeListResponse();
        for (PromptReplacerTypes value : values) {
            response.getItems().add(new PromptReplacerTypeListResponse.Item().setClassName(value.getKlass().getName()).setParamCount(value.getParamCount()));
        }
        return response;
    }

    @RequestMapping("/replacer/get")
    public PromptReplacerGetResponse getPromptReplacer(@RequestBody @Validated PromptReplacerGetRequest request) {
        PromptReplacer replacer;
        if (request.isEmbedded()) {
            replacer = EmbeddedPromptReplacer.get(request.getId());
        } else {
            replacer = tradeRepository.getReplacer(request.getId());
        }
        return new PromptReplacerGetResponse().setId(replacer.getId())
                .setText(replacer.getText())
                .setClassName(replacer.getClassName())
                .setParams(replacer.getParams())
                .setOrder(replacer.getOrder())
                .setEmbedded(replacer.isEmbedded());
    }

    @RequestMapping("/replacer/list")
    public PromptReplacerListResponse listPromptReplacer(@RequestBody @Validated PromptReplacerListRequest request) {
        List<PromptReplacer> results = new ArrayList<>();

        List<PromptReplacer> embeddedReplacers = EmbeddedPromptReplacer.list();
        embeddedReplacers.sort(Comparator.comparingInt(PromptReplacer::getOrder));
        results.addAll(embeddedReplacers);

        List<PromptReplacer> replacers = tradeRepository.listReplacers();
        replacers.sort(Comparator.comparingInt(PromptReplacer::getOrder));
        results.addAll(replacers);

        PromptReplacerListResponse response = new PromptReplacerListResponse();
        for (PromptReplacer replacer : results) {
            response.getItems().add(new PromptReplacerListResponse.Item().setId(replacer.getId())
                    .setText(replacer.getText())
                    .setClassName(replacer.getClassName())
                    .setParams(replacer.getParams())
                    .setOrder(replacer.getOrder())
                    .setEmbedded(replacer.isEmbedded())
            );
        }
        return response;
    }

    @RequestMapping("/replacer/add")
    public void addPromptReplacer(@RequestBody @Validated PromptReplacerAddRequest request) {
        PromptReplacer replacer = new PromptReplacer();
        replacer.setText(request.getText());
        replacer.setClassName(request.getClassName());
        replacer.setParams(request.getParams());
        replacer.setOrder(request.getOrder());
        replacer.setEmbedded(false);
        tradeRepository.saveReplacer(replacer);
    }

    @RequestMapping("/replacer/update")
    public void updatePromptReplacer(@RequestBody @Validated PromptReplacerUpdateRequest request) {
        if (request.isEmbedded()) {
            return;
        }
        PromptReplacer replacer = new PromptReplacer();
        replacer.setId(request.getId());
        replacer.setText(request.getText());
        replacer.setClassName(request.getClassName());
        replacer.setParams(request.getParams());
        replacer.setOrder(request.getOrder());
        replacer.setEmbedded(false);
        tradeRepository.saveReplacer(replacer);
    }

    @RequestMapping("/replacer/delete")
    public void deletePromptReplacer(@RequestBody @Validated PromptReplacerDeleteRequest request) {
        if (request.isEmbedded()) {
            return;
        }
        tradeRepository.deleteReplacer(request.getId());
    }


    // =========================


    @RequestMapping("/holiday/get")
    public HolidayGetResponse getHoliday(@RequestBody @Validated HolidayGetRequest request) {
        Holiday holiday = tradeRepository.getHoliday(request.getYear());
        return new HolidayGetResponse().setYear(holiday.getYear()).setDays(holiday.getDays());
    }

    @RequestMapping("/holiday/add")
    public void addHoliday(@RequestBody @Validated HolidayAddRequest request) {
        Holiday holiday = tradeRepository.getHoliday(request.getYear());
        holiday.add(request.getDay());
        tradeRepository.saveHoliday(holiday);
    }

    @RequestMapping("/holiday/delete")
    public void deleteHoliday(@RequestBody @Validated HolidayDeleteRequest request) {
        Holiday holiday = tradeRepository.getHoliday(request.getYear());
        holiday.remove(request.getDay());
        tradeRepository.saveHoliday(holiday);
    }


    // =========================


    @RequestMapping("/prompt")
    public PromptGenerateResponse generatePrompt(@RequestBody @Validated PromptGenerateRequest request) {
        Map<String, List<String>> prompts = tradeService.generatePrompt(request);
        PromptGenerateResponse response = new PromptGenerateResponse();
        response.setPrompts(prompts);
        return response;
    }

    // 获取当前月合并的
    // 获取今年合并的
    // 获取今年某个的
    // 获取当前月某个的

    @RequestMapping("/settlement-day")
    public SettlementDayGetResponse settlementDay(@RequestBody @Validated SettlementDayGetRequest request) {
        Map<String, List<String>> settlementDayMaps = getSettlementDayMaps(request.getYear());
        filterSettlementDays(settlementDayMaps, request.getMonth(), request.getName());
        return SettlementDayGetResponse.createBy(settlementDayMaps);
    }

    @RequestMapping("/settlement-day/v2")
    public SettlementDayGetResponseV2 settlementDayV2(@RequestBody @Validated SettlementDayGetRequest request) {
        Map<String, List<String>> settlementDayMaps = getSettlementDayMaps(request.getYear());
        filterSettlementDays(settlementDayMaps, request.getMonth(), request.getName());
        return SettlementDayGetResponseV2.createBy(settlementDayMaps);
    }


    private Map<String, List<String>> getSettlementDayMaps(int year) {
        CalendarTuning calendarTuning = CalendarTuning.create().year(year);

        Map<String, List<String>> settlementDayMaps = new LinkedHashMap<>();

        Holiday holiday = tradeRepository.getHoliday(year);

        List<SettlementDayStrategy> strategies = SettlementDayStrategy.getStrategies();
        for (SettlementDayStrategy strategy : strategies) {
            List<String> settlementDays = strategy.getSettlementDays(calendarTuning.copyAndGet());
            settlementDays.removeAll(holiday.getDays());
            settlementDayMaps.put(strategy.getName(), settlementDays);
        }

        return settlementDayMaps;
    }

    private void filterSettlementDays(Map<String, List<String>> settlementDayMaps, Integer month, String name) {
        if (month == null && name == null) {
            return;
        }
        if (month != null) {
            for (Map.Entry<String, List<String>> dayEntry : settlementDayMaps.entrySet()) {
                List<String> days = dayEntry.getValue();
                days.removeIf(day -> CalendarUtil.parse_yyyy_MM_dd(day).get(Calendar.MONTH) != (month - 1));
            }
        }
        if (name != null) {
            List<String> needRemoveNames = new ArrayList<>(settlementDayMaps.keySet());
            needRemoveNames.remove(name);
            for (String nrName : needRemoveNames) {
                settlementDayMaps.remove(nrName);
            }
        }
    }
}
