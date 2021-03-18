package com.geominfo.mlsql.exception;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.commons.Result;
import com.geominfo.mlsql.commons.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @Description:    处理没有访问权限
* @Author:         xqh
* @CreateDate:     2021/3/17 16:11
* @Version:        1.0
*/
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(new Result(ResultCode.UNAUTHORISE)));
    }
}