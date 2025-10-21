package com.hyf.trade.application.rest.response.prompt.replacer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class PromptReplacerTypeListResponse {

    private List<Item> items = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Item {
        private String className;
        private int paramCount;
    }
}
