package com.hyf.trade.application.db.entity;

import com.hyf.trade.strategy.PromptStrategy;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.UUID;

@Data
public class PromptReplacer {

    private String id;
    private String text;
    private String className;
    private String params; // xxx,xxx String Integer Boolean int boolean
    private int order;
    private boolean embedded;

    public void init() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public PromptStrategy createPromptStrategy() {
        try {
            Class<?> klass = Class.forName(className);
            Constructor<?> constructor = klass.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 0) {
                return (PromptStrategy) constructor.newInstance();
            }
            String[] paramArray = params.split(",");
            if (paramArray.length != parameterTypes.length) {
                throw new IllegalArgumentException("PromptStrategy parameter count mismatch, params: " + params + ", types: " + Arrays.toString(parameterTypes));
            }
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                String param = paramArray[i];
                if (parameterType == String.class) {
                    args[i] = param;
                } else if (parameterType == Integer.class) {
                    args[i] = Integer.valueOf(param);
                } else if (parameterType == int.class) {
                    args[i] = Integer.parseInt(param);
                } else if (parameterType == Boolean.class) {
                    args[i] = Boolean.valueOf(param);
                } else if (parameterType == boolean.class) {
                    args[i] = Boolean.parseBoolean(param);
                }
            }
            return (PromptStrategy) constructor.newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException("Create PromptStrategy failed, className" + className, e);
        }
    }
}
