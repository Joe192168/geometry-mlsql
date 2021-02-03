package com.geominfo.mlsql.service.plugin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.service.app.AppFunctionService;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.service.plugin.PluginFunctionService;
import com.geominfo.mlsql.utils.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: geometry-mlsql
 * @description: PluginFunctionServiceImpl
 * @author: BJZ
 * @create: 2021-01-27 09:52
 * @version: 1.0.0
 */
@Service
public class PluginFunctionServiceImpl extends BaseServiceImpl implements PluginFunctionService {

    @Autowired
    private ClusterService clusterService;

    private Map<String, Object> allCacheMap = new ConcurrentHashMap<>();
    private Map<String, Object> paramMap = new ConcurrentHashMap<>();
    private Map<Integer, Object> resMap = new ConcurrentHashMap<>();

    private static final String SCRIPT_PLUGIN = "!show et;";
    private static final String SCRIPT_PLUGIN_NAME = "!show et ";


    @Override
    public <T> T find(String pName) {
        if (pName == null)
            return findAll();
        else if (pName.startsWith("*") && pName.endsWith("*"))
            return findVague(pName);
         else
            return findByName(pName);

    }

    private <T> T findAll() {
        if (allCacheMap.size() == 0) {
            String requestRes = postRequest(SCRIPT_PLUGIN);
            JSONArray jsonArray = JSON.parseArray(requestRes);
            for (int i = 0; i < jsonArray.size(); i++) {
                String res = ((JSONObject) jsonArray.get(i)).get("name").toString();
                if (!allCacheMap.containsKey(res))
                    allCacheMap.put(res, jsonArray.get(i));
            }
        }
        //缓存
        resMap.put(200, allCacheMap);
        return (T) resMap;
    }

    private <T> T findByName(String name) {

        if (!allCacheMap.containsKey(name)) {
            String requestRes = postRequest(SCRIPT_PLUGIN_NAME + "\"" + name + "\" ;");
            resMap.put(200, requestRes);
        } else
            //从缓存拿
            resMap.put(200, allCacheMap.get(name));

        return (T) resMap;
    }

    private <T> T findVague(String vagueStr) {

        if (allCacheMap.size() == 0)
            findAll();

        ArrayList<String> vagueList = new ArrayList<>();
        String tmp = vagueStr.trim();
        String target = tmp.substring(1, tmp.length() - 1);
        for (Map.Entry entry : allCacheMap.entrySet())
            if (entry.getKey().toString().contains(target))
                vagueList.add(entry.getValue().toString());

        //加入缓存
        resMap.put(200, vagueList);

        return (T) resMap;

    }

    private String postRequest(String sql) {
        paramMap.put("sql", sql);
        paramMap.put("owner", ParamsUtil.getParam("owner", "admin"));
        paramMap.put("async", "false");
        Map<Integer, Object>  res = clusterService.runScript(paramMap);
        return res.containsKey(200) ? res.get(200).toString() : "";
    }


}