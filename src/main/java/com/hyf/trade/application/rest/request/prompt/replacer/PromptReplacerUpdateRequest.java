package com.hyf.trade.application.rest.request.prompt.replacer;

import lombok.Data;

@Data
public class PromptReplacerUpdateRequest {
    private String id;
    private String text;
    private String className;
    private String params; // xxx,xxx String Integer Boolean int boolean
    private int order;
    private boolean embedded;
}
