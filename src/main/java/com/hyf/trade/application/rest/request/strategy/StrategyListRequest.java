package com.hyf.trade.application.rest.request.strategy;

import lombok.Data;

@Data
public class StrategyListRequest {
    private String name;
    private int pageNum = 1;
    private int pageSize = Integer.MAX_VALUE;
}
