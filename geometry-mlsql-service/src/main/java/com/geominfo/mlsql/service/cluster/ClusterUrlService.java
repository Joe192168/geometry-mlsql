package com.geominfo.mlsql.service.cluster;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;


import java.util.concurrent.ExecutionException;


/**
 * @program: springboot_console_test
 * @description: 集群后台配置执行地址接口
 * @author: BJZ
 * @create: 2020-07-14 11:28
 * @version: 1.0.0
 */
@Service
public interface ClusterUrlService {

    ResponseEntity<String> runScript(String sql) ;
    ResponseEntity<String> synRunScript(LinkedMultiValueMap<String, String> params) throws ExecutionException, InterruptedException;
    ResponseEntity<String> aynRunScript(LinkedMultiValueMap<String, String> params) throws ExecutionException, InterruptedException;
    ResponseEntity<String> runSQL(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendList(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendAdd(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendRemove(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendTagsUpdate(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendNameCheck(LinkedMultiValueMap<String, String> params) ;
    ResponseEntity<String> backendListNames(LinkedMultiValueMap<String, String> params) ;

}