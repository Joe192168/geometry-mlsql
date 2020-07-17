package com.geominfo.mlsql.interceptor;

import com.geominfo.mlsql.domain.vo.JwtAccount;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import com.geominfo.mlsql.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: LoginHandlerInterceptor
 * @author: anan
 * @create: 2020-07-17 10:15
 * @version: 1.0.0
 */
@Log4j2
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5NDZmMTc5M2U2YmE0NDEzYmUyMjRmZmNkODJhYTViMiIsInN1YiI6Ik1MU1FMX0NPTlNPTEUiLCJpc3MiOiJ0b2tlbi1zZXJ2ZXIiLCJpYXQiOjE1OTQ5NTQxNjgsImV4cCI6MTU5NDk3MjE2OCwicm9sZXMiOiJ7XCJiYWNrZW5kVGFnc1wiOlwiYmlnZGF0YV9hZG1pblwiLFwiaWRcIjoyLFwibmFtZVwiOlwiYXdoQGdtYWlsLmNvbVwiLFwicGFzc3dvcmRcIjpcIu-_vVxcbu-_vTlJ77-9We-_ve-_vVbvv71X77-9XFx1MDAwRu-_vT5cIixcInJvbGVcIjpcImRldmVsb3BlclwifSIsInBlcm1zIjoiYXdoQGdtYWlsLmNvbSJ9.WwLwozlxxrYnWQmvKaJcq3GLprteoZwW0EY0Vv7pRMo";
//        //JwtAccount jwtAccount = JsonWebTokenUtil.parseJwt(request.getHeader("jwt"));
//        JwtAccount jwtAccount = JsonWebTokenUtil.parseJwt(jwt);
//        log.info(jwtAccount.getAppId());
//        log.info(jwtAccount.getRoles());
//        log.info(jwtAccount.getIssuedAt());
//        log.info(JsonWebTokenUtil.parseJwtPayload(jwt));
        //"jti":"946f1793e6ba4413be224ffcd82aa5b2","sub":"MLSQL_CONSOLE","iss":"token-server","iat":1594954168,"exp":1594972168
        //Fri Jul 17 10:49:28 CST 2020
//        String token = request.getHeader("token");
//        MlsqlUser user = null;
//        if (StringUtils.isEmpty(token)){
//    //      user = authenticator.getAuthUser(request);
//          if (user == null) {
//            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
//            log.info("user does not exist");
//            return false;
//          }
//        }else {
//    //       user = userMapper.queryUserByToken(token);
//          if (user == null) {
//            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
//              log.info("user token has expired");
//            return false;
//          }
//        }
    //    request.setAttribute(Constants.SESSION_USER, user);
            return true;
        }
}
