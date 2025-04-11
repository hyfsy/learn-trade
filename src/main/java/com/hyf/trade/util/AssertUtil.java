package com.hyf.trade.util;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class AssertUtil {

    public static void notBlank(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("text is blank");
        }

    }
}
