package com.geominfo.mlsql.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: geometry-mlsql
 * @description: 文件处理工具类
 * @author: BJZ
 * @create: 2020-07-09 16:19
 * @version: 1.0.0
 */
public class FileUtil {

    public static long sizeOfDirectory(File directory) {
        String message;
        if (!directory.exists()) {
            message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        } else if (!directory.isDirectory()) {
            message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        } else {
            long size = 0L;
            File[] files = directory.listFiles();
            if (files == null) {
                return 0L;
            } else {
                File[] arr$ = files;
                int len$ = files.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    File file = arr$[i$];
                    size += sizeOf(file);
                }

                return size;
            }
        }
    }


    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);

            try {
                IOUtils.copy(source, output);
                output.close();
            } finally {
                IOUtils.closeQuietly(output);
            }
        } finally {
            IOUtils.closeQuietly(source);
        }

    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }

        return new FileOutputStream(file, append);
    }


    private static long sizeOf(File file) {
        if (!file.exists()) {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        } else {
            return file.isDirectory() ? sizeOfDirectory(file) : file.length();
        }
    }






}