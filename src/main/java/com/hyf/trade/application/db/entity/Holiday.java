package com.hyf.trade.application.db.entity;

import com.hyf.trade.util.CalendarUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
public class Holiday {

    private Boolean synced;
    private Integer year;
    private List<String> days = new ArrayList<>();

    public List<String> getDays() {
        return new ArrayList<>(days);
    }

    public void add(String day) {
        days.add(day);
    }
    public void remove(String day) {
        days.remove(day);
    }

    public boolean match(String day) {
        return days.contains(day);
    }

    public boolean match(Calendar calendar) {
        return match(CalendarUtil.to_yyyy_MM_dd(calendar));
    }
}
