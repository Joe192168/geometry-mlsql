package com.geominfo.mlsql.globalconstant;

import com.geominfo.mlsql.domain.vo.TableTypeMeta;

import java.util.*;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: GlobalConstant
 * @author: anan
 * @create: 2020-06-11 09:16
 * @version: 1.0.0
 */
public class GlobalConstant {
    public static final String KEY_WORD = "undefined";
    public static final TableTypeMeta HDFS = new TableTypeMeta("hdfs", new HashSet<String>(Arrays.asList("parquet", "json", "csv", "image", "text", "xml", "excel")));
    public static final TableTypeMeta TEMP = new TableTypeMeta("temp", new HashSet<String>(Arrays.asList("jsonStr", "script", "csvStr", "mockStream", "console")));
    public static final TableTypeMeta GRAMMAR = new TableTypeMeta("grammar", new HashSet<String>(Arrays.asList("grammar")));
    public static final String TOKEN_SERVER = "token-server";
    public static final int DEFAULT_INT_VALUE = 1;
    public static final String LOGIN_NAME = "appId";
    public static final String APP_NAME = "MLSQL_CONSOLE";
    /**
     * 默认系统标识
     */
    public static final String BIXT_ROOT = "10005";

    public static final int SIXTY = 60;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TOW = 2;

    public static final int TOW_HUNDRED = 200;
    public static final long FILE_MAX_BYTES = 1000 * 60 * 120;


    public static final String DEFAULT_TEMP_PATH = "/tmp/upload/";
    public static final String HTTP_SEPARATED = "/";

    public static final String SEMICOLON = ";";
    public static final String SQL = "sql";


    public static final String CALLBACK = "callback";

    public static final String BACKEND_LIST = "/backend/list";
    public static final String SCRIPT_FILE_INCLUDE = "/api_v1/script_file/include";

    public static final String RUN_SCRIPT = "/run/script";
    public static final String RUN_SQL = "/run/sql";
    public static final String BACKEND_ADD = "/backend/add";
    public static final String BACKEND_REMOVE = "/backend/remove";
    public static final String BACKEND_TAGS_UPDATE = "/backend/tags/update";
    public static final String BACKEND_NAME_CHECK= "/backend/name/check";
    public static final String BACKEND_LIST_NAMES= "/backend/list/names";
    public static final String SUCCESS= "success";
    public static final String PUBLIC= "public/";


    public static final String DOWNLAOD_FAIL= "download fail";
    public static final String API_FILE_DOWNLAOD= "/api_v1/file/download";
    public static final String API_FILE_UPLAOD= "/api_v1/file/upload";
    public static final String SHOW_JOBS= "!show jobs  ";

    public static String STREAMING_DSL_AUTH_META_CLIENT = "streaming.dsl.auth.meta.client.MLSQLConsoleClient";

    public static String TABLE_AUTH = "/api_v1/table/auth";

}
