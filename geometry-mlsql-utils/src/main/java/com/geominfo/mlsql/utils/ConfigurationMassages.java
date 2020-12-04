package com.geominfo.mlsql.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @program: geometry-mlsql
 * @description: 获取配置文件
 * @author: BJZ
 * @create: 2020-12-04 11:57
 * @version: 1.0.0
 */
public class ConfigurationMassages {

    private static Properties prop = new Properties();

    static {
        try {
            InputStream in = ConfigurationMassages.class
                    .getClassLoader().getResourceAsStream("params.yml");
            prop.load(in);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static Integer getInteger(String key) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Boolean getBoolean(String key) {
        String values = getProperty(key);
        try {
            return Boolean.valueOf(values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Long getLong(String key) {
        String values = getProperty(key);
        try {
            return Long.valueOf(values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
