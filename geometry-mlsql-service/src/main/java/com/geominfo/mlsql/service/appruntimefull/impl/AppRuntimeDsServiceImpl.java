package com.geominfo.mlsql.service.appruntimefull.impl;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.mapper.AppRuntimeMapper;
import com.geominfo.mlsql.domain.appruntimefull.wConnectTable;
import com.geominfo.mlsql.service.appruntimefull.AppRuntimeDsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: mlsql启动数据源信息接口实现
 * @author: ryan
 * @create: 2020-11-10 09:27
 * @version: 1.0.0
 */
@Service
public class AppRuntimeDsServiceImpl implements AppRuntimeDsService {

    @Autowired
    private AppRuntimeMapper appRuntimeMapper;

    @Override
    public void insertAppDS(wConnectTable wct) {
        appRuntimeMapper.insertAppDS(wct);
    }

    @Override
    public List<wConnectTable> getAppRuntimeList() {
        return appRuntimeMapper.getAppRuntimeList();
    }

    @Override
    public wConnectTable getWConnectTable(JDBCD connectParams) {
        HashMap<String, String> map = new HashMap<>();
        map.put("format", connectParams.getFormat());
        map.put("url",connectParams.getUrl());
        map.put("driver",connectParams.getDriver());
        map.put("user",connectParams.getUser());
        map.put("password",connectParams.getPassword());
        wConnectTable wct = new wConnectTable();
        wct.setFormat(connectParams.getFormat());
        wct.setDb(connectParams.getName());
        wct.setOptions(JSONObject.toJSONString(map));
        return wct;
    }

    @Override
    public String jointConnectPrams(JDBCD connectParams) {
        String connectSQL = "";
        if (connectParams.getJType().equals("mysql")) {
            connectSQL = "connect jdbc where driver = \"com.mysql.jdbc.Driver\" and url = \"" + connectParams.getUrl() +
                    "\" and user = \"" + connectParams.getUser() + "\" and password = \"" + connectParams.getPassword() +
                    "\" as " + connectParams.getName() + ";";
        } else if (connectParams.getJType().equals("oracle")) {
            connectSQL = "connect jdbc where driver = \"oracle.jdbc.driver.OracleDriver\" and url = \"" + connectParams.getUrl() +
                    "\" and user = \"" + connectParams.getUser() + "\" and password = \"" + connectParams.getPassword() +
                    "\" as " + connectParams.getName() + ";";
        } else if (connectParams.getJType().equals("sqlserver")) {
            connectSQL = "connect jdbc where driver = \"com.microsoft.sqlserver.jdbc.SQLServerDriver\" and url = \"" + connectParams.getUrl() +
                    "\" and user = \"" + connectParams.getUser() + "\" and password = \"" + connectParams.getPassword() +
                    "\" as " + connectParams.getName() + ";";
        } else if (connectParams.getJType().equals("hbase")) {
            connectSQL = "connect hbase where `zk` = \"" + connectParams.getHost() + ":" + connectParams.getPort() +
                    "\" and `family` = \"" + connectParams.getFamily() + "\" as " + connectParams.getName() + "; \n " +
                    "load hbase.`" + connectParams.getName() +":SYSTEM.CATALOG` as mlsql_example;";
        }
        return connectSQL;
    }

    @Override
    public void updateConnectParams(wConnectTable wct) {
        appRuntimeMapper.updateConnectParams(wct);
    }
}
