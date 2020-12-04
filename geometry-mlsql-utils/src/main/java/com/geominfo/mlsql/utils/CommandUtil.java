package com.geominfo.mlsql.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
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
public class CommandUtil {

    private static final String MLSQL_ENGINE_URL ="mlsql.engine.url" ;
    private static final String MLSQL_CLUSTER_URL ="mlsql.cluster.url" ;
    private static final String MY_URL ="my.url" ;
    private static final String USER_HOME ="user.home" ;
    private static final String UPLOAD_BYTES ="single.user.upload.bytes" ;
    private static final String AUTH_CENTER ="enable.auth.center" ;
    private static final String AUTH_SECRET ="auth.secret" ;

    public static String mlsqlEngineUrl() {
      return (String) returnParam(MLSQL_ENGINE_URL ,"");

    }

    public static String mlsqlClusterUrl(){

        return (String) returnParam(MLSQL_CLUSTER_URL ,"");
    }

    public static String myUrl(){
        return (String) returnParam(MY_URL ,"");

    }

    public static String userHome(){
        return (String) returnParam(USER_HOME,"");
    }

    public static String singleUserUploadBytes(){
        return (String) returnParam(UPLOAD_BYTES,"0");
    }

    public static boolean enableAuthCenter(){
        return Boolean.valueOf(returnParam(AUTH_CENTER,"false").toString()) ;
    }

    public static String auth_secret()
    {
        return (String) returnParam(AUTH_SECRET ,"mlsql");
    }


    public static String md5(String text)
    {
       return  DigestUtils.md5DigestAsHex(text.getBytes());
    }

    private static Object returnParam(String key ,Object defaultValue)
    {
        Object value =ConfigurationMassages.getProperty(key) ;
        if(!ParamsUtil.containsKey(key))
            ParamsUtil.setParam(key,value);

        return ParamsUtil.getParam(key,
                defaultValue);
    }




}