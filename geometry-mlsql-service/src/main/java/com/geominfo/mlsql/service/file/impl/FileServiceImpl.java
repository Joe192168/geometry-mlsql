package com.geominfo.mlsql.service.file.impl;


import com.geominfo.mlsql.domain.vo.MlsqlEngine;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.service.engine.EngineService;
import com.geominfo.mlsql.service.file.FileService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.utils.*;

import org.apache.commons.fileupload.FileItem;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
public class FileServiceImpl extends BaseServiceImpl implements FileService {

    Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private ClusterUrlService clusterUrlService;

    @Autowired
    private FileServerDaemonServiceImpl fileServerDaemonService;

    @Autowired
    private PathFunUtil funUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EngineService engineService;


    @Override
    public <T> T formUpload(HttpServletRequest request, String owner) throws Exception {

        fileServerDaemonService.init();

        ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());

        sfu.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = sfu.parseRequest(request);

        File homeDir = new File("/tmp/upload/" + CommandUtil.md5(owner));
        String finalDir = "";

        if (homeDir.exists()) {
            long totleSize = FileUtil.sizeOfDirectory(homeDir);
            if (totleSize > Long.parseLong(CommandUtil.singleUserUploadBytes())) {
                Map<Integer, Object> resMap = new ConcurrentHashMap<>();
                return (T) resMap.put(500, String.format("You have no enough space. The limit is %s bytes",
                        Integer.parseInt(CommandUtil.singleUserUploadBytes())));
            }
        }

        for (FileItem f : fileItems) {
            if (!f.isFormField()) {
                String prefix = "/tmp/upload/" + CommandUtil.md5(owner);
                String itemPath = f.getName();
                String[] chunks = itemPath.split("/");
                for (String curUrl : chunks)
                    if (curUrl.trim().equals(".") || curUrl.trim().equals("..")) {
                        Map<Integer, Object> resMap = new ConcurrentHashMap<>();
                        return (T) resMap.put(500, String.format("file path is not correct"));
                    }


                if (chunks.length > 0) {

                    deleteQuietly(new File(prefix + "/"
                            + chunks[0]));
                } else {
                    deleteQuietly(new File(prefix + "/"
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

            FileUtil.copyInputStreamToFile(fileContent, targetPath);
            fileContent.close();


        }

        return runUpload(finalDir, 0, owner);
    }

    private <T> T runUpload(String finalDir, int type, String owner) throws ExecutionException, InterruptedException {

        LinkedMultiValueMap<String, String> newParams = new LinkedMultiValueMap<String, String>();
        MlsqlUser user = userService.getUserByName(owner);

        List<MlsqlEngine> engines = engineService.list();

        String engineName = !newParams.containsKey("engineName")
                || newParams.get("engineName").equals("undefined") ?
                getBackendName(user) : newParams.get("engineName").toString();


        List<MlsqlEngine> tmpEngienList = engines.stream().filter(me -> me.getName().equals(engineName))
                .collect(Collectors.toList());

        MlsqlEngine engineConfig = tmpEngienList.size() > 0 ? tmpEngienList.get(0) : null;


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

        logger.info("文件上传sql=" + newParams.get("sql"));

        newParams.add("owner", owner);
        newParams.add("jobName", UUID.randomUUID().toString());
        newParams.add("sessionPerUser", "true");
        newParams.add("show_stack", "false");
        newParams.add("tags", user.getBackendTags());

        newParams.add("context.__default__include_fetch_url__", engineConfig.getConsoleUrl() + "/api_v1/script_file/include");
        newParams.add("context.__default__console_url__", engineConfig.getConsoleUrl());
        newParams.add("context.__default__fileserver_url__", engineConfig.getFileServerUrl() + "/api_v1/file/download");
        newParams.add("context.__default__fileserver_upload_url__", engineConfig.getFileServerUrl() + "/api_v1/file/upload");
        newParams.add("context.__auth_client__", "streaming.dsl.auth.meta.client.MLSQLConsoleClient");
        newParams.add("context.__auth_server_url__", engineConfig.getAuthServerUrl() + "/api_v1/table/auth");
        newParams.add("context.__auth_secret__", CommandUtil.auth_secret());
        newParams.add("access_token", engineConfig.getAccessToken());
        newParams.add("defaultPathPrefix", engineConfig.getHome() + "/" + user.getName());
        newParams.add("skipAuth", String.valueOf(1 == engineConfig.getSkipAuth()));
        newParams.add("skipGrammarValidate", "false");

        Map<Integer, Object> resMap = new ConcurrentHashMap<>();
        ResponseEntity<String> responseEntity = clusterUrlService.synRunScript(newParams);
        return (T) resMap.put(200, responseEntity != null ? responseEntity.getBody() : null);

    }

    private static final String EXTRA_DEFAULT_BACKEND = "backend";

    private String getBackendName(MlsqlUser mlsqlUser) {
        if (mlsqlUser.getBackendTags() != null && !mlsqlUser.getBackendTags().isEmpty()) {
            Map<String, String> map = JSONTool.parseJson(mlsqlUser.getBackendTags(), Map.class);
            return map.get(EXTRA_DEFAULT_BACKEND);
        } else
            return "";
    }

    private boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        } else {
            try {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                }
            } catch (Exception var3) {
                logger.error(var3.getMessage());
            }

            try {
                return file.delete();
            } catch (Exception var2) {
                logger.error(var2.getMessage());
                return false;
            }
        }
    }


