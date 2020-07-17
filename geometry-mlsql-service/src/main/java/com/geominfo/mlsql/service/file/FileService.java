package com.geominfo.mlsql.service.file;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: geometry-mlsql
 * @description: 文件处理服务器类接口
 * @author: BZJ
 * @create: 2020-07-14 10:33
 * @version: 1.0.0
 */
@Service
public interface FileService<T> {

    T formUpload(HttpServletRequest request) ;
    T download(Object o, HttpServletResponse response) ;
    T publicDownload(Object o , HttpServletResponse response) ;
}