package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlRoleMember
 * @author: anan
 * @create: 2020-07-09 16:24
 * @version: 1.0.0
 */
@Data
public class MlsqlRoleMember {
    private int id;
    private int userId;
    private int groupRoleId;
    private MlsqlUser mlsqlUser;
    private MlsqlGroupRole mlsqlGroupRole;
}
