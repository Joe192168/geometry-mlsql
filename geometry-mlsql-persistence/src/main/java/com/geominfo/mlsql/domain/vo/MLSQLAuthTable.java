package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MLSQLAuthTable
 * @author: anan
 * @create: 2020-06-10 16:25
 * @version: 1.0.0
 */
@Data
public class MLSQLAuthTable {
    private int id;
    private String teamName;
    private String roleName;
    private String db;
    private String tableName;
    private String tableType;
    private String sourceType;
    private String operateType;
}
