package com.geominfo.mlsql.service.file.impl;


import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.service.file.FileService;
import com.geominfo.mlsql.utils.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @program: geometry-mlsql
 * @description: 文件处理服务类
 * @author: BJZ
 * @create: 2020-07-09 17:06
 * @version: 1.0.0
 */
@Service
public class FileServiceImpl<T> extends BaseServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private ClusterUrlService clusterUrlService;

    @Autowired
    private FileServerDaemonServiceImpl fileServerDaemonService;

    @Autowired
    private PathFunUtil funUtil;


    @Override
    public T formUpload(HttpServletRequest request, String owner) throws Exception {
        fileServerDaemonService.init();

        ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());

        sfu.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = sfu.parseRequest(request);

        File homeDir = new File("/tmp/upload/" + CommandUtil.md5("userNmae"));//这里需要用户权限模块
        String finalDir = "";

        if (homeDir.exists()) {
            long totleSize = FileUtil.sizeOfDirectory(homeDir);
            if (totleSize > Long.parseLong(CommandUtil.singleUserUploadBytes())) {
                return (T) String.format("You have no enough space. The limit is %s bytes",
                        Integer.parseInt(CommandUtil.singleUserUploadBytes()));
            }
        }

        for (FileItem f : fileItems) {
            if (!f.isFormField()) {
                String prefix = "/tmp/upload/" + CommandUtil.md5("userName"); //这里需要用户权限模块
                String itemPath = f.getName();
                String[] chunks = itemPath.split("/");
                for (String curUrl : chunks)
                    if (curUrl.trim().equals(".") || curUrl.trim().equals(".."))
                        return (T) String.format("file path is not correct");


                if (chunks.length > 0) {
                    FileUtils.deleteDirectory(new File(prefix + "/"
                            + chunks[0]));
                } else {
                    FileUtils.deleteDirectory(new File(prefix + "/"
                            + itemPath));
                }
            }
        }

        for (FileItem item : fileItems) {

            InputStream fileContent = item.getInputStream();
            String tempFilePath =
                    new PathFunUtil("/tmp/upload/" + CommandUtil.md5(owner))
                            .add(item.getName()).toPath();

            String[] temp = tempFilePath.split("/");
            List<String> tempsList = new ArrayList<>();
            int tempLen = temp.length;
            for (int i = 0; i < tempLen; i++) {
                if (i != tempLen - 1)
                    tempsList.add(temp[i]);
            }
            File dir = new File(funUtil.mkString(tempsList));
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File targetPath = new File(tempFilePath);

            int pathLen = funUtil.stripSuffix(funUtil.stripPrefix(tempFilePath.substring(homeDir.getPath().length()),
                    "/"), "/").split("/").length;
            if (pathLen > 2) {
                finalDir = dir.getPath().substring(homeDir.getPath().length());
            } else {
                finalDir = tempFilePath.substring(homeDir.getPath().length());
            }

            finalDir = tempFilePath.substring(homeDir.getPath().length());

            logger.info(String.format("upload to %s ", targetPath.getPath()));

//                FileUtil.copyInputStreamToFile(fileContent, targetPath);
//                fileContent.close();


        }

        return runUpload(finalDir, 0, owner);
    }

    private T runUpload(String finalDir, int type, String owner) throws ExecutionException, InterruptedException {

        LinkedMultiValueMap<String, String> newParams = new LinkedMultiValueMap<String, String>();

        switch (type) {
            case 0:
                newParams.add("sql", "\nrun command as DownloadExt.`` where from=" +
                        "\"" + finalDir + "\"" + " and to=" + "\"" + "/tmp/upload" + "\"" + ";\n");
                break;

            case 1:
                newParams.add("sql", "\nrun command as UploadFileToServerExt.`" +
                        finalDir + "` where tokenName='access-token' and tokenValue=" + newParams.getFirst("accessToken") + "\n");
                break;

            default:
                break;
        }

        newParams.add("owner", owner);
        newParams.add("jobName", UUID.randomUUID().toString());
        newParams.add("sessionPerUser", "true");
        newParams.add("show_stack", "false");
////        newParams.add(GlobalConstant.TAGS, GlobalConstant.TAGS); //这里也是需要用到用户权限模块，暂时写死
//
        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.mlsqlEngineUrl();
//
        newParams.add("context.__default__include_fetch_url__", myUrl + "/api_v1/script_file/include");
        newParams.add("context.__default__fileserver_url__", myUrl + "/api_v1/file/download");
        newParams.add("context.__default__fileserver_upload_url__", myUrl + "/api_v1/file/upload");
        newParams.add("context.__auth_secret__", CommandUtil.auth_secret());
        newParams.add("defaultPathPrefix", CommandUtil.userHome() + "/" + owner);

//        newParams.add("callback", "localhost:8088/file/api_v1/file/upload/callback");


        //同步请求
        return (T) clusterUrlService.synRunScript(newParams);
//        netWorkUtil.aynPost(GlobalConstant.RUN_SCRIPT, newParams);

//        return  (T)GlobalConstant.SUCCESS;
    }


    @Override
    public T download(Object o, HttpServletResponse response, String owner) {

//        if (!hasParam(GlobalConstant.AUTH_SECRET, request) ||
//                !param(GlobalConstant.AUTH_SECRET, request).equals(CommandUtil.auth_secret()))
//            return  (T)"forbidden";
//
//        if (!hasParam(GlobalConstant.FILE_NAME, request))
//            return (T)"fileName required";

        String fileName = (String) o;

        String targetFilePath =
                new PathFunUtil(GlobalConstant.DEFAULT_TEMP_PATH +
                        CommandUtil.md5("userName")).add(fileName).toPath();


        if (fileName.startsWith(GlobalConstant.PUBLIC))
            targetFilePath = GlobalConstant.DATA_MLSQL_DATA + fileName;

        logger.info("Write " + targetFilePath + " to response");

        try {
            if ("userName".endsWith(GlobalConstant.TAR))
                DownloadRunner.getTarFileByTarFile(response, targetFilePath);
            else
                DownloadRunner.getTarFileByPath(response, targetFilePath);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(GlobalConstant.DOWNLAOD_FAIL, e);
        }

        return null;

    }


    @Override
    public T publicDownload(Object o, HttpServletResponse response, String owner) throws ExecutionException, InterruptedException {


//        if (!hasParam(GlobalConstant.FILE_NAME, request))
//            return (T) "fileName required";

        String fileName = (String) o;

        runUpload(fileName, GlobalConstant.ONE, owner);

        List<String> newFiles = Arrays.asList(fileName
                .split(GlobalConstant.HTTP_SEPARATED)).stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());
        String newFile = newFiles.get(newFiles.size() - GlobalConstant.ONE);


        String targetFilePath = new PathFunUtil(GlobalConstant.DEFAULT_TEMP_PATH +
                CommandUtil.md5("userName")).add(newFile).toPath(); //这需要用户权限模块

        try {
            if (newFile.endsWith(GlobalConstant.TAR)) {
                DownloadRunner.getTarFileByTarFile(response, targetFilePath);
            } else {
                DownloadRunner.getTarFileByPath(response, targetFilePath);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(GlobalConstant.DOWNLAOD_FAIL, e);
        }


        return null;


    }

}