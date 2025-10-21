package com.hyf.trade.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipOutputStream;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class ZipUtil {

    public static void zip(String sourcePath, OutputStream os) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(os);
             ZipOutputStream zos = new ZipOutputStream(bos, StandardCharsets.UTF_8)) {

            Path source = Paths.get(sourcePath);
            if (!Files.exists(source)) {
                throw new FileNotFoundException("源文件或目录不存在: " + sourcePath);
            }

            String root = source.getFileName().toString();
            zipDirectory(source, source, zos, root);
        }
    }

    /**
     * 递归压缩目录
     */
    private static void zipDirectory(Path rootPath, Path currentPath, ZipOutputStream zos, String archiveRoot) throws IOException {
        if (Files.isDirectory(currentPath)) {
            Files.list(currentPath).forEach(child -> {
                try {
                    String relativePath = archiveRoot + "/" + currentPath.relativize(child).toString();
                    if (Files.isDirectory(child)) {
                        zos.putNextEntry(new ZipEntry(relativePath + "/"));
                        zos.closeEntry();
                        zipDirectory(rootPath, child, zos, archiveRoot);
                    } else {
                        zos.putNextEntry(new ZipEntry(relativePath));
                        Files.copy(child, zos);
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } else {
            // 是文件
            zos.putNextEntry(new ZipEntry(archiveRoot));
            Files.copy(currentPath, zos);
            zos.closeEntry();
        }
    }

    public static void unzip(InputStream is, String destPath) throws IOException {
        Path dest = Paths.get(destPath);
        Files.createDirectories(dest);

        try (BufferedInputStream bis = new BufferedInputStream(is);
             ZipInputStream zis = new ZipInputStream(bis, StandardCharsets.UTF_8)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entryPath = dest.resolve(entry.getName()).normalize();

                // 防止路径穿越（安全检查）
                if (!entryPath.startsWith(dest)) {
                    throw new IOException("压缩包包含非法路径: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }

}


