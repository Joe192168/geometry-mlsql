package com.geominfo.mlsql.services;

import com.geominfo.mlsql.commons.AuthApiUrl;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.vo.QueryUserVo;
import com.geominfo.mlsql.services.impl.AuthQueryApiServiceFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/5/20 16:36
 * @version: 1.0.0
 */
@FeignClient(name="authQueryService",url = "${auth.server-url}",fallbackFactory = AuthQueryApiServiceFactory.class)
public interface AuthQueryApiService {
    /**
     * 获取系统的权限池
     * @param systemId
     */
    @GetMapping(value = AuthApiUrl.ROLE_PERM_RULE)
    Message getSystemRoles(@PathVariable(value = "systemId") Integer systemId);

    /***
     * @description: 根据登录名查询用户的角色
     */
    @GetMapping(value = AuthApiUrl.GET_USER_ROLE)
    Message getUserRole(@PathVariable(value = "loginName") String loginName);

    /***
     * @description: 根据登录名查询用户信息
     * @author: LF
     * @date: 2021/05/20
     * @param loginName
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.GET_USER_LN)
    Message getUserByLoginName(@PathVariable(value = "loginName") String loginName);

    /***
     * @description: 根据用户的id查询用户信息
     * @author: LF
     * @date: 2021/05/20
     * @param userId
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.USER_ID)
    Message getUserById(@PathVariable(value = "userId") BigDecimal userId);

    /***
     * @description: 根据用户的id查询用户信息
     * @author: LF
     * @date: 2021/05/20
     * @param resourceId
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.USER_RID)
    Message getUserByResourceId(@PathVariable(value = "resourceId") BigDecimal resourceId);

    /***
     * @description: 查询用户的所有权限信息
     * @author: LF
     * @date: 2021/05/21
     * @param userId, appId
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.GET_USER_PERMISSIONS)
    Message getUserPermissions(@PathVariable(value = "userId") BigDecimal userId, @PathVariable(value = "appId") BigDecimal appId);

    /***
     * @description: 查询用户的所有权限的id
     * @author: LF
     * @date: 2021/05/21
     * @param loginName, appId
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.PERMISSION_IDS)
    Message getPermissionIdsByLoginNameAndAppId(@PathVariable(value = "loginName") String loginName, @PathVariable(value = "appId") BigDecimal appId);

    /***
     * @description: 查询人员信息列表
     * @author: LF
     * @date: 2021/05/21
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.USER_LIST)
    Message getUsers();

    /***
     * @description: 查询人员信息（分页）
     * @author: LF
     * @date: 2021/05/21
     * @param queryUserVo
     * @return com.geominfo.bi.domain.vo.Message
     */
    @PostMapping(value = AuthApiUrl.USER_LIST_PAGE)
    Message getUsersPage(@RequestBody QueryUserVo queryUserVo);
}
