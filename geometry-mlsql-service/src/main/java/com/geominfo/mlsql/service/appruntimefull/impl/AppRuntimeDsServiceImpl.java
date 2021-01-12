package com.geominfo.mlsql.service.appruntimefull.impl;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.mapper.AppRuntimeMapper;
import com.geominfo.mlsql.domain.appruntimefull.WConnectTable;
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
    public void insertAppDS(WConnectTable wct) {
        appRuntimeMapper.insertAppDS(wct);
    }

    @Override
    public List<WConnectTable> getAppRuntimeList() {
        return appRuntimeMapper.getAppRuntimeList();
    }

    @Override
    public WConnectTable getWConnectTable(JDBCD connectParams) {
        HashMap<String, String> map = new HashMap<>();
        if (connectParams.getJType().equals("hbase")){
            map.put("format","hbase");
            map.put("family",connectParams.getFamily());
            map.put("zk",connectParams.getUrl());
        } else if (connectParams.getFormat().equals("jdbc")) {
            map.put("format", connectParams.getFormat());
            map.put("url",connectParams.getUrl());
            map.put("driver",connectParams.getDriver());
            map.put("user",connectParams.getUser());
            map.put("password",connectParams.getPassword());
        }
        WConnectTable wct = new WConnectTable();
        wct.setFormat(connectParams.getFormat());
        wct.setDb(connectParams.getName());
        wct.setOptions(JSONObject.toJSONString(map));
        return wct;
    }

    @Override
    public String jointConnectPrams(JDBCD connectParams) {
        String connectSQL = "";
        if (connectParams.getJType().equals("mysql")) {
            connectSQL = connectParams("com.mysql.jdbc.Driver",connectParams);
        } else if (connectParams.getJType().equals("oracle")) {
            connectSQL = connectParams("oracle.jdbc.driver.OracleDriver",connectParams);
        } else if (connectParams.getJType().equals("sqlserver")) {
            connectSQL = connectParams("com.microsoft.sqlserver.jdbc.SQLServerDriver",connectParams);
        } else if (connectParams.getJType().equals("hbase")) {
            connectSQL = "connect hbase where `zk` = \"" + connectParams.getUrl() +
                    "\" and `family` = \"" + connectParams.getFamily() + "\" as " + connectParams.getName() + "; \n " +
                    "load hbase.`" + connectParams.getName() +":SYSTEM.CATALOG` as mlsql_example;";
        }
        return connectSQL;
    }

    public String connectParams(String driver,JDBCD connectParams) {
        return "connect jdbc where driver = \""+driver+"\" and url = \"" + connectParams.getUrl() +
                "\" and user = \"" + connectParams.getUser() + "\" and password = \"" + connectParams.getPassword() +
                "\" as " + connectParams.getName() + ";";
    }

    @Override
    public void updateConnectParams(WConnectTable wct) {
        appRuntimeMapper.updateConnectParams(wct);
    }
}
