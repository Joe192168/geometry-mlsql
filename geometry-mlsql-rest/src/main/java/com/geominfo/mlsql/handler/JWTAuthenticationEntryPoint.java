package com.geominfo.mlsql.exception;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.commons.Result;
import com.geominfo.mlsql.commons.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @Description:    没有携带token或者token无效
* @Author:         xqh
* @CreateDate:     2021/3/17 16:16
* @Version:        1.0
*/
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 用户未登录返回结果
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new Result(ResultCode.UNAUTHENTICATED)));
    }
}