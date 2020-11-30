package com.geominfo.mlsql.service.app;

import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 应用接口
 * @author: ryan
 * @create: 2020-11-30 11:29
 * @version: 1.0.0
 */
public interface MlsqlKvService {

    /**
     * description: 查询信息存储map
     * author: ryan
     * date: 2020/11/30
     * param:
     * return:map
     */
    Map<String,Boolean> appInfo();

    String updateApp(String name, String value);

    String addApp(String name, String value);

}
