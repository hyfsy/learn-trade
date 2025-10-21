package com.hyf.trade.application.rest.request;


public class SettlementDayGetRequest {

    // @NotNull
    private Integer year;
    private Integer month;
    private String name;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
