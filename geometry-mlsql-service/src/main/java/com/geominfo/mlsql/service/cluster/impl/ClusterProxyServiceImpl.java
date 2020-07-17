package com.geominfo.mlsql.service.cluster.impl;


import com.geominfo.mlsql.constants.Constants;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.ClusterProxyService;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.utils.CommandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


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

    @Override
    public <T> T clusterManager(HttpServletRequest request) {

        ResponseEntity<String> result = null;

        String myUrl = CommandUtil.myUrl().isEmpty() ? CommandUtil.mlsqlClusterUrl() : CommandUtil.myUrl();
        paramsMap.add(Constants.CONTEXT_DEFAULT_INCLUDE_URL, myUrl + Constants.SCRIPT_FILE_INCLUDE);
        paramsMap.remove(Constants.TEAM_NAME);

        String[] actions = params(Constants.ACTION, request);

        if(actions.length == Constants.ZERO )
            return (T)"参数异常";

        String action = actions[Constants.ZERO] ;

        switch (action) {
            case Constants.BACKEND_LIST:
                result = clusterUrlService.backendList(paramsMap);
                break;
            case Constants.BACKEND_ADD:
                result = clusterUrlService.backendAdd(paramsMap);
                if (result.getStatusCode().value() == Constants.TOW_HUNDRED) {
                    //同时在后台添加用户
                    logger.info("backendAdd request status  ");
                }
                break;
            case Constants.BACKEND_REMOVE:
                //同时删除数据库里面内容
                result = clusterUrlService.backendRemove(paramsMap);
                break;
            case Constants.BACKEND_TAGS_UPDATE:
                result = clusterUrlService.backendTagsUpdate(paramsMap);
                break;

            case Constants.BACKEND_NAME_CHECK:
                result = clusterUrlService.backendNameCheck(paramsMap);
                break;

            case Constants.BACKEND_LIST_NAMES:
                result = clusterUrlService.backendListNames(paramsMap);
                break;

            default:
                break;
        }

        return (T) result;
    }


}