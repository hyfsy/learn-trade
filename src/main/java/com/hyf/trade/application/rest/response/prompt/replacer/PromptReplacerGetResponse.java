package com.hyf.trade.application.rest.response.prompt.replacer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PromptReplacerGetResponse {
    private String id;
    private String text;
    private String className;
    private String params; // xxx,xxx String Integer Boolean int boolean
    private int order;
    private boolean embedded;
}
