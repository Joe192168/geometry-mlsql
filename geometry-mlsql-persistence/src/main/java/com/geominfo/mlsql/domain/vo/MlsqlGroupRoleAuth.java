package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlGroupRoleAuth
 * @author: anan
 * @create: 2020-07-09 16:25
 * @version: 1.0.0
 */
@Data
public class MlsqlGroupRoleAuth {
    private MlsqlGroupRole mlsqlGroupRole;
    private MLSQLTable mlsqlTable;
    private String operateType;
    private int id;
    private  int tableId;
    private  int groupRoleId;

}
