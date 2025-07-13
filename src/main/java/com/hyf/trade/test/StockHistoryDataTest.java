package com.hyf.trade.test;

import com.hyf.hotrefresh.common.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author baB_hyf
 * @date 2025/07/13
 */
public class StockHistoryDataTest {

    // https://d.10jqka.com.cn/v6/line/33_000736/01/last1800.js?hexin-v=A6rjdGA7thY4rTp6EPh5oxux-xtJGztuII6j5zQ2VsmIfESFHKt-hfAv8ocH
    public static void main(String[] args) throws IOException {
        try (InputStream is = StockHistoryDataTest.class.getClassLoader().getResourceAsStream("a.txt")) {
            String content = IOUtils.readAsString(is);
            String[] days = content.split(";");
            days[0] = days[0].replace("\"", "");
            days[days.length - 1] = days[days.length - 1].replace("\"", "");
            for (String day : days) {
                String[] data = day.split(",");
                String date = data[0];
                String openPrice = data[1];
                String highPrice = data[2];
                String lowPrice = data[3];
                String closePrice = data[4];
                String volume = data[5];
                String money = data[6];
                String amplitude = data[7];
                String d8 = data[8]; // ?
                String d9 = data[9]; // ?
                String d10 = data[10]; // ?
                System.out.println();
            }

        }
    }
}
