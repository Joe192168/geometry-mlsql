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
import com.geominfo.mlsql.utils.PathFunUtil;
import com.sun.javafx.scene.shape.PathUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import scala.Int;
import sun.rmi.runtime.Log;
import sun.security.krb5.internal.PAData;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    private DsService dsService ;

    @Autowired
    private MlsqlJobService mlsqlJobService ;

    @Autowired
    private ApplyService applyService ;

    @Autowired
    private EngineService engineService ;

    @Override
    public <T> T clusterManager(LinkedMultiValueMap<String, String> paramsMap) {
        remove(paramsMap);
        ResponseEntity<String> result = null;

//        if(paramsMap.getFirst("action").equals("/backend/name/check"))
//        {
//            paramsMap.remove("teamName") ;
//        }

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();
        paramsMap.add("context.__default__include_fetch_url__", myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);

        String action = paramsMap.getFirst("action");

        switch (action) {

            case "/backend/list":
                result = clusterUrlService.backendList(paramsMap);
                break;

            case "/backend/add":
                result = clusterUrlService.backendAdd(paramsMap);
                if (result.getStatusCode().value() == GlobalConstant.TOW_HUNDRED) {
                    //同时在后台添加用户
                    String teamName = paramsMap.getFirst("teamName");
                    String backendName = paramsMap.getFirst("name");
                    backendProxyService.intsertBackendProxy(teamName, backendName);
                    logger.info("backendAdd request status  ");
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
                result = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
                break;
        }

        return (T) result;
    }


    @Override
    public <T> T runScript(LinkedMultiValueMap<String, String> paramsMap ) {

        remove(paramsMap);

        assert (paramsMap.containsKey("owner"));

        MlsqlUser user = userService.getUserByName(paramsMap.getFirst("owner"));

        String clusterUrl = CommandUtil.mlsqlClusterUrl();
        String engineUrl = CommandUtil.mlsqlEngineUrl();

        String engineName = !paramsMap.containsKey("engineName")
                || paramsMap.get("engineName").equals("undefined") ?
                getBackendName(user) : paramsMap.getFirst("engineName");

        Map<String ,Object> engineMap = new ConcurrentHashMap<>() ;
//        engineMap.put("status" ,!groupUsers.isEmpty() ? groupUsers.get(0).getStatus() : "") ;
        engineMap.put("userId" ,user.getId()) ;
        List<MlsqlEngine> engines = engineService.getAllEngine(engineMap);
        logger.info("engines = " + engines);

        List<MlsqlEngine> tmpEngienList = engines.stream().filter(me -> me.getName().equals(engineName))
                .collect(Collectors.toList()) ;

        MlsqlEngine engineConfigOpt = tmpEngienList.size() > 0 ? tmpEngienList.get(0) : null ;

        String _proxyUrl = !clusterUrl.isEmpty() ? clusterUrl : engineUrl;
        String _myUrl = CommandUtil.myUrl().isEmpty() ? "http://" + _proxyUrl : CommandUtil.myUrl();
        String _home = CommandUtil.userHome();
        int _skipAuth = !CommandUtil.enableAuthCenter() ? GlobalConstant.ONE : GlobalConstant.TOW;

        MlsqlEngine temEngine = !engineService.list().isEmpty()
                ? engineService.list().get(0) : new MlsqlEngine(0 , "" ,_proxyUrl , _home ,_myUrl ,_myUrl  ,_myUrl ,
                _skipAuth ,"{}","");
        MlsqlEngine engineConfig = engineConfigOpt != null ? engineConfigOpt : temEngine;

        String runMode;
        if (paramsMap.containsKey("runMode"))
            runMode = paramsMap.getFirst("runMode");
        else
            runMode = "mlsql";

        String sql;

        switch (runMode) {
            case "python":
                int scriptId = Integer.parseInt(paramsMap.getFirst("scriptId").toString());
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
                sql = paramsMap.getFirst("sql");
                break;
        }

        Map<String, String> tagsMap = new ConcurrentHashMap<>();
        try {
            String curTags = JSONTool.parseJson(user.getBackendTags() ,String.class) ;
            tagsMap.put(curTags, curTags);
        } catch (Exception e) {
            tagsMap.put(MlsqlUser.STATUS_NORMAL, user.getBackendTags());
        }

        String tags = sql.contains("!scheduler") ? tagsMap.get("scheduler") : tagsMap.get("normal");
        if(tags == null)
            tags = "" ;

        if (!paramsMap.containsKey("context.__default__include_fetch_url__"))
            paramsMap.add("context.__default__include_fetch_url__",
                    engineConfig.getConsoleUrl() + GlobalConstant.SCRIPT_FILE_INCLUDE);

        if (!paramsMap.containsKey("context.__default__console_url__"))
            paramsMap.add("context.__default__console_url__", engineConfig.getConsoleUrl());

        if (!paramsMap.containsKey("context.__default__fileserver_url__"))
            paramsMap.add("context.__default__fileserver_url__", engineConfig.getConsoleUrl()
                    + GlobalConstant.API_FILE_DOWNLAOD);

        if (!paramsMap.containsKey("context.__default__fileserver_upload_url__"))
            paramsMap.add("context.__default__fileserver_upload_url__",
                    engineConfig.getConsoleUrl() + GlobalConstant.API_FILE_UPLAOD);

        if (paramsMap.containsKey("skipAuth") && paramsMap.getFirst("skipAuth").equals("false")) {

            if (!paramsMap.containsKey("context.__auth_client__"))
                paramsMap.add("context.__auth_client__", GlobalConstant.STREAMING_DSL_AUTH_META_CLIENT);

            if (!paramsMap.containsKey("context.__auth_server_url__"))
                paramsMap.add("context.__auth_server_url__",  engineConfig.getConsoleUrl() + GlobalConstant.TABLE_AUTH);

        }

        if (!paramsMap.containsKey("context.__auth_secret__"))
            paramsMap.add("context.__auth_secret__", CommandUtil.auth_secret());

        if(!paramsMap.containsKey("tags"))
            paramsMap.add("tags",tags);

        if(!paramsMap.containsKey("access_token"))
            paramsMap.add("access_token" ,engineConfig.getAccessToken());


        if (!paramsMap.containsKey("defaultPathPrefix"))
            paramsMap.add("defaultPathPrefix",
                    new PathFunUtil(engineConfig.getHome()).add(user.getName()).toPath());

        if(!paramsMap.containsKey("skipAuth"))
            paramsMap.add("skipAuth" , engineConfig.getSkipAuth() == 1 ? "true" : "false");

        if(!paramsMap.containsKey("skipGrammarValidate"))
            paramsMap.add("skipGrammarValidate" ,"false");


        if(!paramsMap.containsKey("callback"))
            paramsMap.add("callback" , engineConfig.getConsoleUrl() +"/api_v1/job/callback?__auth_secret__=" +
                    CommandUtil.auth_secret());


        paramsMap.add("sql" , sql);

        paramsMap.add("owner" , user.getName());

        if(!paramsMap.containsKey("schemaInferUrl"))
            paramsMap.add("schemaInferUrl" , engineConfig.getUrl() + "/run/script");

        logger.info(cleanSql(sql));

        if(paramsMap.containsKey("__connect__")){
            String connect = dsService.getConnect(paramsMap.getFirst("__connect__") ,user) ;
            String newSql = connect + sql ;
            paramsMap.add("sql" ,newSql);
        }

        boolean isSaveQuery = paramsMap.containsKey("queryType") ?
                paramsMap.getFirst("queryType").equals("human") : true ;

        if(paramsMap.getFirst("queryType").equals("analysis_workshop_apply_action"))
            paramsMap.add("timeout" ,String.valueOf(applyTimeOut(user.getBackendTags()) * 1000));

        boolean isAsync = paramsMap.containsKey("async") ?
                paramsMap.getFirst("async").equals("true") : false ;

        long startTime = System.currentTimeMillis() ;

        ResponseEntity<String> response = clusterUrlService.synRunScript(paramsMap);

        //请求失败情况
        if(response.getStatusCode().value() == -1){
            String msg = "Request " + response.getHeaders().getLocation() +"[" +response.getBody()+"]. Please check the backend is alive." ;
            if(isSaveQuery)
                mlsqlJobService.insertJob(
                        buildJob(MlsqlJob.FAIL ,msg ,paramsMap.getFirst("jobName") ,sql ,user.getId() ));
            return (T) new ConcurrentHashMap<Integer,Object>().put(500 ,msg);

        }

        //请求成功情况
        if(paramsMap.getFirst("queryType").equals("analysis_workshop_apply_action")){
           applyService.insertApply(new MlsqlApply(
                   paramsMap.getFirst("analysis_workshop_table_name"),
                   sql ,
                   response.getStatusCodeValue(),
                   user.getId() ,
                   startTime,
                   System.currentTimeMillis(),
                   response.getBody(),
                   paramsMap.getFirst("analysis_workshop_sql")
                   ));
        }

        if(isSaveQuery)
            mlsqlJobService.insertJob(buildJob(MlsqlJob.RUNNING ,"" ,
                    paramsMap.getFirst("jobName") ,sql ,user.getId()));

        if(!isAsync && paramsMap.containsKey("jobName")){
            Map<String ,Object> map = new ConcurrentHashMap<>();
            map.put("jobName" ,paramsMap.getFirst("jobName"));
            map.put("status" ,MlsqlJob.SUCCESS);
            map.put("response" ,response.getBody());
            map.put("finishAt" ,System.currentTimeMillis());
            mlsqlJobService.updateMlsqlJobByJonName(map);
        }


        return (T) new ConcurrentHashMap<Integer ,String>().put(response.getStatusCodeValue() ,response.getBody())  ;

    }

    //private -------------------------------
    private static final String EXTRA_DEFAULT_BACKEND = "backend" ;
    private String getBackendName(MlsqlUser mlsqlUser){
        if(mlsqlUser.getBackendTags() != null && !mlsqlUser.getBackendTags().isEmpty())
        {
           Map<String ,String> map = JSONTool.parseJson(mlsqlUser.getBackendTags() ,Map.class) ;
            return map.get(EXTRA_DEFAULT_BACKEND);
        }

        else
            return null ;
    }

    private long applyTimeOut(String json)
    {
        try{
            Map<String ,String>  res =  JSONTool.parseJson(json , Map.class) ;
            return Long.valueOf(res.get("apply_timeout")) ;
        }catch (Exception e){
            return 10 ;
        }

    }

    private String cleanSql(String sql){
        try{
           List<String> resList =
                   Arrays.asList(sql.split("\n")).stream()
                           .filter(s -> !s.contains("password") ).collect(Collectors.toList());
           return pathFunUtil.mkString(resList) ;

        }catch (Exception e){
            logger.error("clean sql error " + e.getMessage());
            return sql ;
        }
    }

    private MlsqlJob buildJob(int status , String reason , String jobName , String sql , int userId ){
        return new MlsqlJob
                (0 ,jobName , sql ,status ,userId , System.currentTimeMillis(),-1L , reason ,"[]");
    }

    private LinkedMultiValueMap<String, String> remove(LinkedMultiValueMap<String, String> paramsMap) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry entry : paramsMap.entrySet()) {
            String value = paramsMap.getFirst(entry.getKey().toString());
            if (value == null)
                list.add(entry.getKey().toString());
        }

        for (String key : list) {
            paramsMap.remove(key);
        }

        return paramsMap;
    }


}



