package com.geominfo.mlsql.service.cluster.impl;



import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.utils.NetWorkProxy;
import com.geominfo.mlsql.utils.NetWorkUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.ExecutionException;

/**
 * @program: springboot_console_test
 * @description: 集群设置执行路径实现类
 * @author: BJZ
 * @create: 2020-07-14 14:29
 * @version: 1.0.0
 */
@Service
public class ClusterUrlServiceImpl implements ClusterUrlService {


    private NetWorkUtil netWorkUtil = NetWorkProxy.getNetWorkProxy() ;

    @Override
    public ResponseEntity<String> runScript(String sql) {
        return null;
    }

    @Override
    public ResponseEntity<String> synRunScript(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.RUN_SCRIPT , params) ;
    }

    @Override
    public ResponseEntity<String> aynRunScript(MultiValueMap<String, String> params) throws ExecutionException, InterruptedException {
        return netWorkUtil.aynPost(GlobalConstant.RUN_SCRIPT , params) ;
    }

    @Override
    public ResponseEntity<String> runSQL(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.RUN_SQL , params) ;
    }

    @Override
    public ResponseEntity<String> backendList(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_LIST , params) ;
    }

    @Override
    public ResponseEntity<String> backendAdd(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_ADD , params) ;
    }

    @Override
    public ResponseEntity<String> backendRemove(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_REMOVE , params) ;
    }

    @Override
    public ResponseEntity<String> backendTagsUpdate(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_TAGS_UPDATE , params) ;
    }

    @Override
    public ResponseEntity<String> backendNameCheck(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_NAME_CHECK , params) ;
    }

    @Override
    public ResponseEntity<String> backendListNames(MultiValueMap<String, String> params) {
        return netWorkUtil.synPost(GlobalConstant.BACKEND_LIST_NAMES , params) ;
    }


}