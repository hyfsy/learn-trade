package com.hyf.ths.util;

import com.alibaba.fastjson.JSON;
import com.hyf.ths.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class ConfigUtil {

    public static String getStrategy() {
        InputStream resource = ConfigUtil.class.getResourceAsStream("/strategy/strategy.txt");
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(resource))) {
            String row;
            StringBuilder sb = new StringBuilder();
            while ((row = reader.readLine()) != null) {
                String trimmedText = row.trim();
                if (trimmedText.isEmpty()) {
                    continue;
                }
                sb.append(trimmedText).append("，");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("read strategy file failed");
        }
    }

    public static Config getConfig() {
        InputStream resource = ConfigUtil.class.getResourceAsStream("/strategy/config.json");
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(resource))) {
            String row;
            StringBuilder sb = new StringBuilder();
            while ((row = reader.readLine()) != null) {
                sb.append(row);
            }
            String json = sb.toString();
            return JSON.parseObject(json, Config.class);
        } catch (IOException e) {
            throw new RuntimeException("read strategy file failed");
        }
    }

}
