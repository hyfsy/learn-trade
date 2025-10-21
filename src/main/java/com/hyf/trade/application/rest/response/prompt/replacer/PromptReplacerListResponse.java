package com.hyf.trade.application.rest.response.prompt.replacer;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
public class PromptReplacerListResponse {

    private List<Item> items = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Item {
        private String id;
        private String text;
        private String className;
        private String params; // xxx,xxx String Integer Boolean int boolean
        private int order;
        private boolean embedded;
    }
}
