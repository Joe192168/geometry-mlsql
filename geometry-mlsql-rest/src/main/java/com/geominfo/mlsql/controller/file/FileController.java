package com.geominfo.mlsql.controller.file;

import com.geominfo.mlsql.config.restful.CustomException;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.FileInfo;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlEngine;
import com.geominfo.mlsql.service.file.FileService;
import com.geominfo.mlsql.utils.ClassRealPath;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//@PropertySource(value = "file:./config/core-site.xml")
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


    /**
     * 依赖engine插件进行文件下载
     *
     * @param request
     * @return
     * @throws Exception
     */
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
    public synchronized Message executeDownload(@RequestParam(value = "fromPath", required = true) String fromPath,
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
                    response.reset();
                    response.setCharacterEncoding("utf-8");
                    response.addHeader("Content-Disposition", "attachment;filename=" +
                            URLEncoder.encode(fileName, "UTF-8"));
                    ServletOutputStream outputStream = response.getOutputStream();
                    IOUtils.copyLarge(fis, outputStream);
                    IOUtils.closeQuietly(outputStream);
                    IOUtils.closeQuietly(fis);
                }
            }

            return success(HttpStatus.SC_OK, "download success");
        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        } finally {
            files.clear();
        }
    }


    /**
     * 不依赖engine进行文件下载
     *
     * @param fromPath
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api_v1/file/console_downloadFile", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "执行下载")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件下载路径", name = "fromPath", dataType = "String", paramType = "query", required = true)
    })
    public Message console_downloadFile(@RequestParam(value = "fromPath", required = true) String fromPath,
                                        HttpServletResponse response) throws Exception {
        try {
            MlsqlEngine mlsqlEngine = fileService.getMlsqlEngine("ryan@gmail.com");
            String fileName = fromPath.substring(fromPath.lastIndexOf("/") + 1);
            Configuration conf = new Configuration();
            conf.addResource(new Path("core-site.xml"));
            //conf.addResource(new Path(ClassRealPath.getClassRealPath(FileController.class) + "/core-site.xml"));
            String s = conf.get("fs.defaultFS");
            System.out.println("===============================" + s);

            FileSystem fs = FileSystem.get(conf);
            FSDataInputStream fis = fs.open(new Path(mlsqlEngine.getHome() + "/" + "ryan@gmail.com" + "/" + fromPath));
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copyLarge(fis, outputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(fis);
            return success(HttpStatus.SC_OK, "download success");
        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        }
    }


    /**
     * 不依赖engine插件进行文件上传
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/api_v1/file/console_uploadFile", method = {RequestMethod.POST})
    @ApiOperation(value = "不依赖engine文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件上传路径", name = "targetPath", dataType = "String", paramType = "query", required = true)
    })
    public Message console_uploadFile(HttpServletRequest request,
                                      @RequestParam(value = "targetPath", required = true) String targetPath) throws Exception {

        try {
            MlsqlEngine mlsqlEngine = fileService.getMlsqlEngine("ryan@gmail.com");
            List<FileItem> fileItems = fileService.dowonloadToLocal(request);
            if (fileItems.size() > 0) {
                Configuration conf = new Configuration();
                //本地加载
                conf.addResource(new Path("core-site.xml"));
                //服务器加载路径
                //conf.addResource(new Path(ClassRealPath.getClassRealPath(FileController.class) + "/core-site.xml"));
                for (FileItem item : fileItems) {

                    InputStream is = item.getInputStream();
                    String fileName = item.getName();

                    FileSystem fs = FileSystem.get(conf);
                    String finalPath = mlsqlEngine.getHome() + "/" + "ryan@gmail.com" + "/" + targetPath;
                    if (!fs.exists(new Path(finalPath))) {
                        fs.mkdirs(new Path(finalPath));
                    }
                    FSDataOutputStream fdos = fs.create(new Path(new File(finalPath, fileName).getPath()), true);
                    org.apache.hadoop.io.IOUtils.copyBytes(is, fdos, conf);
                }
            }
            return success(HttpStatus.SC_OK, "upload File success");
        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        }
    }


    @RequestMapping(value = "/api_v1/file/fileListInfo", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "获取hdfs文件权限,大小，创建时间，路径等信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查看路径", name = "targetPath", dataType = "String", paramType = "query", required = true)
    })
    public Message console_fileListInfo(@RequestParam(value = "targetPath", required = true) String targetPath) throws Exception {
        MlsqlEngine mlsqlEngine = fileService.getMlsqlEngine("ryan@gmail.com");
        Path path = new Path(mlsqlEngine.getHome() + "/" + "ryan@gmail.com" + "/" + targetPath);
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            conf.addResource(new Path("core-site.xml"));
            //部署服务器路径
            //conf.addResource(new Path(ClassRealPath.getClassRealPath(FileController.class) + "/core-site.xml"));
            fs = FileSystem.get(conf);
            if (fs.exists(path)) {
                FileStatus[] fileStatuses = fs.listStatus(path);
                List fileInfos = new ArrayList<FileInfo>();
                for (FileStatus s : fileStatuses) {
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(s.getModificationTime());
                    FileInfo fileInfo = new FileInfo(
                            s.getPermission().toString(),
                            s.getOwner(),
                            s.getGroup(),
                            String.format("%.2f", s.getLen() / 1024.00) + "kb",
                            date,
                            new Path(path.toString(), s.getPath().getName().toString()).toString());

                    fileInfos.add(fileInfo);
                }
                return success(HttpStatus.SC_OK, "success").addData("fileInfo", fileInfos);
            } else {
                return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "path  does not exist");
            }

        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        } finally {
            fs.close();
        }
    }

    @RequestMapping(value = "/api_v1/file/createDirectory", method = {RequestMethod.GET})
    @ApiOperation(value = "创建hdfs目录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "创建路径", name = "targetPath", dataType = "String", paramType = "query", required = true)
    })
    public Message console_createDirectory(@RequestParam(value = "targetPath", required = true) String targetPath) throws Exception {
        FileSystem fs = null;
        try {
            MlsqlEngine mlsqlEngine = fileService.getMlsqlEngine("ryan@gmail.com");
            Path path = new Path(mlsqlEngine.getHome() + "/" + "ryan@gmail.com" + "/" + targetPath);
            Configuration conf = new Configuration();
            conf.addResource(new Path("core-site.xml"));
            //conf.addResource(new Path(ClassRealPath.getClassRealPath(FileController.class) + "/core-site.xml"));
            fs = FileSystem.get(conf);
            if (!fs.exists(path)) {
                fs.mkdirs(path);
                return success(HttpStatus.SC_OK, "create directory success");
            } else {
                System.out.println("file exists");
                return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "create directory fail");
            }
        } catch (CustomException e) {
            return error(HttpStatus.SC_NOT_FOUND, e.getBody());
        } finally {
            fs.close();
        }
    }

    @RequestMapping(value = "/api_v1/file/deleteDirectory", method = {RequestMethod.GET})
    @ApiOperation(value = "删除文件或目录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "目标路径", name = "targetPath", dataType = "String", paramType = "query", required = true)
    })
    public Message console_deleteDirectory(@RequestParam(value = "targetPath", required = true) String targetPath,
                                           @RequestParam(value = "recutsive", required = true, defaultValue = "false") Boolean recursive) throws Exception {
        MlsqlEngine mlsqlEngine = fileService.getMlsqlEngine("ryan@gmail.com");
        Path path = new Path(mlsqlEngine.getHome() + "/" + "ryan@gmail.com" + "/" + targetPath);
        FileSystem fs = null;
        try {
            Configuration conf = new Configuration();
            conf.addResource(new Path("core-site.xml"));
            //conf.addResource(new Path(ClassRealPath.getClassRealPath(FileController.class) + "/core-site.xml"));
            fs = FileSystem.get(conf);
            if (fs.exists(path)) {
                if (fs.isFile(path)) {
                    fs.delete(path, true);
                    return success(HttpStatus.SC_OK, "delete file success");
                } else {
                    fs.delete(path, recursive);
                    return success(HttpStatus.SC_OK, "delete directory success");
                }
            } else {
                return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "path  does not exist");
            }
        } catch (CustomException e) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getBody());
        } finally {
            fs.close();
        }
    }
}