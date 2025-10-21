package com.hyf.trade.application.rest.request.prompt.replacer;

import lombok.Data;

@Data
public class PromptReplacerGetRequest {
    private String id;
    private boolean embedded;
}
