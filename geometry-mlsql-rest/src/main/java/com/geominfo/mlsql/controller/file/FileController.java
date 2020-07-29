package com.geominfo.mlsql.controller.file;




import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.file.FileService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

/**
 * @program: geometry-mlsql
 * @description: 文件处理控制类
 * @author: BJZ
 * @create: 2020-07-09 09:24
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/file")
@Api(value = "文件处理接口", tags = {"文件处理接口"})
@Log4j2
public class FileController extends BaseController{

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private Message message ;

    @Autowired
    private FileService fileService;

    @RequestMapping("/api_v1/file/upload")
    @ApiOperation(value = "文件上传接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", required = true)
    })
    public Message uploadfile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String result = (String) fileService.formUpload(request ,userName);
        return success(200 ,result) ;
    }

    @RequestMapping("/api_v1/file/download")
    @ApiOperation(value = "文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String", paramType = "query", required = true)
    })
    public Message download(@ApiParam(value = "fileName", required = true) String fileName) throws UnsupportedEncodingException {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        fileName = null ;
        String result = (String) fileService.download(fileName, response,userName);
        return message.ok(result);
    }


    @RequestMapping("/api_v1/public/file/download")
    @ApiOperation(value = "公共文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String", paramType = "query", required = true)
    })
    public Message publicDownload(@ApiParam(value = "fileName", required = true) String fileName)
            throws Exception{
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        String result = (String) fileService.publicDownload(fileName, response,userName);
        return message.ok(result);
    }


    @RequestMapping("/api_v1/file/upload/callback")
    @ApiOperation(value = "文件上传回调接口", httpMethod = "GET")
    public Message uploadcallback() {
        logger.info("uploadcallback!");
        return message.ok(GlobalConstant.SUCCESS);
    }


}