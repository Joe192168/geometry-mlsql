package com.geominfo.mlsql.controller.file;

import com.geominfo.mlsql.config.restful.CustomException;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.file.FileService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: geometry-mlsql
 * @description: 文件处理控制类
 * @author: BJZ
 * @create: 2020-07-09 09:24
 * @version: 1.0.0
 */
@RestController
@Api(value = "文件处理接口", tags = {"文件处理接口"})
@Log4j2
public class FileController extends BaseController {

    Logger logger = LoggerFactory.getLogger(FileController.class);

    private List<FileItem> files;

    @Autowired
    private FileService fileService;

    @Autowired
    private Message returnUtil;


    @RequestMapping(value = "/api_v1/file/upload1", method = RequestMethod.POST)
    public Message uploadfile1(String targetPath) throws Exception {
        System.out.println(targetPath + "====================");
        return null;
    }


    @RequestMapping(value = "/api_v1/file/upload", method = RequestMethod.POST)
    @ApiOperation(value = "文件上传接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", required = true),
            @ApiImplicitParam(value = "上传HDFS路径", name = "targetPath", required = true)
    })
    public Message uploadfile(HttpServletRequest request, @RequestParam("targetPath") String targetPath) throws Exception {
        String tagPath = " ";
        if (StringUtils.isBlank(targetPath) || StringUtils.isEmpty(targetPath)) {
            tagPath = "/tmp/upload";
        } else {
            tagPath = targetPath;
        }
        Map<Integer, Object> result = fileService.formUpload(request, "ryan@gmail.com", tagPath);
        return returnUtil.returnValue(result);
    }

    @RequestMapping(value = "/api_v1/file/download", method = RequestMethod.GET)
    @ApiOperation(value = "文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "文件名", name = "fileName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(
                    value = "用户名", name = "userName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(
                    value = "auth_secret", name = "auth_secret", dataType = "String", paramType = "query", required = true)

    })
    public Message download(
            @ApiParam(value = "fileName", required = true) String fileName,
            @ApiParam(value = "userName", required = true) String userName,
            @ApiParam(value = "auth_secret", required = true) String auth_secret
    ) {

        HttpServletResponse response = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getResponse();
        Map<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("owner", userName);
        paramMap.put("fileName", fileName);
        paramMap.put("auth_secret", auth_secret);
        Map<Integer, Object> result = fileService.download(response, paramMap);
        return null;
    }


    @RequestMapping(value = "/api_v1/public/file/download", method = RequestMethod.POST)
    @ApiOperation(value = "公共文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String", paramType = "query", required = true)
    })
    public Message publicDownload(@ApiParam(value = "fileName", required = true) String fileName)
            throws Exception {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Map<Integer, Object> result = fileService.publicDownload(fileName, response, userName);
        return returnUtil.returnValue(result);
    }


    @RequestMapping(value = "/api_v1/file/acceptStream", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "接受engine端发送的文件流")
    public Message acceptFileStream(HttpServletRequest request) throws Exception {
        List<FileItem> fileItems = fileService.dowonloadToLocal(request);
        if (fileItems.size() > 0) {
            this.files = fileItems;
        }
        return fileItems.size() > 0 ? success(HttpStatus.SC_OK, "success") :
                error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "fail");
    }


    @RequestMapping(value = "/api_v1/file/executeDownload", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "执行下载")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件下载路径", name = "fromPath", dataType = "String", paramType = "query", required = true)
    })
    public Message executeDownload(@RequestParam(value = "fromPath", required = true) String fromPath,
                                   HttpServletResponse response) throws Exception {
        String fileName = fromPath.substring(fromPath.lastIndexOf("/") + 1);
        Map<Integer, Object> result = fileService.skipEngine(fromPath, "ryan@gmail.com");
        if (result.containsKey(500)) {
            CustomException e = (CustomException) result.get(500);
            result.put(500, e.getBody());
            return returnUtil.returnValue(result);
        }
        try {
            if (files.size() > 0) {
                for (FileItem item : files) {
                    InputStream fis = item.getInputStream();
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    fis.close();
                    response.reset();
                    response.setCharacterEncoding("utf-8");
                    response.addHeader("Content-Disposition", "attachment;filename=" +
                            URLEncoder.encode(fileName, "UTF-8"));
                    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                    org.apache.commons.io.IOUtils.copyLarge(fis, outputStream);
                }
            }
            return success(HttpStatus.SC_OK, "download success");
        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        }
    }

}