package com.geominfo.mlsql.services.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.constant.systemidentification.SystemCustomIdentification;
import com.geominfo.mlsql.dao.TEtFunctionInfoDao;
import com.geominfo.mlsql.dao.TScriptExecLogDao;
import com.geominfo.mlsql.dao.TScriptExecMetricLogDao;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TEtFunctionInfo;
import com.geominfo.mlsql.domain.po.TScriptExecLog;
import com.geominfo.mlsql.domain.po.TScriptExecMetricLog;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.systemidentification.SystemTableName;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.domain.vo.MlsqlJobsVO;
import com.geominfo.mlsql.domain.vo.ScriptContentVO;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.utils.TreeDataProcessor;
import com.geominfo.mlsql.utils.TreeVo;
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

import java.math.BigDecimal;
import java.util.*;

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

    String getJobUrl = "running/getLog";

    String checkRunningJobUrl = "running/jobName";

    @Autowired
    @Qualifier("RestTemplateBean")
    private RestTemplate restTemplate;

    @Autowired
    private TScriptExecLogDao tScriptExecLogDao;

    @Autowired
    private TScriptExecMetricLogDao tScriptExecMetricLogDao;

    @Autowired
    private TSystemResourcesDao tSystemResourcesDao;

    @Autowired
    private TEtFunctionInfoDao tEtFunctionInfoDao;

    @Autowired
    private NumberControlService numberControlService;

    @Override
    public String executeMlsql(MlsqlExecuteSqlVO mlsqlExecuteSqlVO) {

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("sql", mlsqlExecuteSqlVO.getSql());
        map.add("owner", mlsqlExecuteSqlVO.getOwner());
        map.add("jobType", mlsqlExecuteSqlVO.getJobType());
        map.add("executeMode", mlsqlExecuteSqlVO.getExecuteMode());
        map.add("jobName", mlsqlExecuteSqlVO.getJobName());
        map.add("timeout", mlsqlExecuteSqlVO.getTimeout());
        map.add("silence", mlsqlExecuteSqlVO.getSilence());
        map.add("sessionPerUser", mlsqlExecuteSqlVO.getSessionPerUser());
        map.add("async", mlsqlExecuteSqlVO.getAsync());
        map.add("callback", mlsqlExecuteSqlVO.getCallback());
        map.add("skipInclude", mlsqlExecuteSqlVO.getSkipInclude());
        map.add("skipAuth", mlsqlExecuteSqlVO.getSkipAuth());
        map.add("skipGrammarValidate", mlsqlExecuteSqlVO.getSkipGrammarValidate());
        map.add("includeSchema", mlsqlExecuteSqlVO.getIncludeSchema());
        map.add("fetchType", mlsqlExecuteSqlVO.getFetchType());
        map.add("defaultPathPrefix", mlsqlExecuteSqlVO.getDefaultPathPrefix());
        map.add("context.__default__include_fetch_url__", mlsqlExecuteSqlVO.getDefaultIncludeFetchUrl());
        map.add("context.__default__console_url__", mlsqlExecuteSqlVO.getDefaultConsoleUrl());
        map.add("context.context.__auth_client__", mlsqlExecuteSqlVO.getAuthClient());
        map.add("context.__auth_server_url__", mlsqlExecuteSqlVO.getAuthServerUrl());
        map.add("context.__auth_secret__", mlsqlExecuteSqlVO.getAuthSecret());


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
        ResponseEntity<JSONObject> Entity = restTemplate.getForEntity(url + runningJobUrl, JSONObject.class);
        if (Entity.getStatusCode().is2xxSuccessful()) {
            return Entity.getBody();
        } else {
            return null;
        }
    }

    @Override
    public String killMlsqlJob(String jobName, String groupId) {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (!StringUtils.isEmpty(jobName)) {
            map.add("jobName", jobName);
        } else if (!StringUtils.isEmpty(groupId)) {
            map.add("groupId", groupId);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + killJobUrl, request, String.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    @Override
    public JSONObject getEngineState() {
        ResponseEntity<JSONObject> Entity = restTemplate.getForEntity(url + engineUrl, JSONObject.class);
        if (Entity.getStatusCode().is2xxSuccessful()) {
            return Entity.getBody();
        } else {
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
            tScriptExecLog.setOperatorTime(new Date());
            tScriptExecLog.setFinishTime(new Date());
            tScriptExecLog.setJobName(jobInfo.getJobName());
            tScriptExecLogDao.insert(tScriptExecLog);

            //根据groupId再次请求获取activeJobs
            JSONArray activeJobsByGroupId = getActiveJobsByGroupId(jobInfo.getGroupId());
            JSONObject jsonObject = (JSONObject) activeJobsByGroupId.get(0);
            Integer jobsCount = jsonObject.getJSONArray("activeJobs").size();
            Integer taskCount = 0;
            Integer stageCount = 0;
            if (jobsCount > 1) {
                JSONArray activeJobs = jsonObject.getJSONArray("activeJobs");
                for (Object activeJob : activeJobs) {
                    JSONObject activeJob1 = (JSONObject) activeJob;
                    Integer numTasks = (Integer) activeJob1.get("numTasks");
                    taskCount += numTasks;

                    JSONArray jsonArray = (JSONArray) activeJob1.get("mLSQLScriptJobStage");
                    stageCount += jsonArray.size();
                }
            } else {
                JSONArray activeJobs = jsonObject.getJSONArray("activeJobs");
                JSONObject jsonObject1 = (JSONObject) activeJobs.get(0);
                taskCount = (Integer) jsonObject1.get("numTasks");
                JSONArray jsonArray = (JSONArray) jsonObject1.get("mLSQLScriptJobStage");
                stageCount = jsonArray.size();
            }


            //插入运行指标表
            TScriptExecMetricLog tScriptExecMetricLog = new TScriptExecMetricLog();
            tScriptExecMetricLog.setJobId(jobInfo.getGroupId());
            tScriptExecMetricLog.setExplainMsg(map.get("explainMsg"));
            Date date = new Date(jobInfo.getStartTime());
            tScriptExecMetricLog.setCreateTime(date);
            tScriptExecMetricLog.setSparkUiJobCnt(jobsCount);
            tScriptExecMetricLog.setSparkUiStageCnt(stageCount);
            tScriptExecMetricLog.setSparkUiTaskCnt(taskCount);
            tScriptExecMetricLogDao.insert(tScriptExecMetricLog);
        } else {
            TScriptExecLog tScriptExecLog = new TScriptExecLog();
            MlsqlJobsVO jobInfo = JSONObject.parseObject(map.get("jobInfo"), MlsqlJobsVO.class);
            tScriptExecLog.setJobId(jobInfo.getGroupId());
            tScriptExecLog.setScriptContent(jobInfo.getJobContent());
            tScriptExecLog.setExecStatus(map.get("stat"));
            tScriptExecLog.setKeyMsg(map.get("msg"));
            tScriptExecLog.setJobName(jobInfo.getJobName());
            tScriptExecLog.setJobType(jobInfo.getJobType());
            tScriptExecLogDao.insert(tScriptExecLog);


        }
    }

    public JSONArray getActiveJobsByGroupId(String groupId) {
        MlsqlExecuteSqlVO mlsqlExecuteSqlVO = new MlsqlExecuteSqlVO();
        mlsqlExecuteSqlVO.setSql("load _mlsql_.`jobs/v3/" + groupId + "` as wow;");
        return JSONObject.parseObject(executeMlsql(mlsqlExecuteSqlVO), JSONArray.class);
    }

    @Override
    public List<TreeVo<TSystemResources>> listTreeByParentId(BigDecimal resourceTypeId,BigDecimal parentId) {
        QueryWrapper<TSystemResources> queryWrapper = new QueryWrapper<TSystemResources>().select("id","parentId","resource_type_id","resource_name","owner").eq("resource_type_id", resourceTypeId).eq("parentid", parentId);
        List<TSystemResources> tSystemResources = tSystemResourcesDao.selectList(queryWrapper);
        List<TSystemResources> tSystemResources1 = new ArrayList<>();
        for (TSystemResources tSystemResource : tSystemResources) {
            List<TSystemResources> tSystemResources2 = tSystemResourcesDao.listByParentId(tSystemResource.getId());
            tSystemResources1.addAll(tSystemResources2);
        }
        TreeDataProcessor t = TreeDataProcessor.getInstance();
        //通过java进行树排序封装
        List<TreeVo<TSystemResources>> treeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tSystemResources1)) {
            treeList = t.getTreeVoList(tSystemResources1, SystemCustomIdentification.TREE_ID, SystemCustomIdentification.TREE_NAME, SystemCustomIdentification.TREE_PARENT_ID);
        }
        return treeList;
    }

    @Override
    public TSystemResources getScriptByRoute(String scriptRoute) {
        TSystemResources systemResources = tSystemResourcesDao.selectScriptByRoute(scriptRoute, ResourceTypeConstants.JBGL);
        return systemResources;
    }

    @Override
    public TSystemResources getScriptById(BigDecimal id) {
        TSystemResources systemResources = tSystemResourcesDao.selectOne(new QueryWrapper<TSystemResources>().eq("id", id));
        return systemResources;
    }

    @Override
    public Map<String, Object> getJobLogByJobName(String jobName) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (!StringUtils.isEmpty(jobName)) {
            map.add("jobName", jobName);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + getJobUrl, request, String.class);

        if (entity.getStatusCode().is2xxSuccessful()) {
            String body = entity.getBody();
            return JSONObject.parseObject(body, HashMap.class);
        } else {
            return null;
        }
    }

    @Override
    public JSONObject checkJobIsFinish(String jobName) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("jobName", jobName);
        String s = executeRequest(checkRunningJobUrl, (LinkedMultiValueMap) map);
        return JSONObject.parseObject(s, JSONObject.class);
    }

    @Override
    public JSONArray grammarCheck(String sql, String owner, String jobType, String executeMode, String jobName, Boolean sessionPerUser, Boolean skipInclude, Boolean skipGrammarValidate, String contextDefaultIncludeFetchUrl) throws Exception {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("sql", sql);
        map.add("owner", owner);
        map.add("jobType", jobType);
        map.add("executeMode", executeMode);
        map.add("jobName", jobName);
        map.add("sessionPerUser", sessionPerUser);
        map.add("skipInclude", skipInclude);
        map.add("skipGrammarValidate", skipGrammarValidate);
        map.add("context.__default__include_fetch_url__", contextDefaultIncludeFetchUrl);
        String s = executeRequest(executeUrl, map);
        return JSONObject.parseObject(s, JSONArray.class);
    }

    @Override
    public List<TreeVo<TEtFunctionInfo>> listFunctionTreeByParentId(BigDecimal resourceType) {
        TEtFunctionInfo parentTEtFunctionEntity = tEtFunctionInfoDao.selectOne(new QueryWrapper<TEtFunctionInfo>().eq("parent_id", 0).eq("etfn_type", resourceType));
        List<TEtFunctionInfo> tSystemResources1 = tEtFunctionInfoDao.listByParentId(parentTEtFunctionEntity.getId());
        TreeDataProcessor t = TreeDataProcessor.getInstance();
        //通过java进行树排序封装
        List<TreeVo<TEtFunctionInfo>> treeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tSystemResources1)) {
            treeList = t.getTreeVoList(tSystemResources1, SystemCustomIdentification.TREE_ID, "etfn_name", "parentId");
        }
        return treeList;
    }

    @Override
    public TScriptExecLog getAsyncLogResult(String jobId) {
        TScriptExecLog tScriptExecLog = tScriptExecLogDao.selectOne(new QueryWrapper<TScriptExecLog>().eq("job_d", jobId));
        return tScriptExecLog;
    }

    @Override
    public TScriptExecMetricLog getRuntimeDetails(String jobId) {
        TScriptExecMetricLog result = tScriptExecMetricLogDao.selectOne(new QueryWrapper<TScriptExecMetricLog>().eq("job_Id", jobId));
        return result;
    }

    @Override
    public Boolean deleteJobHistory(String jobId) {
       Map map =  new HashMap<String, Object>();
       map.put("job_id",jobId);
       int i = tScriptExecLogDao.deleteByMap(map);
       int i1 = tScriptExecMetricLogDao.deleteByMap(map);
       return i > 0 || i1 > 0;
    }

    @Override
    public boolean saveScript(BigDecimal parentId, BigDecimal owner,String content,String scriptName,String path) {

        TSystemResources tSystemResources1 = tSystemResourcesDao.selectScriptByRoute(path+ "/" + scriptName,ResourceTypeConstants.JBGL);
        if (tSystemResources1 == null) {
            BigDecimal maxmum = numberControlService.getMaxNum(SystemTableName.T_SYSTEM_RESOURCES);
            TSystemResources tSystemResources = new TSystemResources();
            tSystemResources.setCreateTime(new Date());
            tSystemResources.setOwner(owner);
            tSystemResources.setParentid(parentId);
            tSystemResources.setResourceName(scriptName);
            tSystemResources.setId(maxmum);
            tSystemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.JBGL));

            ScriptContentVO scriptContentVO = new ScriptContentVO();
            scriptContentVO.setSrc(path+"/"+scriptName);
            scriptContentVO.setScript(content);

            tSystemResources.setContentInfo(JSONObject.toJSONString(scriptContentVO));
            tSystemResourcesDao.insert(tSystemResources);
            return true;
        }
        return false;
    }

    /***
     * @Description: 发送请求方法
     * @Author: zrd
     * @Date: 2021/5/17 14:38
     * @param requestUrl
     * @param map
     * @return java.lang.String
     */
    public String executeRequest(String requestUrl, LinkedMultiValueMap map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + requestUrl, request, String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return null;
        }
    }

    public String getJobProcress(String requestUrl, LinkedMultiValueMap map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url + requestUrl, request, String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return null;
        }
    }


    @Override
    public String getScriptExecuteProgress(String sql, String owner, String jobType, String executeMode, String jobName, Boolean sessionPerUser) {

        JSONObject jsonObject = checkJobIsFinish(jobName);

        if (jsonObject.isEmpty()) {
            return null;
        }

        LinkedMultiValueMap<String, Object> objectObjectLinkedMultiValueMap = new LinkedMultiValueMap<>();
        objectObjectLinkedMultiValueMap.add("owner", owner);
        objectObjectLinkedMultiValueMap.add("jobType", jobType);
        objectObjectLinkedMultiValueMap.add("jobName", jobName);
        objectObjectLinkedMultiValueMap.add("sessionPerUser", sessionPerUser);
        objectObjectLinkedMultiValueMap.add("executeMode", executeMode);
        objectObjectLinkedMultiValueMap.add("sql", "load _mlsql_.`jobs/v3/" + sql + "` as wow;");
        String s = executeRequest(executeUrl, objectObjectLinkedMultiValueMap);
        return s;
    }


}

