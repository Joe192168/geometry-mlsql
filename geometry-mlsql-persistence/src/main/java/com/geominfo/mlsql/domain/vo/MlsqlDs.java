package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: geometry-mlsql
 * @description: MlsqlDs
 * @author: BJZ
 * @create: 2020-11-26 14:40
 * @version: 1.0.0
 */
@Data
public class MlsqlDs {
    private  int id ;
    private String name;
    private String format ;
    private String params;
    private int mlsqlUserId;

    public MlsqlDs(){}

    public MlsqlDs(String name, String format, String params, int mlsqlUserId) {
        this.name = name;
        this.format = format;
        this.params = params;
        this.mlsqlUserId = mlsqlUserId;
    }
}