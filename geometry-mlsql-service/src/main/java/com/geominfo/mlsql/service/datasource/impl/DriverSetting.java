package com.geominfo.mlsql.service.datasource.impl;

import com.geominfo.mlsql.domain.vo.JDBCD;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DriverSetting
 * @author: anan
 * @create: 2020-11-30 14:46
 * @version: 1.0.0
 */
public class DriverSetting {
    String databaseName;
    public DriverSetting(String databaseName){
        this.databaseName = databaseName;
    }
    public void setDriverAndClass(JDBCD jdbcd){
        String dbType = jdbcd.getJType();
        if(dbType.toLowerCase().equals("mysql")){
            jdbcd.setDriver("com.mysql.jdbc.Driver");
        } if(dbType.toLowerCase().equals("oracle")){
            jdbcd.setDriver("oracle.jdbc.driver.OracleDriver");
        } if(dbType.toLowerCase().equals("sqlserver")){
            jdbcd.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } else {

        }
    };
}
