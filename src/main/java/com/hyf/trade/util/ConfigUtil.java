package com.hyf.trade.util;

import com.alibaba.fastjson.JSON;
import com.hyf.trade.Config;

import java.io.*;

/**
 * @author baB_hyf
 * @date 2025/04/11
 */
public class ConfigUtil {

    private static final boolean jarMode = Boolean.getBoolean("jarMode");

    public static String getStrategy() {
        InputStream resource = getResource("/strategy/strategy.txt");
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
        InputStream resource = getResource("/strategy/config.json");
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

    private static InputStream getResource(String path) {
        if (jarMode) {
            return getResourceFromFileSystem(path);
        }
        else {
            return getResourceFromClassPath(path);
        }
    }

    private static InputStream getResourceFromClassPath(String path) {
        return ConfigUtil.class.getResourceAsStream(path);
    }

    private static InputStream getResourceFromFileSystem(String path) {
        String home = System.getProperty("user.dir");
        File file = new File(home, path);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
    }
}
