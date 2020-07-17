package com.geominfo.mlsql.globalconstant;

import com.geominfo.mlsql.domain.vo.TableTypeMeta;
import org.springframework.beans.factory.annotation.Autowired;

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
}
