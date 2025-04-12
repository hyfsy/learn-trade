package com.hyf.trade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class Config {

    private int          loop = 10;
    private String       base = "+1";
    private List<String> holiday = new ArrayList<>();
    private List<String> black = new ArrayList<>();

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<String> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<String> holiday) {
        this.holiday = holiday;
    }

    public List<String> getBlack() {
        return black;
    }

    public void setBlack(List<String> black) {
        this.black = black;
    }
}
