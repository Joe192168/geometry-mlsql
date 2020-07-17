package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlGroupTable
 * @author: anan
 * @create: 2020-07-09 16:28
 * @version: 1.0.0
 */
@Data
public class MlsqlGroupTable {
    private int id;
    private int tableId;
    private int groupId;
    private MLSQLTable mlsqlTable;
    private MlsqlGroup mlsqlGroup;
}
