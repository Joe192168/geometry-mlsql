package com.geominfo.mlsql.services.impl;


import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.services.MlsqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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
}
