package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.vo.QueryUserVo;
import com.geominfo.mlsql.services.AuthApiService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 身份服务平台接口调用
 * @author: LF
 * @create: 2021/5/20 17:00
 * @version: 1.0.0
 */
@Slf4j
@Component
public class AuthApiServiceFactory implements FallbackFactory<AuthApiService> {
    @Override
    public AuthApiService create(Throwable throwable) {
        return new AuthApiService() {
            @Override
            public Message getSystemRoles(Integer systemId) {
                log.error("fallback 查询权限池时发生异常，请稍后再试！...", throwable);
                return new Message().error("查询权限池时发生异常，请稍后再试！");
            }

            @Override
            public Message getUserRole(String loginName) {
                log.error("fallback 根据登录名查询用户的角色时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("根据登录名查询用户的角色时发生异常，请稍后再试！");
            }

            @Override
            public Message getUserByLoginName(String loginName) {
                log.error("fallback 根据登录名查询用户信息时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("根据登录名查询用户信息时发生异常，请稍后再试！");
            }

            @Override
            public Message getUserById(BigDecimal userId) {
                log.error("fallback 根据用户id查询用户信息时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("根据用户id查询用户信息时发生异常，请稍后再试！");
            }

            @Override
            public Message getUserByResourceId(BigDecimal resourceId) {
                log.error("fallback 根据用户资源id查询用户信息时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("根据用户资源 id查询用户信息时发生异常，请稍后再试！");
            }

            @Override
            public Message getUserPermissions(BigDecimal userId, BigDecimal appId) {
                log.error("fallback 查询用户的所有权限信息时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("查询用户的所有权限信息时发生异常，请稍后再试！");
            }

            @Override
            public Message getPermissionIdsByLoginNameAndAppId(String loginName, BigDecimal appId) {
                log.error("fallback 查询用户的所有权限的id时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("查询用户的所有权限的id时发生异常，请稍后再试！");
            }

            @Override
            public Message getUsers() {
                log.error("fallback 查询人员信息列表时发生异常，请稍后再试！ ..." ,throwable);
                return new Message().error("查询人员信息列表时发生异常，请稍后再试！");
            }

            @Override
            public Message getUsersPage(QueryUserVo queryUserVo) {
                log.error("fallback 查询人员信息列表(分页)..." ,throwable);
                return new Message().error("查询人员信息列表(分页)时发生异常，请稍后再试！");
            }

        };
    }
}
