package com.hyf.trade.application.rest.request.prompt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PromptGenerateRequest {

    private List<String> ids = new ArrayList<>();
    private String start;
    private String end;

}