    @Override
    public <T> T download(HttpServletResponse response, Map<String, Object> paramMap) {

        Map<Integer, Object> returnMap = new ConcurrentHashMap<>();
        if (!paramMap.containsKey("auth_secret") ||
                !paramMap.get("auth_secret").equals(CommandUtil.auth_secret()))
            return (T) returnMap.put(500, "forbidden");

        if (!paramMap.containsKey("fileName"))
            return (T) returnMap.put(500, "fileName required");

        String[] tmpS = paramMap.get("fileName").toString().split("/");
        String fileName = tmpS.length > 0 ? tmpS[tmpS.length - 1] : "";

        String targetFilePath =
                new PathFunUtil("/tmp/upload/" +
                        CommandUtil.md5(paramMap.get("owner").toString()))
                        .add(fileName).toPath();

        if (fileName.startsWith("public/"))
            targetFilePath = "/data/mlsql/data/" + fileName;

        logger.info("Write " + targetFilePath + " to response");

        try {
            if (fileName.endsWith(".tar")) {
                int status = DownloadRunner.getTarFileByTarFile(response, targetFilePath);
                if (status == 200)
                    returnMap.put(200, "success");
                else returnMap.put(400, "error");
            } else{
                int status =  DownloadRunner.getTarFileByPath(response, targetFilePath);
                if (status == 200)
                    returnMap.put(200, "success");
                else returnMap.put(400, "error");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(GlobalConstant.DOWNLAOD_FAIL, e);
        }

        return (T) returnMap;

    }


    @Override
    public <T> T publicDownload(Object o, HttpServletResponse response, String owner) {
        Map<Integer, Object> returnMap = new ConcurrentHashMap<>();
//        if (!hasParam(GlobalConstant.FILE_NAME, request))
//            return (T) "fileName required";

        String fileName = (String) o;

//        runUpload(fileName, GlobalConstant.ONE, owner);

        List<String> newFiles = Arrays.asList(fileName
                .split(GlobalConstant.HTTP_SEPARATED)).stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());
        String newFile = newFiles.get(newFiles.size() - GlobalConstant.ONE);


        String targetFilePath = new PathFunUtil(GlobalConstant.DEFAULT_TEMP_PATH +
                CommandUtil.md5(owner)).add(newFile).toPath();

        try {
            if (newFile.endsWith("")) {
                DownloadRunner.getTarFileByTarFile(response, targetFilePath);
            } else {
                DownloadRunner.getTarFileByPath(response, targetFilePath);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(GlobalConstant.DOWNLAOD_FAIL, e);
        }


        return (T) returnMap;


    }

}