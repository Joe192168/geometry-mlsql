package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.ScriptUserRw;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: BJZ
 * @create: 2020-07-20 14:53
 * @version: 1.0.0
 */
@Mapper
@Component
public interface BackendProxyMapper {

    List<MlsqlBackendProxy> getBackendProxyByName(String backendName);
    int intsertBackendProxy(MlsqlBackendProxy mlsqlBackendProxy) ;
    int deleteBackendProxy(MlsqlBackendProxy mlsqlBackendProxy) ;

    List<MlsqlScriptFile> findProjectFiles(Integer userId) ;
}