package com.geominfo.mlsql.services.impl;


import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.param.AccountParam;
import com.geominfo.mlsql.domain.vo.OperateLogVo;
import com.geominfo.mlsql.domain.vo.UserSessionVo;
import com.geominfo.mlsql.services.AuthApiService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2020/11/23 16:16
 * @version: 1.0.0
 */

@Slf4j
@Component
public class AuthApiServiceFactory implements FallbackFactory<AuthApiService> {
    @Override
    public AuthApiService create(Throwable throwable) {
        return new AuthApiService() {


            @Override
            public Message recordInterfaceLog(OperateLogVo operateLogVo) {
                log.error("#recordInterfaceLog param:{}",operateLogVo);
                log.error("fallback 记录日志接口..." ,throwable);
                return new Message().error("记录接口日志时发生异常，请稍后再试！");
            }

            @Override
            public Message userSessionManager(UserSessionVo userSessionVo) {
                log.error("#userSessionManager param:{}",userSessionVo);
                log.error("fallback 用户会话管理接口..." ,throwable);
                return new Message().error("用户会话管理接口发生异常，请稍后再试！");
            }

            @Override
            public Message updateAccountInfo(AccountParam accountParam) {
                log.error("#updateAccountInfo param:{}",accountParam);
                log.error("fallback 修改账户、密码接口..." ,throwable);
                return new Message().error("修改账户、密码接口发生异常，请稍后再试！");
            }
        };
    }
}
