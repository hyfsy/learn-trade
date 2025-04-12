package com.hyf.trade.settlementday;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author baB_hyf
 * @date 2025/04/12
 */
public interface SettlementDayStrategy {

    static List<SettlementDayStrategy> getStrategies() {
        ServiceLoader<SettlementDayStrategy> strategies = ServiceLoader.load(SettlementDayStrategy.class);
        List<SettlementDayStrategy> settlementDayStrategies = new ArrayList<>();
        strategies.forEach(settlementDayStrategies::add);
        return settlementDayStrategies;
    }

    String getName();

    List<String> getSettlementDays();
}
