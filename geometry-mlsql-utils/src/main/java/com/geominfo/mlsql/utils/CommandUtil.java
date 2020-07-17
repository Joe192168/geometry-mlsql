package com.geominfo.mlsql.utils;


import com.geominfo.mlsql.constants.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @program: geometry-mlsql
 * @description: 默认值工具类
 * @author: BJZ
 * @create: 2020-07-09 11:57
 * @version: 1.0.0
 */
@Component
public class CommandUtil {


    private static ParamsUtil paramsUtil = new ParamsUtil();

    public static String mlsqlClusterUrl(){
        return paramsUtil.getParam(Constants.MLSQL_CLUSTER_URL , Constants.MLSQL_CLUSTER_DEFAULT_URL) ;
    }

    public static String myUrl(){
        return paramsUtil.getParam(Constants.MLSQL_MY_URL , Constants.MLSQL_MY_DEFAULT_URL) ;
    }

    public static String userHome(){
        return paramsUtil.getParam(Constants.MLSQL_USER_HOME , Constants.MLSQL_USER_DEFAULT_HOME) ;
    }

    public static String singleUserUploadBytes(){
        return paramsUtil.getParam(Constants.SINGLE_USER_UPLOAD_BYTES ,
                Constants.SINGLE_USER_UPLOAD_DEFAULT_BYTES+"") ;
    }

    public static String enableAuthCenter(){
        return paramsUtil.getParam(Constants.ENABLE_AUTH_CENTER , Constants.FALSE) ;
    }

    public static String auth_secret()
    {
        return paramsUtil.getParam(Constants.AUTH_SECRET , UUID.randomUUID().toString()) ;
    }

    public static String md5(String text)
    {
       return  DigestUtils.md5DigestAsHex(text.getBytes());
    }

}