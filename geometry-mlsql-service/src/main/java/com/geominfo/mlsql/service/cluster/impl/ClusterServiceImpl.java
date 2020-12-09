package com.geominfo.mlsql.service.cluster.impl;


import com.geominfo.mlsql.domain.vo.*;

import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.*;
import com.geominfo.mlsql.service.engine.EngineService;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.service.scriptfile.QuillScriptFileService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.utils.CommandUtil;
import com.geominfo.mlsql.utils.JSONTool;
import com.geominfo.mlsql.utils.ParamsUtil;
import com.geominfo.mlsql.utils.PathFunUtil;
import com.sun.javafx.scene.shape.PathUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import scala.Int;
import sun.rmi.runtime.Log;
import sun.security.krb5.internal.PAData;


import javax.smartcardio.ResponseAPDU;
import java.util.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * @program: springboot_console_test
 * @description: 集群服务类
 * @author: BJZ
 * @create: 2020-07-14 10:29
 * @version: 1.0.0
 */
@Service
public class ClusterServiceImpl extends BaseServiceImpl implements ClusterService {
    Logger logger = LoggerFactory.getLogger(ClusterServiceImpl.class);

    @Autowired
    private ClusterUrlService clusterUrlService;

    @Autowired
    private BackendService backendProxyService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuillScriptFileService quillScriptFileService;

    @Autowired
    private PathFunUtil pathFunUtil;

    @Autowired
    private DsService dsService;

    @Autowired
    private MlsqlJobService mlsqlJobService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private EngineService engineService;


    @Override
    public <T> T clusterManager(Map<String, Object> qparamsMap) {
        ResponseEntity<String> result = null;

//        if(paramsMap.get("action").equals("/backend/name/check"))
//        {
//            paramsMap.remove("teamName") ;
//        }

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();
        qparamsMap.put("context.__default__include_fetch_url__", myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);

        String action = qparamsMap.get("action").toString();

        LinkedMultiValueMap<String, String> paramsMap = transformation(qparamsMap);

        switch (action) {

            case "/backend/list":
                result = clusterUrlService.backendList(paramsMap);
                break;

            case "/backend/put":
                result = clusterUrlService.backendAdd(paramsMap);
                if (result.getStatusCode().value() == GlobalConstant.TOW_HUNDRED) {
                    //同时在后台添加用户
                    String teamName = paramsMap.getFirst("teamName");
                    String backendName = paramsMap.getFirst("name");
                    backendProxyService.intsertBackendProxy(teamName, backendName);
                    logger.info("backendput request status  ");
                }
                break;

            case "/backend/remove":
                //同时删除数据库里面内容
                String name = paramsMap.getFirst("name");
                backendProxyService.deleteBackendProxy((MlsqlBackendProxy)
                        backendProxyService.getBackendProxyByName(name));
                result = clusterUrlService.backendRemove(paramsMap);
                break;

            case "/backend/tags/update":
                result = clusterUrlService.backendTagsUpdate(paramsMap);
                break;

            case "/backend/name/check":
                result = clusterUrlService.backendNameCheck(paramsMap);
                break;

            case "/backend/list/names":
                result = clusterUrlService.backendListNames(paramsMap);
                break;

            default:
                logger.info("没有执行的东西!");
                result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
                break;
        }

        Map<Integer ,Object> resData = new ConcurrentHashMap<>();
        resData.put(result.getStatusCode().value() ,result.getBody()) ;
         return (T) resData;
    }


