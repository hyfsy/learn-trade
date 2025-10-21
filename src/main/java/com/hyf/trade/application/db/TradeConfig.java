package com.hyf.trade.application.db;

import com.hyf.trade.application.db.entity.Holiday;
import com.hyf.trade.application.db.entity.Strategy;
import com.hyf.trade.application.db.entity.PromptReplacer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TradeConfig {

    private String offset; // 页面默认初始值
    private String base; // 页面默认初始值
    private List<Strategy> strategies = new ArrayList<>();
    private List<PromptReplacer> replacers = new ArrayList<>();
    private List<Holiday> holidays = new ArrayList<>();
    private List<String> supportSettlementDayNames = new ArrayList<>();

}
