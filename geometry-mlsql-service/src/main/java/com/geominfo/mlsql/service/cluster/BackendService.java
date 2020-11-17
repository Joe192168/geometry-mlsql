package com.geominfo.mlsql.service.cluster;

import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;

import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 集群后台配置服务
 * @author: BZJ
 * @create: 2020-07-20 15:40
 * @version: 1.0.0
 */
public interface BackendService<T, S> {

    T getBackendProxyByName(S s);

    int intsertBackendProxy(String teamName,String backendName);

    int deleteBackendProxy(MlsqlBackendProxy mlsqlBackendProxy) ;
}