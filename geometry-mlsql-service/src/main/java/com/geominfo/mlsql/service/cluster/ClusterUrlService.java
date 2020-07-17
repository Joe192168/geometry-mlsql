package com.geominfo.mlsql.service.cluster;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;


/**
 * @program: springboot_console_test
 * @description: 集群后台配置执行地址接口
 * @author: BJZ
 * @create: 2020-07-14 11:28
 * @version: 1.0.0
 */
@Service
public interface ClusterUrlService {

    ResponseEntity<String> runScript( String sql) ;
    ResponseEntity<String> synRunScript(MultiValueMap<String, String> params) ;
    ResponseEntity<String> aynRunScript(MultiValueMap<String, String> params) ;
    ResponseEntity<String> runSQL(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendList(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendAdd(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendRemove(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendTagsUpdate(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendNameCheck(MultiValueMap<String, String> params) ;
    ResponseEntity<String> backendListNames(MultiValueMap<String, String> params) ;

}