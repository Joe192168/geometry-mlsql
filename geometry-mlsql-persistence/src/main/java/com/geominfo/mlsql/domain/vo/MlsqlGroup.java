package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlGroup
 * @author: anan
 * @create: 2020-07-09 16:17
 * @version: 1.0.0
 */
@Data
public class MlsqlGroup  implements Serializable {
    private int id;
    private String name;
    private List<MlsqlGroupUser> mlsqlGroupUsers;
    private List<MlsqlGroupRole> mlsqlGroupRoles;
    private List<MlsqlGroupTable> mlsqlGroupTables;
    private List<MlsqlBackendProxy> mlsqlBackendProxies;
}
