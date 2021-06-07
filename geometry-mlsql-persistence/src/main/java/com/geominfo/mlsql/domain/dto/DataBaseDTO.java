package com.geominfo.mlsql.domain.dto;

import lombok.Data;

@Data
public class DataBaseDTO {

    /**
     * 数据库类型
     */
    String dataBaseType;
    /**
     * 数据库ip
     */
    String ip;
    /**
     * 数据库端口
     */
    String port;
    /**
     * 数据库名称
     */
    String dbname;
    /**
     * 数据库用户
     */
    String username;
    /**
     * 数据库密码
     */
    String password;
    /**
     * 表名称
     */
    String tableName;

}
