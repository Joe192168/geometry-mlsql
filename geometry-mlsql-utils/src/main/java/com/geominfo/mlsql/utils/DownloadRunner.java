package com.geominfo.mlsql.utils;


import com.geominfo.mlsql.constants.Constants;
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
 * @description: 文件下载工具类
 * @author: BJZ
 * @create: 2020-07-09 14:43
 * @version: 1.0.0
 */
public class DownloadRunner {

   static Logger  logger = LoggerFactory.getLogger(DownloadRunner.class);

    private static final String HEADER_KEY = "Content-Disposition";
    private static final String HEADER_VALUE = "attachment; filename=";


    public static void iteratorFiles(String path, List<File> files) {
        File p = new File(path);
        if (p.exists()) {
            if (p.isFile()) {
                files.add(p);
            } else if (p.isDirectory()) {
                File[] fileStatusArr = p.listFiles();
                if (fileStatusArr != null && fileStatusArr.length > Constants.ZERO) {
                    for (File file : fileStatusArr) {
                        iteratorFiles(file.getPath(), files);
                    }

                }
            }
        }
    }

    public static int getTarFileByTarFile(HttpServletResponse response, String pathStr) throws UnsupportedEncodingException {

        String[] fileChunk = pathStr.split(Constants.HTTP_SEPARATED);
        response.setContentType(Constants.APPLICATION_OCTET_STREAM);
        //response.setHeader("Transfer-Encoding", "chunked");
        response.setHeader(HEADER_KEY, HEADER_VALUE +
                Constants.THE_BACKSLASH + URLEncoder.encode(fileChunk[fileChunk.length - Constants.ONE],
                Constants.ENCODE) + Constants.THE_BACKSLASH);

        try {
            org.apache.commons.io.IOUtils.copyLarge(new FileInputStream(new File(pathStr)), response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.FIVE_HUNDRED;

        }
        return Constants.TOW_HUNDRED;
    }

    public static int getTarFileByPath(HttpServletResponse response, String pathStr) throws UnsupportedEncodingException {

        String[] fileChunk = pathStr.split(Constants.HTTP_SEPARATED);
        response.setContentType(Constants.APPLICATION_OCTET_STREAM);
        //response.setHeader("Transfer-Encoding", "chunked");
        response.setHeader(HEADER_KEY, HEADER_VALUE + Constants.THE_BACKSLASH +
                URLEncoder.encode(fileChunk[fileChunk.length - Constants.ONE] + Constants.TAR,
                        Constants.ENCODE) + Constants.THE_BACKSLASH);


        try {
            OutputStream outputStream = response.getOutputStream();
            ArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
            List<File> files = new ArrayList<File>();
            iteratorFiles(pathStr, files);

            if (files.size() > Constants.ZERO) {
                InputStream inputStream = null;
                int len = files.size();
                int i = Constants.ONE;
                for (File cur : files) {
                    logger.info(Constants.LEFT_BRACKETS + i++
                            + Constants.HTTP_SEPARATED + len + Constants.RIGHT_BRACKETS +
                            Constants.READ_FILE + cur.getPath() +
                            Constants.ENTRY_NAME + fileChunk[fileChunk.length - Constants.ONE] + cur.getPath()
                            .substring(pathStr.length()));

                    inputStream = new FileInputStream(cur);
                    ArchiveEntry entry = tarOutputStream.createArchiveEntry(cur,
                            fileChunk[fileChunk.length - Constants.ONE] + cur.getPath().substring(pathStr.length()));
                    tarOutputStream.putArchiveEntry(entry);
                    org.apache.commons.io.IOUtils.copyLarge(inputStream, tarOutputStream);
                    tarOutputStream.closeArchiveEntry();
                }
                tarOutputStream.flush();
                tarOutputStream.close();
                return Constants.TOW_HUNDRED;
            } else return Constants.FOUR_HUNDRED;

        } catch (Exception e) {
            e.printStackTrace();
            return Constants.FIVE_HUNDRED;

        }
    }
}
