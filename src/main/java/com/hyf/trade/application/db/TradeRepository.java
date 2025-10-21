package com.hyf.trade.application.db;

import com.alibaba.fastjson.JSON;
import com.hyf.trade.application.Constants;
import com.hyf.trade.application.db.dto.HolidayData;
import com.hyf.trade.application.db.dto.StrategyDTO;
import com.hyf.trade.application.db.entity.Holiday;
import com.hyf.trade.application.db.entity.Strategy;
import com.hyf.trade.application.db.entity.PromptReplacer;
import com.hyf.trade.application.util.FileUtil;
import com.hyf.trade.util.ZipUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TradeRepository {

    private static final String CONFIG_FILE = Constants.HOME + File.separator + "trade.config";

    private static final TradeRepository instance = new TradeRepository();

    private TradeConfig tradeConfig = new TradeConfig();

    private TradeRepository() {
        reload();
    }

    public static TradeRepository getInstance() {
        return instance;
    }

    public Strategy getStrategy(String id) {
        assertNotNull(id);
        for (Strategy strategy : tradeConfig.getStrategies()) {
            if (Objects.equals(strategy.getId(), id)) {
                return strategy;
            }
        }
        throw new DataNotFoundException(id);
    }

    public String getPrompt(String id) {
        assertNotNull(id);
        Strategy strategy = getStrategy(id);
        return strategy.load();
    }

    public List<Strategy> listStrategy() {
        return new ArrayList<>(tradeConfig.getStrategies());
    }

    public void saveStrategy(StrategyDTO strategyDTO) {
        Strategy strategy;
        if (strategyDTO.getId() == null) {
            strategy = new Strategy();
            strategy.init();
            tradeConfig.getStrategies().add(strategy);
        } else {
            strategy = getStrategy(strategyDTO.getId());
        }
        strategy.setName(strategyDTO.getName());
        strategy.store(strategyDTO.getQuestion());
        store();
    }

    public void deleteStrategy(String id) {
        assertNotNull(id);
        for (Strategy strategy : tradeConfig.getStrategies()) {
            if (strategy.getId().equals(id)) {
                tradeConfig.getStrategies().remove(strategy);
                strategy.destroy();
                store();
                break;
            }
        }
    }

    public PromptReplacer getReplacer(String id) {
        assertNotNull(id);
        for (PromptReplacer replacer : tradeConfig.getReplacers()) {
            if (Objects.equals(replacer.getId(), id)) {
                return replacer;
            }
        }
        throw new DataNotFoundException(id);
    }

    public void saveReplacer(PromptReplacer newReplacer) {
        PromptReplacer replacer;
        if (newReplacer.getId() == null) {
            replacer = new PromptReplacer();
            replacer.init();
            tradeConfig.getReplacers().add(replacer);
        } else {
            replacer = getReplacer(newReplacer.getId());
        }
        replacer.setText(newReplacer.getText());
        replacer.setClassName(replacer.getClassName());
        replacer.setParams(newReplacer.getParams());
        replacer.setOrder(newReplacer.getOrder());
        store();
    }

    public void deleteReplacer(String id) {
        assertNotNull(id);
        for (PromptReplacer replacer : tradeConfig.getReplacers()) {
            if (replacer.getId().equals(id)) {
                tradeConfig.getReplacers().remove(replacer);
                store();
                break;
            }
        }
    }

    public List<PromptReplacer> listReplacers() {
        return new ArrayList<>(tradeConfig.getReplacers());
    }

    private Holiday syncHoliday(Integer year) {
        if (year > CalendarTuning.create().year()) {
            throw new IllegalArgumentException("year is coming");
        }
        Holiday holiday = getHoliday(year);
        if (!holiday.getSynced()) {
            List<String> days = HolidayData.get(year);
            for (String day : days) {
                holiday.add(day);
            }
            holiday.setSynced(true);
            saveHoliday(holiday);
        }
        return holiday;
    }

    public Holiday getHoliday(Integer year) {
        assertNotNull(year);
        for (Holiday holiday : tradeConfig.getHolidays()) {
            if (Objects.equals(holiday.getYear(), year)) {
                return holiday;
            }
        }
        Holiday holiday = new Holiday();
        holiday.setYear(year);
        tradeConfig.getHolidays().add(holiday);
        syncHoliday(holiday.getYear());
        return holiday;
    }

    public void saveHoliday(Holiday newHoliday) {
        Holiday holiday = getHoliday(newHoliday.getYear());
        holiday.setDays(newHoliday.getDays());
        store();
    }

    public List<Holiday> listHolidays() {
        return new ArrayList<>(tradeConfig.getHolidays());
    }

    public void _import(InputStream is) throws IOException {
        ZipUtil.unzip(is, Constants.HOME);
        reload();
    }

    public InputStream _export() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipUtil.zip(Constants.HOME, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private void store() {
        FileUtil.write(CONFIG_FILE, JSON.toJSONString(tradeConfig));
    }

    private void reload() {
        tradeConfig = load();
    }

    private TradeConfig load() {
        TradeConfig tradeConfig = JSON.parseObject(FileUtil.read(CONFIG_FILE), TradeConfig.class);
        return tradeConfig == null ? new TradeConfig() : tradeConfig;
    }
    private void assertNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }
}
