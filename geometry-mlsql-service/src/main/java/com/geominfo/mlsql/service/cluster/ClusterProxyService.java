package com.geominfo.mlsql.service.cluster;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: springboot_console_test
 * @description: 集群后台配置接口
 * @author: BJZ
 * @create: 2020-07-14 10:26
 * @version: 1.0.0
 */
@Service
public interface ClusterProxyService {

    <T> T clusterManager(HttpServletRequest request) ;
}