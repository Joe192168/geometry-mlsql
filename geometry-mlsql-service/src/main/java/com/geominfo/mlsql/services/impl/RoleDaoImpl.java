package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.domain.dto.RolePermRule;
import com.geominfo.mlsql.services.feign.FeignRoleService;
import com.geominfo.mlsql.services.dao.IRoleDao;
import com.geominfo.mlsql.utils.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("IRoleDaoImpl")
public class RoleDaoImpl implements IRoleDao {

    @Autowired
    private FeignRoleService feignRoleSystemService;

    @Override
    public List<RolePermRule> queryRolePermRule(Integer systemId) {
        return FeignUtils.parseArray(feignRoleSystemService.getSystemRoles(systemId),RolePermRule.class);
    }

    @Override
    public String getUserRole(String loginName) {
        return FeignUtils.parseString(feignRoleSystemService.getUserRole(loginName));
    }
}
