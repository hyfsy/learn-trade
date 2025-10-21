package com.hyf.trade.application.rest.request.holiday;

import lombok.Data;

@Data
public class HolidayDeleteRequest {
    private Integer year;
    private String day;
}
