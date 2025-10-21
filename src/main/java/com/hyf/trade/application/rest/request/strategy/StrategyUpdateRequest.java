package com.hyf.trade.application.rest.request.strategy;

import lombok.Data;

@Data
public class StrategyUpdateRequest {
    private String id;
    private String name;
    private String question;
}
