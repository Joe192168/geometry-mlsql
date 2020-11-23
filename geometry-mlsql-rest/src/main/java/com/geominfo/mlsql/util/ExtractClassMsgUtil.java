package com.geominfo.mlsql.util;

import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.domain.vo.MsqlActiveJobs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: ExtractClassMsgUtil
 * @author: anan
 * @create: 2020-11-23 11:04
 * @version: 1.0.0
 */
public class ExtractClassMsgUtil {
    /**
     * 获取类变量
     * 加if语句的目的过滤定义的类常量，例如MlsqlUser中的 public static String STATUS_LOCK = "lock";
     * @param clazz
     * @return List<String> 数组
     */
    public static List<String> extractClassName(Class clazz){
        List<String> fieldList = new ArrayList<String>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            if(Modifier.toString(field.getModifiers()).equals("private")){
                fieldList.add(field.getName());
            }
        }
        return fieldList;
    }

    public static void main(String[] args) {
        List<String> fieldList = extractClassName(MsqlActiveJobs.class);
        for(String field : fieldList){
            System.out.println(field);
        }
    }
}
