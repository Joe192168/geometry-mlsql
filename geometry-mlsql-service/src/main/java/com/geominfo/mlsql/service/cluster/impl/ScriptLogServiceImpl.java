package com.geominfo.mlsql.service.cluster.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.ScriptExeLog;
import com.geominfo.mlsql.mapper.ScriptExeLogMapper;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.service.cluster.ScriptLogService;
import com.geominfo.mlsql.utils.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: geometry-mlsql
 * @description: ScriptLogServiceImpl
 * @author: BJZ
 * @create: 2021-01-06 10:29
 * @version: 1.0.0
 */
@Service
public class ScriptLogServiceImpl implements ScriptLogService {

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private ScriptExeLogMapper scriptExeLogMapper;

    private static final String LOG_SCRIPT = "load _mlsql_.`jobs/v2/";
    private static final String ACTIVEJOBS = "activeJobs";
    private static final String LOGICALEXECUTIONPLAN = "mLSQLScriptJobStage";
    private static final String JSON_IS_EMPTY = "json is empty!";

    @Override
    public <T> T addLog(T t) {
        Map<Integer, Object> result = new ConcurrentHashMap<>();

        try {
            String resJson = t.toString();
            if (resJson.contains(ACTIVEJOBS) && resJson.contains(LOGICALEXECUTIONPLAN))
                return (T) result.put(200, insertLog(resJson));
            else if (!resJson.equals("[]") && !ParamsUtil.getParam("sql", "").toString().contains("!show")) {
                String tmp = LOG_SCRIPT + ParamsUtil.getParam("groupId", "") + "` as wow ;";
                postScript(tmp);
            } else
                return (T) result.put(500, JSON_IS_EMPTY);

        } catch (Exception e) {
            e.printStackTrace();
            return (T) result.put(500, e.getMessage());
        }
        return (T) result.put(500, "error");
    }


    @Override
    public <T> T addExecutionPlan(String executionPlan, String groupId) {
        ScriptExeLog se = new ScriptExeLog();
        se.setJobId(groupId);
        se.setExplainMsg(executionPlan);
        se.setCreateTime(new Date(System.currentTimeMillis()));
        scriptExeLogMapper.addLog(se);
        Map<Integer, Object> result = new ConcurrentHashMap<>();
        return (T) result.put(200, "success");

    }

    private String insertLog(String json) {
        if (!checkJson(json)) return JSON_IS_EMPTY;
        JSONArray ja = JSON.parseArray(json);
        if (ja.size() == 0) return JSON_IS_EMPTY;
        JSONObject mainJo = ja.getJSONObject(0);

        JSONArray actionJsonArray = mainJo.getJSONArray("activeJobs");
        if (actionJsonArray.size() == 0) return JSON_IS_EMPTY;

        String groupID = mainJo.getString("groupId");
        if (groupID.isEmpty()) throw new IllegalArgumentException("groupId is empty!");

        List<ScriptExeLog> scriptExeLogsList = new ArrayList<>();
        int stages = 0;
        int tasks = 0;
        ScriptExeLog se = new ScriptExeLog();
        se.setJobId(groupID);
        se.setExplainMsg(scriptExeLogMapper.findByGroupID(groupID));
        int jobs = actionJsonArray.size();
        se.setSparkUiJobCnt(jobs);

        for (int i = 0; i < jobs; i++) {
            JSONObject aJo = actionJsonArray.getJSONObject(i);
            tasks += aJo.getInteger("numTasks");
            JSONArray stageArray = aJo.getJSONArray("mLSQLScriptJobStage");
            stages += stageArray.size();
        }

        se.setSparkUiStageCnt(stages);
        se.setSparkUiTaskCnt(tasks);
        se.setExtraOpts(mainJo.toString());

        scriptExeLogsList.add(se);

        scriptExeLogMapper.delByGroupID(groupID);
        scriptExeLogMapper.batchInsert(scriptExeLogsList);

        return "success";
    }


    private boolean checkJson(String json) {
        if (json == null || json.equals("") || json.equals("[]")) return false;
        return true;
    }

    private void postScript(String sql) {
        Map<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("sql", sql);
        paramMap.put("owner", ParamsUtil.getParam("owner", "admin"));
        paramMap.put("skipConnect", "true");
        clusterService.runScript(paramMap);
    }

}