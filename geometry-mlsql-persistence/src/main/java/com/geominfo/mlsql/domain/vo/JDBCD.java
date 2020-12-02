package com.geominfo.mlsql.domain.vo;

import lombok.Data;

import javax.sql.DataSource;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: JDBCD
 * @author: anan
 * @create: 2020-11-30 11:58
 * @version: 1.0.0
 */
@Data
public class JDBCD {
    private String jType;
    private String name;
    private String host;
    private String port;
    private String db;
    private String url;
    private String driver;
    private String user;
    private String password;
    private String format;
}
