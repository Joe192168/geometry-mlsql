package com.geominfo.mlsql.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

/**
 * Created by Lenovo on 2021/2/4.
 */
public class ClassRealPath {
    public static String getClassRealPath(Class<?> tClass){
        ApplicationHome h = new ApplicationHome(tClass);
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString();
        return path;
    }
}
