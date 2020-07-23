package com.geominfo.mlsql.shiro.provider.impl;

import com.geominfo.mlsql.domain.rule.RolePermRule;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.shiro.provider.ShiroFilterRulesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: ShiroFilter角色实现
 * @author: 肖乔辉
 * @create: 2018-11-02 09:53
 * @version: 3.0.0
 */
@Service("ShiroFilterRulesProvider")
public class ShiroFilterRulesProviderImpl implements ShiroFilterRulesProvider {



    @Autowired
    private TeamRoleService teamRoleService;

    @Override
    public List<RolePermRule> loadRolePermRules() {
        List<RolePermRule> rolePermRules = new ArrayList<>();
        RolePermRule rolePermRule = new RolePermRule();
        rolePermRule.setUrl("/**");
        rolePermRule.setNeedRoles(teamRoleService.getRolesAll());
        rolePermRules.add(rolePermRule);
        return rolePermRules;
    }

}
