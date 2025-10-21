package com.hyf.trade.application.rest.response.strategy;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StrategyGetResponse {
    private String id;
    private String name;
    private String question;
}
