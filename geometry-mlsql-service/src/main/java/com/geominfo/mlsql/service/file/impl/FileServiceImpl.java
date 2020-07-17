package com.geominfo.mlsql.service.file.impl;


import com.geominfo.mlsql.constants.Constants;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.service.file.FileService;
import com.geominfo.mlsql.utils.*;
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
    private ClusterUrlService clusterUrlService ;

    @Autowired
    private FileServerDaemonServiceImpl fileServerDaemonService;

    @Autowired
    private PathFunUtil funUtil;
    //HttpServletRequest request

    @Override
    public T formUpload(HttpServletRequest request) {
        fileServerDaemonService.init();

        ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());

        sfu.setHeaderEncoding(Constants.ENCODE);
        List<FileItem> fileItems = new ArrayList<>();
        try {
            fileItems = sfu.parseRequest(request);

        } catch (FileUploadException e) {
            e.printStackTrace();
        }


        File homeDir = new File(Constants.DEFAULT_TEMP_PATH + CommandUtil.md5("userNmae"));//这里需要用户权限模块
        String finalDir = "";

        if (homeDir.exists()) {
            long totleSize = FileUtil.sizeOfDirectory(homeDir);
            if (totleSize > Long.parseLong(CommandUtil.singleUserUploadBytes())) {
                return  (T)String.format("You have no enough space. The limit is %s bytes",
                        Integer.parseInt(CommandUtil.singleUserUploadBytes()));
            }
        }

        for (FileItem f : fileItems) {
            if (!f.isFormField()) {
                String prefix = Constants.DEFAULT_TEMP_PATH + CommandUtil.md5("userName"); //这里需要用户权限模块
                String itemPath = f.getName();
                String[] chunks = itemPath.split(Constants.HTTP_SEPARATED);
                for (String curUrl : chunks)
                    if (curUrl.trim().equals(Constants.POINT) || curUrl.trim().equals(Constants.POINT2))
                        return  (T)String.format(Constants.CORRECT_PATH);


                if (chunks.length > Constants.ZERO) {
                    try {
                        FileUtils.deleteDirectory(new File(prefix + Constants.HTTP_SEPARATED
                                + chunks[Constants.ZERO]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileUtils.deleteDirectory(new File(prefix + Constants.HTTP_SEPARATED
                                + itemPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (FileItem item : fileItems) {

            try {
                InputStream fileContent = item.getInputStream();
                String tempFilePath =
                        new PathFunUtil(Constants.DEFAULT_TEMP_PATH + CommandUtil.md5("userName")) //这里需要用户权限模块
                                .add(item.getName()).toPath();

                String[] temp = tempFilePath.split(Constants.HTTP_SEPARATED);
                List<String> tempsList = new ArrayList<>();
                int tempLen = temp.length;
                for (int i = Constants.ZERO; i < tempLen; i++) {
                    if (i != tempLen - Constants.ONE)
                        tempsList.add(temp[i]);
                }
                File dir = new File(funUtil.mkString(tempsList));
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File targetPath = new File(tempFilePath);

                int pathLen = funUtil.stripSuffix(funUtil.stripPrefix(tempFilePath.substring(homeDir.getPath().length()),
                        Constants.HTTP_SEPARATED), Constants.HTTP_SEPARATED).split(Constants.HTTP_SEPARATED).length;
                if (pathLen > Constants.TOW) {
                    finalDir = dir.getPath().substring(homeDir.getPath().length());
                } else {
                    finalDir = tempFilePath.substring(homeDir.getPath().length());
                }

                logger.info(String.format("upload to %s ", targetPath.getPath()));

                FileUtil.copyInputStreamToFile(fileContent, targetPath);
                fileContent.close();

            } catch (IOException e) {
                logger.info("upload fail ", e);
                e.printStackTrace();
                return (T)String.format("upload fail,check master log %s", e.getMessage());
            }

        }

        return runUpload(finalDir, Constants.ZERO);
    }

    private T runUpload(String finalDir, int type) {

        //  TreeMap<String ,String> newParams = new TreeMap<>() ;
        MultiValueMap<String, String> newParams = new LinkedMultiValueMap<String, String>();

        switch (type) {
            case Constants.ZERO:
                newParams.add(Constants.SQL, Constants.RUN_COMMAND_DOWNLAODEXT +
                        finalDir + Constants.AND_TO_TMP_UPLAOD);
                break;

            case Constants.ONE:
                newParams.add(Constants.SQL, Constants.RUN_COMMAND_UPLOADFILETOSERVIEREXT +
                        finalDir + Constants.TOKEN_NAME_AND_TOKENVALUE + "accessToken"); //说明：这里的accessToken 需要用到用户权限模块，这里暂时写死
                break;

            default:
                break;
        }

        newParams.add(Constants.OWNER, "userName"); //用户名等用户权限模块开发，直接动态获取，这里先暂时写死
        newParams.add(Constants.JOB_NAME, UUID.randomUUID().toString());
        newParams.add(Constants.SESSION_PERUSER, Constants.TRUE);
        newParams.add(Constants.SHOW_STACK, Constants.SHOW_STACK);
        newParams.add(Constants.TAGS, Constants.TAGS); //这里也是需要用到用户权限模块，暂时写死

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();

        newParams.add(Constants.CONTEXT_DEFAULT_INCLUDE_URL, myUrl + Constants.SCRIPT_FILE_INCLUDE);
        newParams.add(Constants.CONTEXT_DEFAULT_FILESERVER_URL, myUrl + Constants.API_FILE_DOWNLAOD);
        newParams.add(Constants.CONTEXT_DEFAULT_FILESERVER_UPLOAD_URL, myUrl + Constants.API_FILE_UPLAOD);
        newParams.add(Constants.CONTEXT_AUTH_SECRET, CommandUtil.auth_secret());
        newParams.add(Constants.DEFAULTPATHPREFIX, CommandUtil.userHome() + "/userName"); //这里用户名也是需要等到用户谦虚模块开发完成，动态获取

        newParams.add(Constants.CALLBACK, Constants.AYN_POST_UPLOADCALLBACK_URL);
        // newParams.add(Constants.CONTEXT_DEFAULT_URL ,CommandUtil.auth_secret()) ;

      return  (T)clusterUrlService.aynRunScript(newParams) ;
//        netWorkUtil.aynPost(Constants.RUN_SCRIPT, newParams);

//        return  (T)Constants.SUCCESS;
    }


    @Override
    public T download(Object o, HttpServletResponse response) {

//        if (!hasParam(Constants.AUTH_SECRET, request) ||
//                !param(Constants.AUTH_SECRET, request).equals(CommandUtil.auth_secret()))
//            return  (T)"forbidden";
//
//        if (!hasParam(Constants.FILE_NAME, request))
//            return (T)"fileName required";

        String fileName = (String)o;

        String targetFilePath =
                new PathFunUtil(Constants.DEFAULT_TEMP_PATH +
                        CommandUtil.md5("userName")).add(fileName).toPath();


        if (fileName.startsWith(Constants.PUBLIC))
            targetFilePath = Constants.DATA_MLSQL_DATA + fileName;

        logger.info("Write " + targetFilePath + " to response");

        try {
            if ("userName".endsWith(Constants.TAR))
                DownloadRunner.getTarFileByTarFile(response, targetFilePath);
            else
                DownloadRunner.getTarFileByPath(response, targetFilePath);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(Constants.DOWNLAOD_FAIL, e);
        }

        return null;

    }


    @Override
    public T publicDownload(Object o , HttpServletResponse response) {


//        if (!hasParam(Constants.FILE_NAME, request))
//            return (T) "fileName required";

        String fileName = (String)o ;

        runUpload(fileName, Constants.ONE);

        List<String> newFiles = Arrays.asList(fileName
                .split(Constants.HTTP_SEPARATED)).stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());
        String newFile = newFiles.get(newFiles.size() - Constants.ONE);


        String targetFilePath =new PathFunUtil(Constants.DEFAULT_TEMP_PATH +
                CommandUtil.md5("userName")).add(newFile).toPath() ; //这需要用户权限模块

        try {
            if (newFile.endsWith(Constants.TAR)) {
                DownloadRunner.getTarFileByTarFile(response, targetFilePath) ;
            } else {
                DownloadRunner.getTarFileByPath(response, targetFilePath);
            }

        } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(Constants.DOWNLAOD_FAIL, e);
        }


        return null;


    }

}