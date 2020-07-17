package com.geominfo.mlsql.controller.file;



import com.geominfo.mlsql.service.file.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: geometry-mlsql
 * @description: 文件处理控制类
 * @author: BJZ
 * @create: 2020-07-09 09:24
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/file")
@Api(value="文件处理接口",tags={"文件处理接口"})
@Log4j2
public class FileController {

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService ;

    @RequestMapping("/api_v1/file/upload")
    @ApiOperation(value = "文件上传接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", required = true)
    })
    public String uploadfile(HttpServletRequest request, HttpServletResponse response ){

       String result = (String) fileService.formUpload(request);
        return result ;
    }

    @RequestMapping("/api_v1/file/download")
    @ApiOperation(value = "文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String",  required = true)
    })
    public String download(HttpServletRequest request, HttpServletResponse response ){
        String result = (String) fileService.download(request ,response);
        return result ;
    }


    @RequestMapping("/api_v1/public/file/download")
    @ApiOperation(value = "文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String",  required = true)
    })
    public String publicDownload(HttpServletRequest request, HttpServletResponse response ){
        String result = (String) fileService.publicDownload(request ,response);
        return result ;
    }


    @GetMapping("/api_v1/file/upload/callback")
    public String uploadcallback(){
        logger.info("uploadcallback!");
        return "success" ;
    }


}