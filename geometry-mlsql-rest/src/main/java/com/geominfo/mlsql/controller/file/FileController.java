package com.geominfo.mlsql.controller.file;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.file.FileService;
import com.geominfo.mlsql.util.ReturnUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

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
public class FileController extends BaseController{

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private ReturnUtil returnUtil ;

    @RequestMapping(value = "/api_v1/file/upload" ,method = RequestMethod.POST)
    @ApiOperation(value = "文件上传接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", required = true)
    })
    public Message uploadfile(HttpServletRequest request) throws Exception {
        Map<Integer ,Object> result=  fileService.formUpload(request ,"banjianzu@gmail.com");
        return returnUtil.returnValue(result) ;
    }

    @RequestMapping(value = "/api_v1/file/download" ,method = RequestMethod.GET)
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
            @ApiParam(value = "fileName", required = true) String fileName ,
            @ApiParam(value = "userName", required = true) String userName ,
            @ApiParam(value = "auth_secret", required = true) String auth_secret
            )

            throws UnsupportedEncodingException {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        Map<String ,Object> paramMap = new ConcurrentHashMap<>() ;
        paramMap.put("owner" ,userName) ;
        paramMap.put("fileName" ,fileName) ;
        paramMap.put("auth_secret" ,auth_secret) ;
        Map<Integer ,Object> result =  fileService.download( response,paramMap);
        return null;
    }


    @RequestMapping(value = "/api_v1/public/file/download" , method = RequestMethod.POST)
    @ApiOperation(value = "公共文件下载接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件名", name = "fileName", dataType = "String", paramType = "query", required = true)
    })
    public Message publicDownload(@ApiParam(value = "fileName", required = true) String fileName)
            throws Exception{
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
       Map<Integer ,Object> result= fileService.publicDownload(fileName, response,userName);
        return returnUtil.returnValue(result);
    }



}