package com.geominfo.mlsql.services.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.geominfo.mlsql.dao.TScriptExecLogDao;
import com.geominfo.mlsql.dao.TScriptExecMetricLogDao;
import com.geominfo.mlsql.domain.po.TScriptExecLog;
import com.geominfo.mlsql.domain.po.TScriptExecMetricLog;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.domain.vo.MlsqlJobsVO;
import com.geominfo.mlsql.services.MlsqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.service.ApiListing;

import java.util.Date;
import java.util.Map;

/**
 * @title: MlsqlServiceImpl
 * @date 2021/4/6 10:24
 */
@Service
public class MlsqlServiceImpl implements MlsqlService {
    @Value(value = "${my_url.mlsqlUrl}")
    String url;

    String runningJobUrl = "/runningjobs";

    String engineUrl = "/health/liveness";

    String executeUrl = "/run/script";

    String killJobUrl = "/killjob";

    @Autowired
    @Qualifier("RestTemplateBean")
    private RestTemplate restTemplate;

    @Autowired
    private TScriptExecLogDao tScriptExecLogDao;

    @Autowired
    private TScriptExecMetricLogDao tScriptExecMetricLogDao;

    @Override
    public String executeMlsql(MlsqlExecuteSqlVO mlsqlExecuteSqlVO) {

        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("sql",mlsqlExecuteSqlVO.getSql());
        map.add("owner",mlsqlExecuteSqlVO.getOwner());
        map.add("jobType",mlsqlExecuteSqlVO.getJobType());
        map.add("executeMode",mlsqlExecuteSqlVO.getExecuteMode());
        map.add("jobName",mlsqlExecuteSqlVO.getJobName());
        map.add("timeout",mlsqlExecuteSqlVO.getTimeout());
        map.add("silence",mlsqlExecuteSqlVO.getSilence());
        map.add("sessionPerUser",mlsqlExecuteSqlVO.getSessionPerUser());
        map.add("async",mlsqlExecuteSqlVO.getAsync());
        map.add("callback",mlsqlExecuteSqlVO.getCallback());
        map.add("skipInclude",mlsqlExecuteSqlVO.getSkipInclude());
        map.add("skipAuth",mlsqlExecuteSqlVO.getSkipAuth());
        map.add("skipGrammarValidate",mlsqlExecuteSqlVO.getSkipGrammarValidate());
        map.add("includeSchema",mlsqlExecuteSqlVO.getIncludeSchema());
        map.add("fetchType",mlsqlExecuteSqlVO.getFetchType());
        map.add("defaultPathPrefix",mlsqlExecuteSqlVO.getDefaultPathPrefix());
        map.add("context.__default__include_fetch_url__",mlsqlExecuteSqlVO.getDefaultIncludeFetchUrl());
        map.add("context.__default__console_url__",mlsqlExecuteSqlVO.getDefaultConsoleUrl());
        map.add("context.context.__auth_client__",mlsqlExecuteSqlVO.getAuthClient());
        map.add("context.__auth_server_url__",mlsqlExecuteSqlVO.getAuthServerUrl());
        map.add("context.__auth_secret__",mlsqlExecuteSqlVO.getAuthSecret());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + executeUrl, request, String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            if (mlsqlExecuteSqlVO.getAsync()) {
                return null;
            }
            return entity.getBody();

        }
        return null;
    }

    @Override
    public JSONObject getAllExecuteJobs() {
        ResponseEntity<JSONObject> Entity = restTemplate.getForEntity(url + runningJobUrl,JSONObject.class);
        if (Entity.getStatusCode().is2xxSuccessful()) {
            return Entity.getBody();
        }else {
            return null;
        }
    }

    @Override
    public String killMlsqlJob(String jobName,String groupId) {

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        if ( !StringUtils.isEmpty(jobName)) {
            map.add("jobName",jobName);
        }else if ( !StringUtils.isEmpty(groupId)){
            map.add("groupId",groupId);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + killJobUrl, request, String.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        }else {
            return null;
        }
    }

    @Override
    public JSONObject getEngineState() {
        ResponseEntity<JSONObject> Entity = restTemplate.getForEntity(url + engineUrl,JSONObject.class);
        if (Entity.getStatusCode().is2xxSuccessful()) {
            return Entity.getBody();
        }else {
            return null;
        }
    }

    @Override
    @Transactional
    public void dealAsyncCallback(Map<String, String> map) {
        String stat = map.get("stat");
        if (stat.equals("succeeded")) {
            TScriptExecLog tScriptExecLog = new TScriptExecLog();
            tScriptExecLog.setExecResult(map.get("res"));
            MlsqlJobsVO jobInfo = JSONObject.parseObject(map.get("jobInfo"), MlsqlJobsVO.class);
            tScriptExecLog.setJobId(jobInfo.getGroupId());
            tScriptExecLog.setScriptContent(jobInfo.getJobContent());
            tScriptExecLog.setExecStatus(map.get("stat"));
            tScriptExecLog.setJobType(jobInfo.getJobType());
            tScriptExecLog.setFinishTime(new Date());
            tScriptExecLog.setJobName(jobInfo.getJobName());
            tScriptExecLogDao.update(tScriptExecLog,new UpdateWrapper<TScriptExecLog>().eq("job_name",tScriptExecLog.getJobName()));

            //根据groupId再次请求获取activeJobs
            JSONArray activeJobsByGroupId = getActiveJobsByGroupId(jobInfo.getGroupId());
            JSONObject jsonObject = (JSONObject) activeJobsByGroupId.get(0);
            Integer jobsCount = jsonObject.getJSONArray("activeJobs").size();
            Integer taskCount = 0;
            Integer stageCount = 0;
            if (jobsCount > 1 ) {
                JSONArray activeJobs = jsonObject.getJSONArray("activeJobs");
                for (Object activeJob : activeJobs) {
                    JSONObject activeJob1 = (JSONObject) activeJob;
                    Integer numTasks = (Integer) activeJob1.get("numTasks");
                    taskCount += numTasks;

                    JSONArray jsonArray = (JSONArray) activeJob1.get("mLSQLScriptJobStage");
                    stageCount += jsonArray.size();
                }
            }else {
                JSONArray activeJobs = jsonObject.getJSONArray("activeJobs");
                JSONObject jsonObject1 = (JSONObject) activeJobs.get(0);
                taskCount =  (Integer)jsonObject1.get("numTasks");
                JSONArray jsonArray = (JSONArray) jsonObject1.get("mLSQLScriptJobStage");
                stageCount = jsonArray.size();
            }


            //插入运行指标表
            TScriptExecMetricLog tScriptExecMetricLog = new TScriptExecMetricLog();
            tScriptExecMetricLog.setJobId(jobInfo.getGroupId());
            tScriptExecMetricLog.setExplainMsg(map.get("explainMsg"));
            tScriptExecMetricLog.setCreateTime(jobInfo.getStartTime());
            tScriptExecMetricLog.setSparkUiJobCnt(jobsCount);
            tScriptExecMetricLog.setSparkUiStageCnt(stageCount);
            tScriptExecMetricLog.setSparkUiTaskCnt(taskCount);
            tScriptExecMetricLogDao.insert(tScriptExecMetricLog);
        }else {
            TScriptExecLog tScriptExecLog = new TScriptExecLog();
            MlsqlJobsVO jobInfo = JSONObject.parseObject(map.get("jobInfo"), MlsqlJobsVO.class);
            tScriptExecLog.setJobId(jobInfo.getGroupId());
            tScriptExecLog.setScriptContent(jobInfo.getJobContent());
            tScriptExecLog.setExecStatus(map.get("stat"));
            tScriptExecLog.setOperatorTime(jobInfo.getStartTime());
            tScriptExecLog.setKeyMsg(map.get("msg"));
            tScriptExecLog.setJobType(jobInfo.getJobType());
            tScriptExecLogDao.update(tScriptExecLog,new UpdateWrapper<TScriptExecLog>().eq("job_id",tScriptExecLog.getJobName()));

        }
    }

    public JSONArray getActiveJobsByGroupId(String groupId) {
        MlsqlExecuteSqlVO mlsqlExecuteSqlVO = new MlsqlExecuteSqlVO();
        mlsqlExecuteSqlVO.setSql("load _mlsql_.`jobs/v3/"+groupId+"` as wow;");
        return JSONObject.parseObject(executeMlsql(mlsqlExecuteSqlVO),JSONArray.class);
    }
}
