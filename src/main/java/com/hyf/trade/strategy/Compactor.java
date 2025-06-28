package com.hyf.trade.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author baB_hyf
 * @date 2025/06/28
 */
public class Compactor {

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}\\.\\d{1,2}\\.\\d{1,2}日");

    public static String compact(String strategy) {

        // strategy = strategy.replace("日", ""); // 5日均线情况
        strategy = strategy.replace("的", "");
        strategy = strategy.replace("不", "!");
        strategy = strategy.replace("大于", ">");
        strategy = strategy.replace("小于", "<");
        strategy = strategy.replace("等于", "=");

        StringBuffer sb = new StringBuffer();
        Matcher matcher = DATE_PATTERN.matcher(strategy);
        while (matcher.find()) {
            String group = matcher.group();
            String[] groupArray = group.split("\\.");
            String year = groupArray[0];
            String month = groupArray[1];
            String date = groupArray[2];
            month = month.length() == 1 ? "0" + month : month;
            date = date.replace("日", "");
            date = date.length() == 1 ? "0" + date : date;
            matcher.appendReplacement(sb, year + month + date);
        }
        matcher.appendTail(sb);
        strategy = sb.toString();

        return strategy;
    }
}
