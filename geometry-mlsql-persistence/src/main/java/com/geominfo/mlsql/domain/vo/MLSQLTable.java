package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MLSQLTable
 * @author: anan
 * @create: 2020-06-10 11:26
 * @version: 1.0.0
 */
@Data
public class MLSQLTable implements Serializable{
    private String db;
    private String table;
    private Set<String> columns;
    private Map<String, String> operateType;
    private String sourceType;
    private Map<String, String> tableType;
}
