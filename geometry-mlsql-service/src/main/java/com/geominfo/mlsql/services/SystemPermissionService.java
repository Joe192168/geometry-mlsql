package com.geominfo.mlsql.services;

import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.domain.vo.UserPermissionInfosVo;
import com.geominfo.mlsql.utils.TreeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/25 17:38
 * @version: 1.0.0
 */
public interface SystemPermissionService {

    /**
     * @description: 查询系统权限信息
     * @author: LF
     * @date: 2021/6/1
     * @param token, jwtUser
     * @return com.geominfo.mlsql.domain.vo.UserPermissionInfosVo
     */
    UserPermissionInfosVo getUserPermissionInfos(String token,JwtUser jwtUser);

    /***
     * @description: 管理人员会话信息
     * @author: LF
     * @date: 2021/5/28
     * @param user, jwt, token_ken, request
     * @return void
     */
    void  userSession(User user, String jwt, String token_ken, HttpServletRequest request);
}
