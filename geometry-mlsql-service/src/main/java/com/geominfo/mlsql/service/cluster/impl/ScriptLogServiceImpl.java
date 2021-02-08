package com.geominfo.mlsql.service.cluster.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.ScriptExeLog;
import com.geominfo.mlsql.domain.vo.ScriptRun;
import com.geominfo.mlsql.mapper.ScriptExeLogMapper;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.service.cluster.ScriptLogService;
import com.geominfo.mlsql.utils.ParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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
    private static final String LOGICALEXECUTIONPLAN = "logicalExecutionPlan";
    private static final String JSON_IS_EMPTY = "json is empty!" ;

    @Override
    public <T> T addLog(T t) {
        Map<Integer, Object> result = new ConcurrentHashMap<>();

        try {
            String resJson = t.toString();
             if (resJson.contains(ACTIVEJOBS) && resJson.contains(LOGICALEXECUTIONPLAN))
                return (T) result.put(200 ,insertLog(resJson));
             else if (!resJson.equals("[]") && !ParamsUtil.getParam("sql","").toString().contains("!show"))
                 postScript(LOG_SCRIPT + ParamsUtil.getParam("groupId" ,"") + "` as wow ;");

            else
                return (T) result.put(500, JSON_IS_EMPTY);

        } catch (Exception e) {
            e.printStackTrace();
            return (T) result.put(500, e.getMessage());
        }

        return (T) result.put(500, "error");
    }

    private String insertLog(String json) {
        if(!checkJson(json))  return JSON_IS_EMPTY;
        JSONArray ja = JSON.parseArray(json);
        if (ja.size() == 0) return JSON_IS_EMPTY;
        JSONObject mainJo = ja.getJSONObject(0);

        JSONArray actionJsonArray = mainJo.getJSONArray("activeJobs");
        if (actionJsonArray.size() == 0) return JSON_IS_EMPTY;
        JSONObject aJo = actionJsonArray.getJSONObject(0);

        ScriptExeLog se = new ScriptExeLog();
        se.setGroupId(mainJo.getString("groupId"));

        se.setJob(aJo.getInteger("job"));
        se.setDuration(aJo.getInteger(  "duration"));
        se.setJobId(aJo.getString(  "jobId"));
        se.setStages(aJo.getInteger("stages"));
        se.setTasks(aJo.getInteger("numTasks"));
        se.setInPutSum(aJo.getInteger("inPutSum"));
        se.setInPutByte(aJo.getString("inPutByte"));
        se.setOutPutSum(aJo.getInteger("outPutSum"));
        se.setOutPutBtye(aJo.getString("outPutBtye"));
        se.setLogicalExecutionPlan(aJo.getString("logicalExecutionPlan"));
        se.setPhysicalExecutionPlan(aJo.getString("physicalExecutionPlan"));

        scriptExeLogMapper.addLog(se);



        return "success";
    }


    private boolean checkJson(String json) {
        if (json == null || json.equals("") || json.equals("[]")) return false;
        return  true ;
    }

    private void postScript(String sql) throws Exception {
        Map<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("sql", sql);
        paramMap.put("owner", ParamsUtil.getParam("owner", "admin"));
        clusterService.runScript(paramMap);
    }


}