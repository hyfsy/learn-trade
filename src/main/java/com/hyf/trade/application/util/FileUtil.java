package com.hyf.trade.application.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtil {

    public static String read(String f) {
        return read(new File(f));
    }

    public static String read(File f) {
        File file = getFile(f);
        try {
            return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Read file failed", e);
        }
    }

    public static void write(String f, String content) {
        write(new File(f), content);
    }

    public static void write(File f, String content) {
        File file = getFile(f);
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Write file failed", e);
        }

    }

    private static File getFile(File file) {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new RuntimeException("Create file mkdir false: " + file.getAbsolutePath());
                }
            }
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Create file return false: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Create file failed: " + file.getAbsolutePath());
            }
            if (file.getName().endsWith(".json")) {
                try {
                    Files.write(file.toPath(), "{}".getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    throw new RuntimeException("Init file failed", e);
                }
            }
        }
        return file;
    }

    public static void delete(File file) {
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Delete file failed: " + file.getAbsolutePath());
            }
        }
    }
}
