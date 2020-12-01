package com.geominfo.mlsql.service.cluster;

import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;
import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.domain.vo.ScriptUserRw;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 集群后台配置服务
 * @author: BZJ
 * @create: 2020-07-20 15:40
 * @version: 1.0.0
 */
@Service
public interface BackendService<T, S> {

    T getBackendProxyByName(S s);

    int intsertBackendProxy(String teamName,String backendName);

    int deleteBackendProxy(MlsqlBackendProxy mlsqlBackendProxy) ;

    //2.0新增接口，完成QuillScriptFileService 类接口功能
    List<MlsqlScriptFile> findProjectFiles(Integer userId) ;
}