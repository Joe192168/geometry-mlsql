package com.geominfo.mlsql.domain.vo;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-09 17:43
 * @version: 1.0.0
 */
@Data
public class MlsqlJob {
    private Integer id;
    private String name;
    private String content;
    private Integer status;
    private Long createdAt;
    private Long finishAt;
    private String reason;
    private String response;

}
