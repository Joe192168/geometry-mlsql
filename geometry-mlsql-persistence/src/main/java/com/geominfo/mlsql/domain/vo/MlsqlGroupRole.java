package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlGroupRole
 * @author: anan
 * @create: 2020-07-09 16:21
 * @version: 1.0.0
 */
@Data
public class MlsqlGroupRole {
    private int id;
    private String name;
    private int groupId;
}
