package com.geominfo.mlsql.service.cluster.impl;


import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;

import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.BackendService;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.utils.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;


import java.util.ArrayList;

import java.util.Map;


/**
 * @program: springboot_console_test
 * @description: 集群设置服务类
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
    public <T> T runScript(LinkedMultiValueMap<String, String> paramsMap) {

        remove(paramsMap);

        String myUrl;
        if (CommandUtil.myUrl().isEmpty()) {
            myUrl = GlobalConstant.HTTP_HEAD_TOW + GlobalConstant.MLSQL_CLUSTER_DEFAULT_URL;
        } else {
            myUrl = CommandUtil.myUrl();
        }

        if (!paramsMap.containsKey("context.__default__include_fetch_url__"))
            paramsMap.add("context.__default__include_fetch_url__",
                    myUrl + GlobalConstant.SCRIPT_FILE_INCLUDE);

        if (!paramsMap.containsKey("context.__default__console_url__"))
            paramsMap.add("context.__default__console_url__", myUrl);

        if (!paramsMap.containsKey("context.__default__fileserver_url__"))
            paramsMap.add("context.__default__fileserver_url__", myUrl + GlobalConstant.API_FILE_DOWNLAOD);

        if (!paramsMap.containsKey("context.__default__fileserver_upload_url__"))
            paramsMap.add("context.__default__fileserver_upload_url__",
                    myUrl + GlobalConstant.API_FILE_UPLAOD);

        if (paramsMap.containsKey("skipAuth") && paramsMap.getFirst("skipAuth").equals("false")) {

            if (!paramsMap.containsKey("context.__auth_client__"))
                paramsMap.add("context.__auth_client__", GlobalConstant.STREAMING_DSL_AUTH_META_CLIENT);

            if (!paramsMap.containsKey("context.__auth_server_url__"))
                paramsMap.add("context.__auth_server_url__", myUrl + GlobalConstant.TABLE_AUTH);

        }

        if (!paramsMap.containsKey("context.__auth_secret__"))
            paramsMap.add("context.__auth_secret__", CommandUtil.auth_secret());

        if (!paramsMap.containsKey("defaultPathPrefix"))
            paramsMap.add("defaultPathPrefix", CommandUtil.userHome() +
                    GlobalConstant.HTTP_SEPARATED + paramsMap.getFirst("owner"));


        return (T) clusterUrlService.synRunScript(paramsMap);

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