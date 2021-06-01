package com.geominfo.mlsql.domain.vo;

import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.utils.TreeVo;

import java.util.List;

/**
 * @program: geometry-bi
 * @description:人员权限信息
 * @author: LF
 * @create: 2021/5/31 10:45
 * @version: 1.0.0
 */
public class UserPermissionInfosVo {
    /**
     * 权限树
     */
    private List<TreeVo<SystemResourceVo>> permissionTrees ;

    /**
     * 人员基础信息
     */
    private User user ;

    /**
     *jwt
     */
    private String jwt;


    public List<TreeVo<SystemResourceVo>> getPermissionTrees() {
        return permissionTrees;
    }

    public void setPermissionTrees(List<TreeVo<SystemResourceVo>> permissionTrees) {
        this.permissionTrees = permissionTrees;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "UserPermissionInfosVo{" +
                "permissionTrees=" + permissionTrees +
                ", user=" + user +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
