package com.geominfo.mlsql.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONObject;
import org.omg.CORBA.Object;


/**
 * @program: geometry-mlsql
 * @description: JSONTool
 * @author: BJZ
 * @create: 2020-11-25 17:01
 * @version: 1.0.0
 */
public class JSONTool {

    public static <T> T parseJson(String str ,Class<?> c) { return (T)JSONObject.parseObject(str,c ) ; }

    public  static <T> String toJsonStr(T item) {
//       return  net.liftweb.json.Serialization.write(item ,null) ;
        return JSON.toJSONString(item) ;
    }

    public static String toJsonList4J(Object item) {
        return JSONArray.toJSONString(item) ;
    }

    public static String toJsonMap4J(Object item){
        return JSONObject.toJSONString(item) ;
    }




}