package com.geominfo.mlsql.domain.vo;


import lombok.Data;


/**
 * @program: MLSQL CONSOLE 工坊信息pojo类
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-16 10:34
 * @version: 1.0.0
 */

@Data
public class MlsqlWorkshopTable {

    private Integer id;
    private String tableName;
    private String content;
    private Integer mlsqlUserId;
    private String sessionId;
    private Integer status;
    private String tableSchema;
    private String jobName;

}
