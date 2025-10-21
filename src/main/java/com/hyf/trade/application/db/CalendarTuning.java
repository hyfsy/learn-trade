package com.hyf.trade.application.db;

import com.hyf.trade.application.db.entity.Holidays;
import com.hyf.trade.util.CalendarUtil;

import java.util.Calendar;

public class CalendarTuning {

    private final Calendar calendar;
    private CalendarSkipJudge skipJudge = new HolidaySkipJudge(new Holidays());

    private CalendarTuning(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("calendar is null");
        }
        this.calendar = calendar;
    }

    public static CalendarTuning create() {
        return new CalendarTuning(Calendar.getInstance());
    }

    public static CalendarTuning with(String calendar) {
        return new CalendarTuning(CalendarUtil.parse_yyyy_MM_dd(calendar));
    }

    public static CalendarTuning with(Calendar calendar) {
        return new CalendarTuning(calendar);
    }

    public CalendarTuning copy() {
        return CalendarTuning.with(CalendarUtil.copy(calendar)).config(skipJudge);
    }

    public Calendar copyAndGet() {
        return copy().get();
    }

    public Calendar get() {
        return calendar;
    }

    public boolean isYear(int year) {
        return year() == year;
    }

    public boolean isMonth(int month) {
        return month() == month;
    }

    public boolean isDay(int day) {
        return day() == day;
    }

    public int year() {
        return calendar.get(Calendar.YEAR);
    }

    public int month() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int day() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public CalendarTuning year(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    public CalendarTuning month(int month) {
        calendar.set(Calendar.MONTH, month - 1);
        // int oldMonth = calendar.get(Calendar.MONTH);
        // int delta = month > oldMonth ? 1 : -1;
        // calendar.set(Calendar.MONTH, month - 1);
        // while (needSkip()) {
        //     calendar.add(Calendar.MONTH, delta);
        // }
        return this;
    }

    public CalendarTuning day(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return this;
    }

    public CalendarTuning yearAdd(int year) {
        calendar.add(Calendar.YEAR, year);
        return this;
    }

    public CalendarTuning monthAdd(int month) {
        calendar.add(Calendar.MONTH, month);
        return this;
    }

    public CalendarTuning dayAdd(int day) {
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return this;
    }

    public CalendarTuning config(CalendarSkipJudge skipJudge) {
        this.skipJudge = skipJudge;
        return this;
    }

    public CalendarTuning notSkipHoliday() {
        return config(null);
    }

    public CalendarTuning skipHoliday() {
        return config(new HolidaySkipJudge(new Holidays()));
    }

    public CalendarTuning rollYear(int amount) {
        roll(Calendar.YEAR, amount);
        return this;
    }

    public CalendarTuning rollMonth(int amount) {
        roll(Calendar.MONTH, amount);
        return this;
    }

    public CalendarTuning rollDay(int amount) {
        roll(Calendar.DAY_OF_YEAR, amount);
        return this;
    }

    public String to_yyyy_MM_dd() {
        return CalendarUtil.to_yyyy_MM_dd(calendar);
    }

    public String to_simple_yyyy_MM_dd() {
        return CalendarUtil.to_simple_yyyy_MM_dd(calendar);
    }

    private void roll(int field, int amount) {
        int time = amount < 0 ? -amount : amount;
        int delta = amount < 0 ? -1 : 1;

        while (time > 0) {
            calendar.add(field, delta);
            if (needSkip()) {
                continue;
            }
            time--;
        }
    }

    private boolean needSkip() {
        return skipJudge.needSkip(calendar);
    }

    public int compare_yyyy_MM_dd(CalendarTuning other) {
        return CalendarUtil.max(to_yyyy_MM_dd(), other.to_yyyy_MM_dd());
    }

    public boolean equals_yyyy_MM_dd(CalendarTuning other) {
        return compare_yyyy_MM_dd(other) == 0;
    }
}
