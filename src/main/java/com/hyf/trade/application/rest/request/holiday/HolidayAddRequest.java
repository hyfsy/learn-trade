package com.hyf.trade.application.rest.request.holiday;

import lombok.Data;

@Data
public class HolidayAddRequest {
    private Integer year;
    private String day;
}
