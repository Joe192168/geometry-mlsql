package com.geominfo.mlsql.service.file.impl;



import com.geominfo.mlsql.globalconstant.GlobalConstant;
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
    private ClusterUrlService clusterUrlService ;

    @Autowired
    private FileServerDaemonServiceImpl fileServerDaemonService;

    @Autowired
    private PathFunUtil funUtil;
    //HttpServletRequest request

    @Override
    public T formUpload(HttpServletRequest request) throws ExecutionException, InterruptedException {
        fileServerDaemonService.init();

        ServletFileUpload sfu = new ServletFileUpload(new DiskFileItemFactory());

        sfu.setHeaderEncoding(GlobalConstant.ENCODE);
        List<FileItem> fileItems = new ArrayList<>();
        try {
            fileItems = sfu.parseRequest(request);

        } catch (FileUploadException e) {
            e.printStackTrace();
        }


        File homeDir = new File(GlobalConstant.DEFAULT_TEMP_PATH + CommandUtil.md5("userNmae"));//这里需要用户权限模块
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
                String prefix = GlobalConstant.DEFAULT_TEMP_PATH + CommandUtil.md5("userName"); //这里需要用户权限模块
                String itemPath = f.getName();
                String[] chunks = itemPath.split(GlobalConstant.HTTP_SEPARATED);
                for (String curUrl : chunks)
                    if (curUrl.trim().equals(GlobalConstant.POINT) || curUrl.trim().equals(GlobalConstant.POINT2))
                        return  (T)String.format(GlobalConstant.CORRECT_PATH);


                if (chunks.length > GlobalConstant.ZERO) {
                    try {
                        FileUtils.deleteDirectory(new File(prefix + GlobalConstant.HTTP_SEPARATED
                                + chunks[GlobalConstant.ZERO]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileUtils.deleteDirectory(new File(prefix + GlobalConstant.HTTP_SEPARATED
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
                        new PathFunUtil(GlobalConstant.DEFAULT_TEMP_PATH + CommandUtil.md5("userName")) //这里需要用户权限模块
                                .add(item.getName()).toPath();

                String[] temp = tempFilePath.split(GlobalConstant.HTTP_SEPARATED);
                List<String> tempsList = new ArrayList<>();
                int tempLen = temp.length;
                for (int i = GlobalConstant.ZERO; i < tempLen; i++) {
                    if (i != tempLen - GlobalConstant.ONE)
                        tempsList.add(temp[i]);
                }
                File dir = new File(funUtil.mkString(tempsList));
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File targetPath = new File(tempFilePath);

                int pathLen = funUtil.stripSuffix(funUtil.stripPrefix(tempFilePath.substring(homeDir.getPath().length()),
                        GlobalConstant.HTTP_SEPARATED), GlobalConstant.HTTP_SEPARATED).split(GlobalConstant.HTTP_SEPARATED).length;
                if (pathLen > GlobalConstant.TOW) {
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

        return runUpload(finalDir, GlobalConstant.ZERO);
    }

    private T runUpload(String finalDir, int type) throws ExecutionException, InterruptedException {

        //  TreeMap<String ,String> newParams = new TreeMap<>() ;
        LinkedMultiValueMap<String, String> newParams = new LinkedMultiValueMap<String, String>();

        switch (type) {
            case GlobalConstant.ZERO:
                newParams.add(GlobalConstant.SQL, GlobalConstant.RUN_COMMAND_DOWNLAODEXT +
                        finalDir + GlobalConstant.AND_TO_TMP_UPLAOD);
                break;

            case GlobalConstant.ONE:
                newParams.add(GlobalConstant.SQL, GlobalConstant.RUN_COMMAND_UPLOADFILETOSERVIEREXT +
                        finalDir + GlobalConstant.TOKEN_NAME_AND_TOKENVALUE + newParams.getFirst("accessToken"));
                break;

            default:
                break;
        }

        newParams.add(GlobalConstant.OWNER, newParams.getFirst("owner"));
        newParams.add(GlobalConstant.JOB_NAME, UUID.randomUUID().toString());
        newParams.add(GlobalConstant.SESSION_PERUSER, GlobalConstant.TRUE);
        newParams.add(GlobalConstant.SHOW_STACK, GlobalConstant.SHOW_STACK);
//        newParams.add(GlobalConstant.TAGS, GlobalConstant.TAGS); //这里也是需要用到用户权限模块，暂时写死

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();

        newParams.add(GlobalConstant.CONTEXT_DEFAULT_INCLUDE_URL, myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);
        newParams.add(GlobalConstant.CONTEXT_DEFAULT_FILESERVER_URL, myUrl + GlobalConstant.API_FILE_DOWNLAOD);
        newParams.add(GlobalConstant.CONTEXT_DEFAULT_FILESERVER_UPLOAD_URL, myUrl + GlobalConstant.API_FILE_UPLAOD);
        newParams.add(GlobalConstant.CONTEXT_AUTH_SECRET, CommandUtil.auth_secret());
        newParams.add(GlobalConstant.DEFAULTPATHPREFIX, CommandUtil.userHome() + "/userName"); //这里用户名也是需要等到用户谦虚模块开发完成，动态获取

        newParams.add(GlobalConstant.CALLBACK, GlobalConstant.AYN_POST_UPLOADCALLBACK_URL);
        // newParams.add(GlobalConstant.CONTEXT_DEFAULT_URL ,CommandUtil.auth_secret()) ;

      return  (T)clusterUrlService.aynRunScript(newParams) ;
//        netWorkUtil.aynPost(GlobalConstant.RUN_SCRIPT, newParams);

//        return  (T)GlobalConstant.SUCCESS;
    }


    @Override
    public T download(Object o, HttpServletResponse response) {

//        if (!hasParam(GlobalConstant.AUTH_SECRET, request) ||
//                !param(GlobalConstant.AUTH_SECRET, request).equals(CommandUtil.auth_secret()))
//            return  (T)"forbidden";
//
//        if (!hasParam(GlobalConstant.FILE_NAME, request))
//            return (T)"fileName required";

        String fileName = (String)o;

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
    public T publicDownload(Object o , HttpServletResponse response) throws ExecutionException, InterruptedException {


//        if (!hasParam(GlobalConstant.FILE_NAME, request))
//            return (T) "fileName required";

        String fileName = (String)o ;

        runUpload(fileName, GlobalConstant.ONE);

        List<String> newFiles = Arrays.asList(fileName
                .split(GlobalConstant.HTTP_SEPARATED)).stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());
        String newFile = newFiles.get(newFiles.size() - GlobalConstant.ONE);


        String targetFilePath =new PathFunUtil(GlobalConstant.DEFAULT_TEMP_PATH +
                CommandUtil.md5("userName")).add(newFile).toPath() ; //这需要用户权限模块

        try {
            if (newFile.endsWith(GlobalConstant.TAR)) {
                DownloadRunner.getTarFileByTarFile(response, targetFilePath) ;
            } else {
                DownloadRunner.getTarFileByPath(response, targetFilePath);
            }

        } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(GlobalConstant.DOWNLAOD_FAIL, e);
        }


        return null;


    }

}