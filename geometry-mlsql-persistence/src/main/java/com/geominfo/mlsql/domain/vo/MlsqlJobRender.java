package com.geominfo.mlsql.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlJobRender
 * @author: ryan(丁帅波)
 * @create: 2020-11-00 15:43
 * @version: 1.0.0
 */

@Data
@AllArgsConstructor
@ApiModel(value = "MlsqlJob")
public class MlsqlJobRender {
    private Integer id;
    private String name;
    private String content;
    private String status;
    private String reason;
    private String createdAt;
    private String finishAt;
    private String response;
}
