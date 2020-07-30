package com.geominfo.mlsql.service.cluster.impl;

import com.geominfo.mlsql.domain.vo.MlsqlBackendProxy;
import com.geominfo.mlsql.domain.vo.MlsqlGroup;
import com.geominfo.mlsql.mapper.BackendProxyMapper;
import com.geominfo.mlsql.service.base.BaseServiceImpl;
import com.geominfo.mlsql.service.cluster.BackendProxyService;
import com.geominfo.mlsql.service.user.TeamRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @program: geometry-mlsql
 * @description: backendproxy实现类
 * @author: bjz
 * @create: 2020-07-20 15:46
 * @version: 1.0.0
 */
@Service
public class BackendProxyServiceImpl extends BaseServiceImpl implements BackendProxyService {

    @Autowired
    private BackendProxyMapper backendProxyMapper;

    @Autowired
    private TeamRoleService teamRoleService ;

    @Override
    public Object getBackendProxyByName(Object s) {
        return backendProxyMapper.getBackendProxyByName((String)s);
    }


    @Override
    public int intsertBackendProxy(String backendName) {
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(backendName) ;
        MlsqlBackendProxy mlsqlBackendProxy = new MlsqlBackendProxy();
        mlsqlBackendProxy.setGroupId(mlsqlGroup.getId());
        mlsqlBackendProxy.setBackendName(backendName );
        return backendProxyMapper.intsertBackendProxy(mlsqlBackendProxy);
    }

    @Override
    public int deleteBackendProxy(MlsqlBackendProxy mlsqlBackendProxy) {
        return backendProxyMapper.deleteBackendProxy(mlsqlBackendProxy);
    }


}