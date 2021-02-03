package com.geominfo.mlsql.service.app.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.service.app.AppFunctionService;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.utils.ParamsUtil;
import com.geominfo.mlsql.utils.metadb.util.PrintUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.SneakyThrows;
import org.apache.commons.collections.list.SynchronizedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Int;
import sun.rmi.runtime.Log;


import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


/**
 * @program: geometry-mlsql
 * @description: AppFunctionServiceImpl (spark 系统函数详解地址：https://spark.apache.org/docs/latest/api/sql/index.html）
 * @author: BJZ
 * @create: 2021-01-19 09:52
 * @version: 1.0.0
 */
@Service
public class AppFunctionServiceImpl extends BaseServiceImpl implements AppFunctionService {

    @Autowired
    private ClusterService clusterService;

    private Map<Integer, Object> resMap = new ConcurrentHashMap<>();
    private Map<String, Object> paramMap = new ConcurrentHashMap<>();
    private List<String> allCacheList = new ArrayList<>();
    private Map<String, Object> findByNameCacheMap = new ConcurrentHashMap<>();

    private static final String SCRIPT_FUNCTIONS = "!show functions;";
    private static final String SCRIPT_FUNCTION_NAME = "!show function ";


    @Override
    public <T> T find(String fName) {
        if (fName == null)
            return findAll();
        else if (fName.startsWith("*") && fName.endsWith("*")) {
            return findVague(fName);
        } else
            return findByName(fName);

    }

    private <T> T findAll() {
        if (allCacheList.size() == 0) {
            String requestRes = postRequest(SCRIPT_FUNCTIONS);
            JSONArray jsonArray = JSON.parseArray(requestRes);
            for (int i = 0; i < jsonArray.size(); i++) {
                String res = ((JSONObject) jsonArray.get(i)).get("function").toString();
                //如果缓存中有，直接拿
                if (!allCacheList.contains(res))
                    allCacheList.add(res);
            }
        }
        //加入缓存
        resMap.put(200, allCacheList);
        return (T) resMap;
    }

    private <T> T findByName(String name) {

        if (!findByNameCacheMap.containsKey(name) ||
                findByNameCacheMap.containsKey(name) && findByNameCacheMap.get(name).equals("")) {
            String requestRes = postRequest(SCRIPT_FUNCTION_NAME + "\"" + name + "\" ;");
            resMap.put(200, requestRes);
            findByNameCacheMap.put(name, requestRes);
        } else
            //从缓存拿
            resMap.put(200, findByNameCacheMap.get(name));

        return (T) resMap;
    }

    private <T> T findVague(String vagueStr) {

        if (allCacheList.size() == 0)
            findAll();

        ArrayList<String> vagueList = new ArrayList<>();

        String tmp = vagueStr.trim();
        String target = tmp.substring(1, tmp.length() - 1);
        for (String curStr : allCacheList)
            if (curStr.contains(target))
                vagueList.add(curStr);

        //加入缓存
        resMap.put(200, vagueList);

        return (T) resMap;

    }

    private String postRequest(String sql) {
        paramMap.put("sql", sql);
        paramMap.put("owner", ParamsUtil.getParam("owner", "admin"));
        paramMap.put("skipConnect", "true");
        paramMap.put("async", "false");
        String requestRes = ((Map<Integer, Object>) clusterService.runScript(paramMap)).get(200).toString();
        return requestRes;
    }


}

