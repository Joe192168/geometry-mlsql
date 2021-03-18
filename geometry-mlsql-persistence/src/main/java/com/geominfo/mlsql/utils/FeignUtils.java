package com.geominfo.mlsql.utils;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.commons.SystemCustomIdentification;

import java.util.List;

/**
 * @program: geometry-bi
 * @description: 提供通过Feign方式请求返回数据处理
 * @author: LF
 * @create: 2020/11/18 17:32
 * @version: 1.0.0
 */
public class FeignUtils {

    /***
     * @description: 获取Feign接口返回编码
     * @author: LF
     * @date: 2020/11/24
     * @param [message]
     * @return java.lang.Integer
     */
    public static Integer getCode(Message message){
        if (message != null){
            return (Integer) message.getMeta().get(SystemCustomIdentification.CODE_KEY);
        }else {
            return null;
        }
    }

    /***
     * @description: 将调用Feign接口返回数据，转化为字符串
     * @author: LF
     * @date: 2020/11/18
     * @param [message]
     * @return java.lang.String
     */
    public static String parseString(Message message){
        if (getCode(message).equals(SystemCustomIdentification.CODE_VAL)){
            return  message.getData().get(SystemCustomIdentification.OPEN_QUERY_DATA).toString();
        }else {
            return null;
        }
    }

    /***
     * @description: 将调用Feign接口返回数据，转化为整型
     * @author: LF
     * @date: 2020/11/24
     * @param [message]
     * @return java.lang.Integer
     */
    public static Integer parseInteger(Message message){
        if (getCode(message).equals(SystemCustomIdentification.CODE_VAL)){
            return  (Integer) message.getData().get(SystemCustomIdentification.OPEN_QUERY_DATA);
        }else {
            return null;
        }
    }

    /***
     * @description: 将调用Feign接口返回数据，转化为布尔型
     * @author: LF
     * @date: 2020/11/24
     * @param [message]
     * @return java.lang.Boolean
     */
    public static Boolean parseBoolean(Message message){
        if (getCode(message).equals(SystemCustomIdentification.CODE_VAL)){
            return  (Boolean) message.getData().get(SystemCustomIdentification.OPEN_QUERY_DATA);
        }else {
            return null;
        }
    }

    /***
     * @description: 将调用Feign接口返回数据，转化为实体
     * @author: LF
     * @date: 2020/11/18
     * @param [message, clazz]
     * @return T
     */
    public static <T> T parseObject(Message message, Class<T> clazz) {
        if (getCode(message).equals(SystemCustomIdentification.CODE_VAL)){
            Object o = message.getData().get(SystemCustomIdentification.OPEN_QUERY_DATA);
            String text = JSON.toJSONString(o);
            return JSON.parseObject(text,clazz);
        }else {
            return null;
        }
    }

    /***
     * @description: 将调用Feign接口返回数据，转化为集合
     * @author: LF
     * @date: 2020/11/18
     * @param [message, clazz]
     * @return java.util.List<T>
     */
    public static <T> List<T> parseArray(Message message, Class<T> clazz) {
        if (getCode(message).equals(SystemCustomIdentification.CODE_VAL)){
            Object o = message.getData().get(SystemCustomIdentification.OPEN_QUERY_DATA);
            String text = JSON.toJSONString(o);
            return JSON.parseArray(text,clazz);
        }else {
            return null;
        }
    }
}
