package com.geominfo.mlsql.domain.appruntimefull;

import lombok.Data;

/**
 * @program: MLSQL CONSOLE应用pojo类
 * @description: AppKv
 * @author: ryan(丁帅波)
 * @create: 2021-1-05 14:27
 * @version: 1.0.0
 */
@Data
public class WConnectTable {
    private Integer id;
    private String format;
    private String db;
    private String options;
}