    @Override
    public <T> T runScript(Map<String, Object> paramsMap) throws ExecutionException, InterruptedException {

        assert (paramsMap.containsKey("owner") && paramsMap.containsKey("sql"));

        if(!paramsMap.containsKey("jobName"))
            paramsMap.put("jobName" ,UUID.randomUUID().toString()) ;

        if (!paramsMap.containsKey("sessionPerUser"))
            paramsMap.put("sessionPerUser", "false");

        if (!paramsMap.containsKey("async"))
            paramsMap.put("async", "true");

        if (!paramsMap.containsKey("show_stack"))
            paramsMap.put("show_stack", "true");

        if (!paramsMap.containsKey("timeout"))
            paramsMap.put("timeout", "-1");

        MlsqlUser user = userService.getUserByName(paramsMap.get("owner").toString());

        String clusterUrl = CommandUtil.mlsqlClusterUrl();
        String engineUrl = CommandUtil.mlsqlEngineUrl();

        String engineName = !paramsMap.containsKey("engineName")
                || paramsMap.get("engineName").equals("undefined") ?
                getBackendName(user) : paramsMap.get("engineName").toString();

        if (!paramsMap.containsKey("engineName"))
            paramsMap.put("engineName", engineName);

        Map<String, Object> engineMap = new ConcurrentHashMap<>();
        engineMap.put("userId", user.getId());
//        List<MlsqlEngine> engines = engineService.getAllEngine(engineMap);
        List<MlsqlEngine> engines = engineService.list();


        List<MlsqlEngine> tmpEngienList = engines.stream().filter(me -> me.getName().equals(engineName))
                .collect(Collectors.toList());

        MlsqlEngine engineConfigOpt = tmpEngienList.size() > 0 ? tmpEngienList.get(0) : null;

        String _proxyUrl = !clusterUrl.isEmpty() ? clusterUrl : engineUrl;
        String _myUrl = CommandUtil.myUrl().isEmpty() ?  _proxyUrl : CommandUtil.myUrl();
        String _home = CommandUtil.userHome();

        int _skipAuth = !CommandUtil.enableAuthCenter() ? GlobalConstant.ONE : GlobalConstant.TOW;

        MlsqlEngine temEngine = !engineService.list().isEmpty()
                ? engineService.list().get(0) : new MlsqlEngine(0, "", _proxyUrl, _home, _myUrl, _myUrl, _myUrl,
                _skipAuth, "{}", "");
        MlsqlEngine engineConfig = engineConfigOpt != null ? engineConfigOpt : temEngine;

        String runMode;
        if (paramsMap.containsKey("runMode"))
            runMode = paramsMap.get("runMode").toString();
        else
            runMode = "mlsql";

        String sql;

        switch (runMode) {
            case "python":
                int scriptId = Integer.parseInt(paramsMap.get("scriptId").toString());
                String projectName = quillScriptFileService.findProjectNameFileIn(scriptId);

                List<FullPathAndScriptFile> buffer = quillScriptFileService.findProjectFiles(
                        user.getName(), projectName);

                FullPathAndScriptFile currentScriptFile =
                        buffer.stream().filter(sf -> sf.getScriptFile().getId() == scriptId).collect(Collectors.toList())
                                .get(0);

                boolean isInPackage = quillScriptFileService.isInPackage(currentScriptFile, buffer);

                if (isInPackage)
                    sql = JSONTool.toJsonStr(buffer);
                else
                    sql = JSONTool.toJsonStr(new ArrayList<>().add(currentScriptFile));
                break;

            default:
                sql = paramsMap.get("sql").toString();
                break;
        }

        Map<String, String> tagsMap = new ConcurrentHashMap<>();
        try {
            String curTags = JSONTool.parseJson(user.getBackendTags(), String.class);
            tagsMap.put(curTags, curTags);
        } catch (Exception e) {
            tagsMap.put(MlsqlUser.STATUS_NORMAL, user.getBackendTags());
        }

        String tags = sql.contains("!scheduler") ? tagsMap.get("scheduler") : tagsMap.get("normal");
        if (tags == null)
            tags = "";

        if (!paramsMap.containsKey("context.__default__include_fetch_url__"))
            paramsMap.put("context.__default__include_fetch_url__",
                    engineConfig.getConsoleUrl() + GlobalConstant.SCRIPT_FILE_INCLUDE);

        if (!paramsMap.containsKey("context.__default__console_url__"))
            paramsMap.put("context.__default__console_url__", engineConfig.getConsoleUrl());

        if (!paramsMap.containsKey("context.__default__fileserver_url__"))
            paramsMap.put("context.__default__fileserver_url__", engineConfig.getConsoleUrl()
                    + GlobalConstant.API_FILE_DOWNLAOD);

        if (!paramsMap.containsKey("context.__default__fileserver_upload_url__"))
            paramsMap.put("context.__default__fileserver_upload_url__",
                    engineConfig.getConsoleUrl() + GlobalConstant.API_FILE_UPLAOD);

//        if (paramsMap.containsKey("skipAuth") && paramsMap.get("skipAuth").equals("false")) {

        if (!paramsMap.containsKey("context.__auth_client__"))
            paramsMap.put("context.__auth_client__", GlobalConstant.STREAMING_DSL_AUTH_META_CLIENT);

        if (!paramsMap.containsKey("context.__auth_server_url__"))
            paramsMap.put("context.__auth_server_url__", engineConfig.getConsoleUrl() + GlobalConstant.TABLE_AUTH);

//        }

        if (!paramsMap.containsKey("context.__auth_secret__"))
            paramsMap.put("context.__auth_secret__",
                    CommandUtil.auth_secret() == null || CommandUtil.auth_secret().equals("")
                            ? UUID.randomUUID().toString() : CommandUtil.auth_secret());

        if (!paramsMap.containsKey("tags"))
            paramsMap.put("tags", tags);

        if (!paramsMap.containsKey("access_token"))
            paramsMap.put("access_token", engineConfig.getAccessToken());


        if (!paramsMap.containsKey("defaultPathPrefix"))
            paramsMap.put("defaultPathPrefix",
                    new PathFunUtil(engineConfig.getHome()).add(user.getName()).toPath());


        if (!paramsMap.containsKey("skipAuth"))
            paramsMap.put("skipAuth", engineConfig.getSkipAuth() == 1 ? "true" : "false");

        if (!paramsMap.containsKey("skipGrammarValidate"))
            paramsMap.put("skipGrammarValidate", "false");



        if (paramsMap.get("async").toString().equals("true"))
        {
            if (!paramsMap.containsKey("callback"))
                paramsMap.put("callback", engineConfig.getConsoleUrl() + "/api_v1/job/callback?__auth_secret__=" +
                        paramsMap.get("context.__auth_secret__"));
        }
        else
        {
            if(paramsMap.containsKey("callback"))
                paramsMap.remove("callback") ;
        }

        paramsMap.put("sql", sql);

        paramsMap.put("owner", user.getName());

        if (!paramsMap.containsKey("schemaInferUrl"))
            paramsMap.put("schemaInferUrl", engineConfig.getUrl() + "/run/script");

        logger.info(cleanSql(sql));

        if (paramsMap.containsKey("__connect__")) {
            String connect = dsService.getConnect(paramsMap.get("__connect__").toString(), user);
            String newSql = connect + sql;
            paramsMap.put("sql", newSql);
        }

        boolean isSaveQuery = paramsMap.containsKey("queryType") ?
                paramsMap.get("queryType").equals("human") : true;

        boolean queryType = paramsMap.containsKey("queryType") ?
                paramsMap.get("queryType").equals("analysis_workshop_apply_action") : false;

        if (queryType)
            paramsMap.put("timeout", String.valueOf(applyTimeOut(user.getBackendTags()) * 1000));

        boolean isAsync = paramsMap.containsKey("async") ?
                paramsMap.get("async").equals("true") : false;

        long startTime = System.currentTimeMillis();

        LinkedMultiValueMap<String, String> runParamsMap = transformation(paramsMap);

//        System.out.println("----------------打印请求前的参数信息------------------------------");
//        for (Map.Entry entry : runParamsMap.entrySet())
//            System.out.println("key=" + entry.getKey() + "\n" + "value=" + entry.getValue());

        ResponseEntity<String> response = clusterUrlService.synRunScript(runParamsMap);

        System.out.println("同步执行返回数据 = " + response.getBody());

        //请求失败情况
        if (response.getStatusCode().value() == -1) {
            String msg = "Request " + response.getHeaders().getLocation() + "[" + response.getBody() + "]. Please check the backend is alive.";
            if (isSaveQuery)
                mlsqlJobService.insertJob(
                        buildJob(MlsqlJob.FAIL, msg, runParamsMap.getFirst("jobName"), sql, user.getId()));

            Map<Integer ,Object> errorData = new ConcurrentHashMap<>();
            errorData.put(500, msg);
            return (T)errorData ;

        }

        //请求成功情况
        if (queryType) {
            applyService.insertApply(new MlsqlApply(
                    runParamsMap.getFirst("analysis_workshop_table_name"),
                    sql,
                    response.getStatusCodeValue(),
                    user.getId(),
                    startTime,
                    System.currentTimeMillis(),
                    response.getBody(),
                    runParamsMap.getFirst("analysis_workshop_sql")
            ));
        }

        if (isSaveQuery)
            mlsqlJobService.insertJob(buildJob(MlsqlJob.RUNNING, "",
                    runParamsMap.getFirst("jobName"), sql, user.getId()));

        if (!isAsync && paramsMap.containsKey("jobName")) {
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("jobName", paramsMap.get("jobName"));
            map.put("status", MlsqlJob.SUCCESS);
            map.put("response", response.getBody());
            map.put("finishAt", System.currentTimeMillis());
            mlsqlJobService.updateMlsqlJobByJonName(map);
        }

        Map<Integer ,Object> errorData = new ConcurrentHashMap<>();
        errorData.put(200, response.getBody()) ;
        return (T)errorData ;

    }

