//package com.geominfo.mlsql.services.feign;
//
//import com.geominfo.mlsql.commons.AuthApiUrl;
//import com.geominfo.mlsql.commons.Message;
//import com.geominfo.mlsql.feign.FeignDisableHystrixConfiguration;
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient(name="authSystemService",url = "${auth.server-url}",fallbackFactory = FeignRoleService.FeignRoleServiceFallbackFactory.class,configuration = FeignDisableHystrixConfiguration.class)
//public interface FeignRoleService {
//
//    /**
//     * 获取系统的权限池
//     *
//     * @param systemId
//     * @return
//     */
//    @GetMapping(value = AuthApiUrl.ROLE_PERM_RULE)
//    Message getSystemRoles(@PathVariable(value = "systemId") Integer systemId);
//
//    /***
//     * @description: 根据登录名查询用户的角色
//     */
//    @GetMapping(value = AuthApiUrl.GET_USER_ROLE)
//    Message getUserRole(@PathVariable(value = "loginName") String loginName);
//
//    @Slf4j
//    @Component
//    class FeignRoleServiceFallbackFactory implements FallbackFactory<FeignRoleService> {
//        @Override
//        public FeignRoleService create(Throwable throwable) {
//            return new FeignRoleService() {
//                @Override
//                public Message getSystemRoles(Integer systemId) {
//                    log.error("fallback 查询权限池时发生异常，请稍后再试！...", throwable);
//                    return new Message().error("查询权限池时发生异常，请稍后再试！");
//                }
//                @Override
//                public Message getUserRole(String loginName) {
//                    log.error("fallback 根据登录名查询用户的角色时发生异常，请稍后再试！ ..." ,throwable);
//                    return new Message().error("根据登录名查询用户的角色时发生异常，请稍后再试！");
//                }
//            };
//        }
//    }
//
//}