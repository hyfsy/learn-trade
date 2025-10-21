package com.hyf.trade.application.db.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StrategyDTO {

    private String id;
    private String name;
    private String question;

}
