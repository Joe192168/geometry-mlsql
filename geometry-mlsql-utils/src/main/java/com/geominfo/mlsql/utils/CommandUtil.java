package com.geominfo.mlsql.utils;



import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @program: geometry-mlsql
 * @description: 默认值工具类
 * @author: BJZ
 * @create: 2020-07-09 11:57
 * @version: 1.0.0
 */

public class CommandUtil {

    private static ParamsUtil paramsUtil = new ParamsUtil();
    public static String mlsqlClusterUrl(){

        return paramsUtil.getParam("mlsql_cluster_url" , "192.168.20.209:9003") ;
    }

    public static String myUrl(){
        return paramsUtil.getParam("my_url" ,"jdbc:mysql://192.168.2.239:3306/mlsql_console?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false") ;
    }

    public static String userHome(){
        return paramsUtil.getParam("user_home" , "/home/mlsql") ;
    }

    public static String singleUserUploadBytes(){
        return paramsUtil.getParam("single_user_upload_bytes" ,
                1024l * 1024 * 125 + "") ;
    }

    public static boolean enableAuthCenter(){
        return Boolean.parseBoolean(paramsUtil.getParam("enable_auth_center", "false")) ;
    }

    public static String auth_secret()
    {
        return paramsUtil.getParam("auth_secret" , UUID.randomUUID().toString()) ;
    }

    public static String md5(String text)
    {
       return  DigestUtils.md5DigestAsHex(text.getBytes());
    }

}