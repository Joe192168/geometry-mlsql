package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE 应用信息pojo类
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-19 15：00
 * @version: 1.0.0
 */

@Data
public class MlsqlApply {
    private Integer id;
    private String name;
    private String content;
    private Integer status;
    private Integer mlsqlUserId;
    private String reason;
    private Long createdAt;
    private Long finishAt;
    private String response;
    private String applySql;
}