    //private -------------------------------
    private static final String EXTRA_DEFAULT_BACKEND = "backend";

    private String getBackendName(MlsqlUser mlsqlUser) {
        if (mlsqlUser.getBackendTags() != null && !mlsqlUser.getBackendTags().isEmpty()) {
            Map<String, String> map = JSONTool.parseJson(mlsqlUser.getBackendTags(), Map.class);
            return map.get(EXTRA_DEFAULT_BACKEND);
        } else
            return "";
    }

    private long applyTimeOut(String json) {
        try {
            Map<String, String> res = JSONTool.parseJson(json, Map.class);
            return Long.valueOf(res.get("apply_timeout"));
        } catch (Exception e) {
            return 10;
        }

    }

    private String cleanSql(String sql) {
        try {
            List<String> resList =
                    Arrays.asList(sql.split("\n")).stream()
                            .filter(s -> !s.contains("password")).collect(Collectors.toList());
            return pathFunUtil.mkString(resList);

        } catch (Exception e) {
            logger.error("clean sql error " + e.getMessage());
            return sql;
        }
    }

    private MlsqlJob buildJob(int status, String reason, String jobName, String sql, int userId) {
        return new MlsqlJob
                (0, jobName, sql, status, userId, System.currentTimeMillis(), -1L, reason, "[]");
    }

    private LinkedMultiValueMap<String, String> transformation(Map<String, Object> paramsMap) {
        LinkedMultiValueMap<String, String> curMap = new LinkedMultiValueMap<>();
        for (Map.Entry entry : paramsMap.entrySet()) {
            if (entry.getValue() != null)
                curMap.add(entry.getKey().toString(), entry.getValue().toString());
        }
        return curMap;
    }


}



