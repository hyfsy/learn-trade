package com.hyf.trade.application.rest.response.prompt;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PromptGenerateResponse {

    private Map<String, List<String>> prompts;

}
