package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.domain.dto.RolePermRule;
import com.geominfo.mlsql.services.AuthApiService;
import com.geominfo.mlsql.services.dao.IRoleDao;
import com.geominfo.mlsql.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("IRoleDaoImpl")
public class RoleDaoImpl implements IRoleDao {

    @Autowired
    private AuthApiService authApiService;

    @Override
    public List<RolePermRule> queryRolePermRule(Integer systemId) {
        return FeignUtils.parseArray(authApiService.getSystemRoles(systemId),RolePermRule.class);
    }

    @Override
    public String getUserRole(String loginName) {
        return FeignUtils.parseString(authApiService.getUserRole(loginName));
    }
}
