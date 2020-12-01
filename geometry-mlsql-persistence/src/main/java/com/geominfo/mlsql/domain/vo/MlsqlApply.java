package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: geometry-mlsql
 * @description: MlsqlApply
 * @author: BJZ
 * @create: 2020-11-30 14:04
 * @version: 1.0.0
 */
@Data
public class MlsqlApply {

    private int id;
    private String name ;
    private String content ;
    private int status;
    private int mlsqlUserId;
    private String reason ;
    private Long createdAt ;
    private Long finishAt ;
    private String response;
    private String applySql ;

    public MlsqlApply(String name,
                      String content,
                      int status,
                      int mlsqlUserId,
//                      String reason,
                      Long createdAt,
                      Long finishAt,
                      String response,
                      String applySql) {
        this.name = name;
        this.content = content;
        this.status = status;
        this.mlsqlUserId = mlsqlUserId;
//        this.reason = reason;
        this.createdAt = createdAt;
        this.finishAt = finishAt;
        this.response = response;
        this.applySql = applySql;
    }
}