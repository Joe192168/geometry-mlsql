package com.geominfo.mlsql.service.file;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * @program: geometry-mlsql
 * @description: 文件处理服务器类接口
 * @author: BZJ
 * @create: 2020-07-14 10:33
 * @version: 1.0.0
 */
@Service
public interface FileService {

    <T> T formUpload(HttpServletRequest request, String owner,String targetPath) throws Exception;

    <T> T dowonloadToLocal(HttpServletRequest request) throws Exception;

    <T> T skipEngine(String fromPath, String owner) throws ExecutionException, InterruptedException;

    <T> T download(HttpServletResponse response, Map<String, Object> paramMap);

    <T> T publicDownload(Object o, HttpServletResponse response, String owner);

    <T> T getMlsqlEngine(String userName);
}