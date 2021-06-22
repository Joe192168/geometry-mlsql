package com.geominfo.mlsql.domain.dto;

import com.geominfo.mlsql.utils.DtaBaseUtil;
import lombok.Data;
import sun.util.resources.cldr.uk.CurrencyNames_uk;

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
    /***
     * 连接名
     */
    String connectName;

}
