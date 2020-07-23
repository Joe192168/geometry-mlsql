package com.geominfo.mlsql.service.cluster.impl;



import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;

import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.BackendProxyService;
import com.geominfo.mlsql.service.cluster.ClusterProxyService;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.utils.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.UUID;


/**
 * @program: springboot_console_test
 * @description: 集群设置服务类
 * @author: BJZ
 * @create: 2020-07-14 10:29
 * @version: 1.0.0
 */
@Service
public class ClusterProxyServiceImpl extends BaseServiceImpl implements ClusterProxyService {
    Logger logger = LoggerFactory.getLogger(ClusterProxyServiceImpl.class);


    @Autowired
    private ClusterUrlService clusterUrlService;

    @Autowired
    private BackendProxyService backendProxyService;

    @Override
    public <T> T clusterManager(Object o) {

        ResponseEntity<String> result = null;

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();
        paramsMap.add(GlobalConstant.CONTEXT_DEFAULT_INCLUDE_URL, myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);
        paramsMap.remove(GlobalConstant.TEAM_NAME);

//        String[] actions = params(GlobalConstant.ACTION, request);
//
//        if(actions.length == GlobalConstant.ZERO )
//            return (T)"参数异常";
//
//        String action = actions[GlobalConstant.ZERO] ;

        MultiValueMap<String, String> params = (MultiValueMap<String, String>) o;
        String action = params.getFirst(GlobalConstant.ACTION);
        String teamName = params.getFirst("teamName");
        String name = params.getFirst("name");


        switch (action) {
            case GlobalConstant.BACKEND_LIST:
                result = clusterUrlService.backendList(paramsMap);
                break;
            case GlobalConstant.BACKEND_ADD:
                result = clusterUrlService.backendAdd(paramsMap);
                if (result.getStatusCode().value() == GlobalConstant.TOW_HUNDRED) {
                    //同时在后台添加用户
                    backendProxyService.intsertBackendProxy(teamName, name);
                    logger.info("backendAdd request status  ");
                }
                break;
            case GlobalConstant.BACKEND_REMOVE:
                //同时删除数据库里面内容
                backendProxyService.deleteBackendProxy((MlsqlBackendProxy) backendProxyService.getBackendProxyByName(name));

                result = clusterUrlService.backendRemove(paramsMap);
                break;
            case GlobalConstant.BACKEND_TAGS_UPDATE:
                result = clusterUrlService.backendTagsUpdate(paramsMap);
                break;

            case GlobalConstant.BACKEND_NAME_CHECK:
                result = clusterUrlService.backendNameCheck(paramsMap);
                break;

            case GlobalConstant.BACKEND_LIST_NAMES:
                result = clusterUrlService.backendListNames(paramsMap);
                break;

            default:
                break;
        }

        return (T) result;
    }


    @Override
    public <T> T runScript(Object o) {

        paramsMap = (MultiValueMap<String, String>) o; //这里后期需要优化，直接获取参数

        //这些参数，应该封装在底层基础类里。这里暂时写死
        paramsMap.add("jobName", UUID.randomUUID().toString());
        paramsMap.add("skipAuth", GlobalConstant.FALSE);
        paramsMap.add("background", GlobalConstant.FALSE);
        paramsMap.add("tags", "{\"normal\":\"b4833b75-9b0a-487d-bf59-f38e4c151479_admin\"}");
        paramsMap.add("sessionPerUser", "true");
        paramsMap.add("show_stack", "true");
        paramsMap.add("owner", "banjianzu@gmail.com");
        paramsMap.add("timeout", "-1");


        String myUrl;
        if (CommandUtil.myUrl().isEmpty()) {
            myUrl = GlobalConstant.HTTP_HEAD_TOW + GlobalConstant.MLSQL_CLUSTER_DEFAULT_URL;
        } else {
            myUrl = CommandUtil.myUrl();
        }

        String sql = paramsMap.getFirst(GlobalConstant.SQL);

        HashMap<String, String> tagsMap = new HashMap<>();
        //这里暂时写死，到时候，，直接从用户模块直接动态获取
        tagsMap.put("normal", "b4833b75-9b0a-487d-bf59-f38e4c151479_admin");

        String tags;
        if (sql.contains(GlobalConstant.SCHEDULER)) {
            tags = tagsMap.get(GlobalConstant.SCHEDULER_TAG_TYPE);
        } else {
            tags = tagsMap.get(GlobalConstant.NORMAL_TAG_TYPE);

        }

        paramsMap.add(GlobalConstant.CONTEXT_DEFAULT_INCLUDE_URL, myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);
        paramsMap.add(GlobalConstant.DEFAULT_CONSOLE_URL, myUrl);
        paramsMap.add(GlobalConstant.CONTEXT_DEFAULT_FILESERVER_URL, myUrl + GlobalConstant.API_FILE_DOWNLAOD);
        paramsMap.add(GlobalConstant.CONTEXT_DEFAULT_FILESERVER_UPLOAD_URL, myUrl + GlobalConstant.API_FILE_UPLAOD);
        paramsMap.add(GlobalConstant.CONTEXT_AUTH_CLIENT, GlobalConstant.STREAMING_DSL_AUTH_META_CLIENT);
        paramsMap.add(GlobalConstant.CONTEXT_AUTH_SERVER_URL, myUrl + GlobalConstant.TABLE_AUTH);
        paramsMap.add(GlobalConstant.CONTEXT_AUTH_SECRET, CommandUtil.auth_secret());
        paramsMap.add(GlobalConstant.TAGS, tags);
        //这里userName 暂时写死，到时候，动态获取
        paramsMap.add(GlobalConstant.DEFAULTPATHPREFIX, CommandUtil.userHome() + GlobalConstant.HTTP_SEPARATED + "userName");
        paramsMap.add(GlobalConstant.SKIPAUTH, String.valueOf(!CommandUtil.enableAuthCenter()));
        paramsMap.add(GlobalConstant.SKIPGRAMMARVALIDATE, GlobalConstant.FALSE);

        return (T) clusterUrlService.synRunScript(paramsMap);

    }


}