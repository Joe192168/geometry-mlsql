package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlBackendProxy
 * @author: anan
 * @create: 2020-07-14 09:29
 * @version: 1.0.0
 */
@Data
public class MlsqlBackendProxy {
    private int id;
    private int groupId;
    private String backendName;
}
