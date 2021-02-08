package com.geominfo.mlsql.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlJob
 * @author: ryan(丁帅波)
 * @create: 2020-11-09 17:43
 * @version: 1.0.0
 */
@Data
@AllArgsConstructor
public class FileInfo {
    private String permission;
    private String owner;
    private String userGroup;
    private String size;
    private String crateDate;
    private String path;

}
