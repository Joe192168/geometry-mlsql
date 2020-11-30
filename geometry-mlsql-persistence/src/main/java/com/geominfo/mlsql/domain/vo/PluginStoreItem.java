package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE接受调用接口信息pojo类
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-26 10:43
 * @version: 1.0.0
 */
@Data
public class PluginStoreItem {

    private Integer id;
    private String name;
    private String path;
    private String version;
    private Integer pluginType;
    private String extraParams;
}
