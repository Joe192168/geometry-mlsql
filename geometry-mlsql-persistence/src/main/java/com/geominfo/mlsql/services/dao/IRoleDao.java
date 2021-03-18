package com.geominfo.mlsql.services.dao;

import com.geominfo.mlsql.domain.dto.RolePermRule;
import java.util.List;

public interface IRoleDao {

    List<RolePermRule> queryRolePermRule(Integer systemId);

    String getUserRole(String loginName);

}
