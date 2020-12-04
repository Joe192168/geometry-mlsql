package com.geominfo.mlsql.service.cluster.impl;



import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.cluster.ClusterUrlService;
import com.geominfo.mlsql.service.proxy.ProxyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

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

    @Autowired
    private ProxyService netWorkUtil ;

    private static final String BASE_URL ="http://192.168.20.209:9003" ;


    @Override
    public ResponseEntity<String> runScript(String sql) {
        return null;
    }

    @Override
    public ResponseEntity<String> synRunScript(LinkedMultiValueMap<String, String> params) throws ExecutionException, InterruptedException {
        return cPost(GlobalConstant.RUN_SCRIPT , params) ;
    }

    @Override
    public ResponseEntity<String> aynRunScript(LinkedMultiValueMap<String, String> params) throws ExecutionException, InterruptedException {
        return cPost(GlobalConstant.RUN_SCRIPT , params) ;
    }

    @Override
    public ResponseEntity<String> runSQL(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.RUN_SQL , params) ;
    }

    @Override
    public ResponseEntity<String> backendList(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_LIST , params) ;
    }

    @Override
    public ResponseEntity<String> backendAdd(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_ADD , params) ;
    }

    @Override
    public ResponseEntity<String> backendRemove(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_REMOVE , params) ;
    }

    @Override
    public ResponseEntity<String> backendTagsUpdate(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_TAGS_UPDATE , params) ;
    }

    @Override
    public ResponseEntity<String> backendNameCheck(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_NAME_CHECK , params) ;
    }

    @Override
    public ResponseEntity<String> backendListNames(LinkedMultiValueMap<String, String> params) {
        return cPost(GlobalConstant.BACKEND_LIST_NAMES , params) ;
    }

    
    private ResponseEntity<String> cPost(String url,LinkedMultiValueMap<String, String> params){
       return  netWorkUtil.postForEntity(BASE_URL+url ,params ,String.class) ;
    }


}