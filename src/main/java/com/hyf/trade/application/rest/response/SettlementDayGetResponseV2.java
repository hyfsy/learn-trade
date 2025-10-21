package com.hyf.trade.application.rest.response;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SettlementDayGetResponseV2 {

    private List<SettlementDay> settlementDays;

    public static SettlementDayGetResponseV2 createBy(Map<String, List<String>> settlementDayMaps) {
        SettlementDayGetResponseV2 response = new SettlementDayGetResponseV2();
        Map<String, List<String>> settlementDayMapsV2 = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> dayEntry : settlementDayMaps.entrySet()) {
            for (String day : dayEntry.getValue()) {
                settlementDayMapsV2.putIfAbsent(day, new ArrayList<>());
                settlementDayMapsV2.get(day).add(dayEntry.getKey());
            }
        }
        for (Map.Entry<String, List<String>> dayEntry : settlementDayMapsV2.entrySet()) {
            SettlementDayGetResponseV2.SettlementDay settlementDay = new SettlementDayGetResponseV2.SettlementDay();
            settlementDay.setDay(dayEntry.getKey());
            settlementDay.setNames(dayEntry.getValue());
            response.getSettlementDays().add(settlementDay);
        }
        return response;
    }

    public List<SettlementDay> getSettlementDays() {
        if (this.settlementDays == null) {
            this.settlementDays = new ArrayList<>();
        }
        return settlementDays;
    }

    public void setSettlementDays(List<SettlementDay> settlementDays) {
        this.settlementDays = settlementDays;
    }

    public static class SettlementDay {
        private String day;
        private List<String> names;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<String> getNames() {
            if (this.names == null) {
                this.names = new ArrayList<>();
            }
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }
    }
}
