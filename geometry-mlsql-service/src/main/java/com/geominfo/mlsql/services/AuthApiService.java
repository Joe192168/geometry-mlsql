package com.geominfo.mlsql.services;

import com.geominfo.mlsql.commons.AuthApiUrl;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.vo.OperateLogVo;
import com.geominfo.mlsql.domain.vo.UserSessionVo;
import com.geominfo.mlsql.services.impl.AuthApiServiceFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @program: geometry-bi
 * @description: 身份认证平台-数据操作接口
 * @author: LF
 * @create: 2020/11/23 16:13
 * @version: 1.0.0
 */
@FeignClient(name="AuthApiService",url = "${auth.server-url}",fallbackFactory = AuthApiServiceFactory.class)
public interface AuthApiService {

    /***
     * @description: 记录日志接口
     * @author: LF
     * @date: 2020/11/24
     * @param operateLogVo
     * @return com.geominfo.bi.domain.vo.Message
     */
    @PostMapping(value= AuthApiUrl.OPERATION_LOG)
    Message recordInterfaceLog(@RequestBody OperateLogVo operateLogVo);

    /***
     * @description: 用户会话管理接口
     * @author: LF
     * @date: 2020/11/24
     * @param userSessionVo
     * @return com.geominfo.bi.domain.vo.Message
     */
    @PostMapping(value= AuthApiUrl.USER_SESSION)
    Message userSessionManager(@RequestBody UserSessionVo userSessionVo);




}
