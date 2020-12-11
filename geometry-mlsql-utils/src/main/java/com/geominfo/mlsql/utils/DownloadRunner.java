package com.geominfo.mlsql.utils;


import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description:  文件下载
 * @author: BJZ
 * @create: 2020-11-09 14:43
 * @version: 1.0.0
 */
public class DownloadRunner {

    static Logger logger = LoggerFactory.getLogger(DownloadRunner.class);

    private static final String HEADER_KEY = "Content-Disposition";
    private static final String HEADER_VALUE = "attachment; filename=";


    public static void iteratorFiles(String path, List<File> files) {
        File p = new File(path);
        if (p.exists()) {
            if (p.isFile()) {
                files.add(p);
            } else if (p.isDirectory()) {
                File[] fileStatusArr = p.listFiles();
                if (fileStatusArr != null && fileStatusArr.length > 0) {
                    for (File file : fileStatusArr) {
                        iteratorFiles(file.getPath(), files);
                    }

                }
            }
        }
    }

    public static int getTarFileByPath(HttpServletResponse response, String pathStr) {

        String[] fileChunk = pathStr.split("/");
        response.setContentType("application/octet-stream");
        //response.setHeader("Transfer-Encoding", "chunked");

        try {
            response.setHeader(HEADER_KEY,
                    HEADER_VALUE + "\"" +
                            URLEncoder.encode(fileChunk[fileChunk.length - 1] + ".tar", "utf-8") + "\"");

            OutputStream outputStream = response.getOutputStream();
            ArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            List<File> files = new ArrayList<>();
            iteratorFiles(pathStr, files);

            if (files.size() > 0) {
                int len = files.size();
                int i = 1;
                for (File cur : files) {
                    logger.info("[" + i++ + "/" + len + "]" + ",读取文件" + cur.getPath() + " entryName:" + fileChunk[fileChunk.length - 1] + cur.getPath().substring(pathStr.length()));
                    InputStream inputStream = new FileInputStream(cur);
                    ArchiveEntry entry = tarOutputStream.createArchiveEntry(cur, fileChunk[fileChunk.length - 1] + cur.getPath().substring(pathStr.length()));
                    tarOutputStream.putArchiveEntry(entry);
                    org.apache.commons.io.IOUtils.copyLarge(inputStream, tarOutputStream);
                    tarOutputStream.closeArchiveEntry();
                }
                tarOutputStream.flush();
                tarOutputStream.close();
            } else return 500;

        } catch (Exception e) {
            e.printStackTrace();
            return 500;

        }

        return 200;
    }
}
