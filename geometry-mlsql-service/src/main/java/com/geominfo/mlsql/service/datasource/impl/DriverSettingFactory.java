package com.geominfo.mlsql.service.datasource.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DriverSettingFactory
 * @author: anan
 * @create: 2020-11-30 14:46
 * @version: 1.0.0
 */
@Service
public class DriverSettingFactory {
    Map<String, DriverSetting> driverSettingMap = new HashMap<String, DriverSetting>();
    public DriverSetting getDriverSetting(String databaseName){
        if(driverSettingMap.containsKey(databaseName) == false){
            driverSettingMap.put(databaseName,new DriverSetting(databaseName));
        }
        return driverSettingMap.get(databaseName.toLowerCase());
    }
}
