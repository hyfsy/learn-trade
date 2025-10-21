package com.hyf.trade.application.db.dto;

import com.alibaba.fastjson.JSON;
import com.hyf.trade.client.HttpClient;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class HolidayData {

    public static List<String> get(int year) {
        return getData(year).dates.stream()
                .filter(item -> item.type.equals("public_holiday"))
                .map(item -> item.date.replace("-", "."))
                .collect(Collectors.toList());
    }

    public static HolidayData getData(int year) {
        int maxYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year > maxYear) {
            throw new IllegalArgumentException("year: " + year);
        }
        String url = "https://unpkg.com/holiday-calendar/data/CN/" + year + ".json";
        try {
            String json = HttpClient.getString(url);
            return JSON.parseObject(json, HolidayData.class);
        } catch (IOException e) {
            throw new RuntimeException("Get holiday failed, url: " + url, e);
        }
    }

    private int year;
    private String region;
    private List<HolidayDataItem> dates = new ArrayList<>();

    @Data
    public static class HolidayDataItem {
        private String date; // 2025-01-28, 2025-01-26
        private String name; // 春节, 春节补班
        private String name_cn; // 春节, 春节补班
        private String name_en; // Chinese New Year, Spring Festival Workday
        private String type; // public_holiday, transfer_workday
    }

}
