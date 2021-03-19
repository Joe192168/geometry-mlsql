package com.geominfo.mlsql.services.feign;

import com.geominfo.mlsql.commons.AuthApiUrl;
import com.geominfo.mlsql.commons.Message;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ProjectName: geometry-bi
 * @Description: 身份认证平台-数据查询接口
 * @Author: LF
 * @Date: 2020/7/8 16:19
 * @Version: 1.0.0
 */
@FeignClient(name="authQueryService",url = "${auth.server-url}",fallbackFactory = FeignUserService.FeignUserServiceFallbackFactory.class)
public interface FeignUserService {

    /***
     * @description: 根据登录名查询用户信息
     * @author: LF
     * @date: 2020/11/17
     * @param [loginName]
     * @return com.geominfo.bi.domain.vo.Message
     */
    @GetMapping(value = AuthApiUrl.GET_USER_LN)
    Message getUserByLoginName(@PathVariable(value = "loginName") String loginName);

    @Slf4j
    @Component
    class FeignUserServiceFallbackFactory implements FallbackFactory<FeignUserService> {
        @Override
        public FeignUserService create(Throwable throwable) {
            return new FeignUserService() {
                @Override
                public Message getUserByLoginName(String loginName) {
                    log.error("fallback 根据登录名查询用户信息时发生异常，请稍后再试！ ..." ,throwable);
                    return new Message().error("根据登录名查询用户信息时发生异常，请稍后再试！");
                }
            };
        }
    }

}
