package com.geominfo.mlsql.utils;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: StringUtils
 * @author: anan
 * @create: 2020-07-17 10:20
 * @version: 1.0.0
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
