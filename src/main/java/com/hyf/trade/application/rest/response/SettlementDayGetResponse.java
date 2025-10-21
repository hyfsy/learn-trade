package com.hyf.trade.application.rest.response;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettlementDayGetResponse {

    private List<SettlementDay> settlementDays;

    public static SettlementDayGetResponse createBy(Map<String, List<String>> settlementDayMaps) {
        SettlementDayGetResponse response = new SettlementDayGetResponse();
        for (Map.Entry<String, List<String>> dayEntry : settlementDayMaps.entrySet()) {
            SettlementDayGetResponse.SettlementDay settlementDay = new SettlementDayGetResponse.SettlementDay();
            settlementDay.setName(dayEntry.getKey());
            settlementDay.setDays(dayEntry.getValue());
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
        private String name;
        private List<String> days;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getDays() {
            if (this.days == null) {
                this.days = new ArrayList<>();
            }
            return days;
        }

        public void setDays(List<String> days) {
            this.days = days;
        }
    }
}
