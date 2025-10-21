package com.hyf.trade.application.rest.response.strategy;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
public class StrategyListResponse {

    private List<Item> items = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Item {
        private String id;
        private String name;
    }
}
