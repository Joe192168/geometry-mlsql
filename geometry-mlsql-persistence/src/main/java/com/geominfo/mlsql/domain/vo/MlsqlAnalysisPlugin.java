package com.geominfo.mlsql.domain.vo;


import lombok.Data;

/**
 * @program: MLSQL CONSOLE 应用信息pojo类
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-20 09:50
 * @version: 1.0.0
 */
@Data
public class MlsqlAnalysisPlugin {

    private Integer id;
    private String name;
    private String content;
    private Integer mlsqlUserId;

}
