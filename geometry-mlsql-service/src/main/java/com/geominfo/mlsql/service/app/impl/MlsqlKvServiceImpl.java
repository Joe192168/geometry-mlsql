package com.geominfo.mlsql.service.app.impl;

import com.geominfo.mlsql.domain.vo.AppKv;
import com.geominfo.mlsql.mapper.AppMapper;
import com.geominfo.mlsql.service.app.MlsqlKvService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 应用信息接口实现
 * @author: ryan
 * @create: 2020-11-30 11:30
 * @version: 1.0.0
 */
@Service
public class MlsqlKvServiceImpl implements MlsqlKvService {

    public final static String LOGIN = "login";
    public final static String CONFIGURED = "configured";
    public final static String REGISTER = "register";
    public final static String CONSOLE = "console";

    @Autowired
    private AppMapper appMapper;

    @Override
    public Map<String, Boolean> appInfo() {
        Map<String, Boolean> map = new HashMap<>();
        List<AppKv> appKvs = appMapper.appInfo();
        for (AppKv appKv : appKvs){
            map.put(appKv.getName(),Boolean.parseBoolean(appKv.getValue()));
        }
        return map;
    }

    @Override
    public String updateApp(String name, String value) {
        int result = appMapper.updateApp(name, value);
        return result > 0? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }

    @Override
    public String addApp(String name, String value) {
        int result = appMapper.addApp(name, value);
        return result > 0? InterfaceReturnInformation.SUCCESS: InterfaceReturnInformation.FAILED;
    }
}
