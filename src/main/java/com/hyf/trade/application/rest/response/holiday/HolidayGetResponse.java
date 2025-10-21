package com.hyf.trade.application.rest.response.holiday;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class HolidayGetResponse {
    private Integer year;
    private List<String> days;

    public List<String> getDays() {
        if (this.days == null) {
            this.days = new ArrayList<>();
        }
        return days;
    }

}
