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

    public static final int MAX_SIZE = 10000;
    public static final int SIXTY = 60;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TOW = 2;
    public static final int ONE_HUNDRED = 100;
    public static final int TOW_HUNDRED = 200;
    public static final int FOUR_HUNDRED = 400;
    public static final int FIVE_HUNDRED = 500;
    public static final long FILE_MAX_BYTES = 1000 * 60 * 120;
    public static final long SINGLE_USER_UPLOAD_DEFAULT_BYTES = 1024L * 1024 * 125;

    public static final String DEFAULT_TEMP_PATH = "/tmp/upload/";
    public static final String HTTP_HEAD_ONE = "http:";
    public static final String HTTP_HEAD_TOW = "http://";
    public static final String HTTP_SEPARATED = "/";

    public static final String ENCODE = "UTF-8";
    public static final String MLSQL_CLUSTER_URL = "mlsql_cluster_url";
    public static final String MLSQL_CLUSTER_DEFAULT_URL = "192.168.20.209:9003";
    public static final String MLSQL_MY_URL = "my_url";
    public static final String MLSQL_MY_DEFAULT_URL =
            "jdbc:mysql://192.168.2.239:3306/mlsql_console?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false";

    public static final String MLSQL_USER_HOME = "user_home";
    public static final String MLSQL_USER_DEFAULT_HOME = "/home/mlsql";
    public static final String SINGLE_USER_UPLOAD_BYTES = "single_user_upload_bytes";
    public static final String ENABLE_AUTH_CENTER = "enable_auth_center";
    public static final String FALSE = "false";
    public static final String TRUE = "true";

    public static final String CORRECT_PATH = "file path is not correct";
    public static final String POINT = ".";
    public static final String POINT2 = "..";
    public static final String SEMICOLON = ";";
    public static final String SQL = "sql";
    public static final String OWNER = "owner";
    public static final String JOB_NAME = "jobName";
    public static final String SESSION_PERUSER = "sessionPerUser";
    public static final String SHOW_STACK = "show_stack";
    public static final String TAGS = "tags";
    public static final String CONTEXT_DEFAULT_INCLUDE_URL = "context.__default__include_fetch_url__";
    public static final String CONTEXT_DEFAULT_FILESERVER_URL = "context.__default__fileserver_url__";
    public static final String CONTEXT_DEFAULT_FILESERVER_UPLOAD_URL = "context.__default__fileserver_upload_url__";
    public static final String CONTEXT_AUTH_SECRET = "context.__auth_secret__";
    public static final String DEFAULTPATHPREFIX = "defaultPathPrefix";
    public static final String ACCEPT = "Accept";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CALLBACK = "callback";
    public static final String ASYNC = "async";
    public static final String ACTION = "action";
    public static final String BACKEND_LIST = "/backend/list";
    public static final String SCRIPT_FILE_INCLUDE = "/api_v1/script_file/include";
    public static final String TEAM_NAME = "teamName";
    public static final String RUN_SCRIPT = "/run/script";
    public static final String RUN_SQL = "/run/sql";
    public static final String BACKEND_ADD = "/backend/add";
    public static final String BACKEND_REMOVE = "/backend/remove";
    public static final String BACKEND_TAGS_UPDATE = "/backend/tags/update";
    public static final String BACKEND_NAME_CHECK= "/backend/name/check";
    public static final String BACKEND_LIST_NAMES= "/backend/list/names";
    public static final String SUCCESS= "success";
    public static final String FILE_NAME= "fileName";
    public static final String USER_NAME= "userName";
    public static final String PUBLIC= "public/";
    public static final String DATA_MLSQL_DATA= "/data/mlsql/data/";
    public static final String APPLICATION_OCTET_STREAM= "application/octet-stream";
    public static final String THE_BACKSLASH= "\"";
    public static final String TAR= ".tar";
    public static final String LEFT_BRACKETS= "[";
    public static final String RIGHT_BRACKETS= "]";
    public static final String READ_FILE= ",读取文件";
    public static final String ENTRY_NAME= " entryName:";
    public static final String DOWNLAOD_FAIL= "download fail";
    public static final String RUN_COMMAND_DOWNLAODEXT= "run command as DownloadExt.`` where from=";
    public static final String AND_TO_TMP_UPLAOD= " and to='/tmp/upload';";
    public static final String RUN_COMMAND_UPLOADFILETOSERVIEREXT= "run command as UploadFileToServerExt.`";
    public static final String TOKEN_NAME_AND_TOKENVALUE= "` where tokenName='access-token' and tokenValue=";
    public static final String API_FILE_DOWNLAOD= "/api_v1/file/download";
    public static final String API_FILE_UPLAOD= "/api_v1/file/upload";
    public static final String AYN_POST_UPLOADCALLBACK_URL= "localhost:8080/uploadcallback";
    public static final String SHOW_JOBS= "!show jobs  ";
    public static String SCHEDULER_TAG_TYPE = "scheduler";
    public static String NORMAL_TAG_TYPE = "normal";
    public static String SCHEDULER = "!scheduler";
    public static String DEFAULT_CONSOLE_URL = "context.__default__console_url__";
    public static String CONTEXT_AUTH_CLIENT = "context.__auth_client__";
    public static String STREAMING_DSL_AUTH_META_CLIENT = "streaming.dsl.auth.meta.client.MLSQLConsoleClient";
    public static String CONTEXT_AUTH_SERVER_URL = "context.__auth_server_url__";
    public static String TABLE_AUTH = "/api_v1/table/auth";
    public static String SKIPAUTH = "skipAuth";
    public static String SKIPGRAMMARVALIDATE = "skipGrammarValidate";
    public static String REORD_EXISTING = "数据库中已存在该记录";
    public static String CODE = "code";
    public static String MSG = "msg";




    public static final String AUTH_SECRET = "auth_secret";
}
