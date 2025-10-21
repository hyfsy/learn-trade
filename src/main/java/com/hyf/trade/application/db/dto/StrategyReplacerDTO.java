package com.hyf.trade.application.db.dto;

import lombok.Data;

@Data
public class StrategyReplacerDTO {

    private String id;
    private String text;
    private String className;
    private String params;
    private int order;

}
