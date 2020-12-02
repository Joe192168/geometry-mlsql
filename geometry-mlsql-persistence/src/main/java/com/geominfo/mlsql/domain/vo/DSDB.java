package com.geominfo.mlsql.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @program: MLSQL CONSOLE应用pojo类
 * @description: AppKv
 * @author: ryan(丁帅波)
 * @create: 2020-11-30 10:48
 * @version: 1.0.0
 */
@Data
@AllArgsConstructor
public class DSDB {
    private String name;
    private String db;
    private Set<String> tables;
}
