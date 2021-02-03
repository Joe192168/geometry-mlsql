package com.geominfo.mlsql.utils;


import org.springframework.util.DigestUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: geometry-mlsql
 * @description: 默认值工具类
 * @author: BJZ
 * @create: 2020-07-09 11:57
 * @version: 1.0.0
 */
public class CommandUtil {

    private static final String MLSQL_ENGINE_URL = "engine.url";
    private static final String MLSQL_CLUSTER_URL = "cluster.url";
    private static final String MY_URL = "my_url.url";
    private static final String USER_HOME = "user.home";
    private static final String UPLOAD_BYTES = "user.single.user.upload.bytes";
    private static final String AUTH_CENTER = "user.enable.auth.center";
    private static final String AUTH_SECRET = "user.auth.secret";
    private static Map<String, Object> paramsMap;

    private CommandUtil(){}

    static {
        try {
            InputStream in = CommandUtil.class
                    .getClassLoader().getResourceAsStream("application.yml");
            paramsMap = analysis(in);
//            System.out.println(paramsMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Map<String, Object> analysis(InputStream in) {
        Map<String, Object> paramsMap = new ConcurrentHashMap<>();
        Scanner sc = new Scanner(in);
        String tmp = "";
        while (sc.hasNextLine()) {
            StringBuilder sf = new StringBuilder();
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                if (!line.startsWith("  ") && check(line)) {
                    tmp = line.substring(0, line.indexOf(":"));
                }
                if (line.startsWith("  ") && check(line.trim())) {
                    sf.append(tmp).append(".");
                    String tmpline = line.trim();
                    int index = tmpline.indexOf(":");
                    String level = tmpline.substring(0, index);
                    int len = tmpline.length();
                    if (index < len - 1) {
                        String value = tmpline.substring(index + 1, tmpline.length());
                        sf.append(level);
                        paramsMap.put(sf.toString(), value.trim());
                    }
                }
            }
        }
        return paramsMap;
    }

    private static boolean check(String targeter) {
        return !targeter.startsWith("#") && !targeter.startsWith("-");
    }


    public static String mlsqlEngineUrl() {
        return (String) returnParam(MLSQL_ENGINE_URL, "");

    }

    public static String mlsqlClusterUrl() {

        return (String) returnParam(MLSQL_CLUSTER_URL, "");
    }

    public static String myUrl() {
        return (String) returnParam(MY_URL, "");

    }

    public static String userHome() {
        return (String) returnParam(USER_HOME, "");
    }

    public static String singleUserUploadBytes() {
        return (String) returnParam(UPLOAD_BYTES, "0");
    }

    public static boolean enableAuthCenter() {
        return Boolean.valueOf(returnParam(AUTH_CENTER,
                "false").toString());
    }

    public static String auth_secret() {
        return (String) returnParam(AUTH_SECRET, "mlsql");
    }


    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

    private static Object returnParam(String key, Object defaultValue) {
        Object value = paramsMap.get(key);
        if (!ParamsUtil.containsKey(key) && value != null )
            ParamsUtil.setParam(key, value);

        return ParamsUtil.getParam(key, defaultValue);
    }


}