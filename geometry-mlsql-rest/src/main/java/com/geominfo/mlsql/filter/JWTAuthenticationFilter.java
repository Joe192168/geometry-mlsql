package com.geominfo.mlsql.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geominfo.mlsql.commons.Result;
import com.geominfo.mlsql.commons.ResultCode;
import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.utils.JwtTokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
* @Description:    认证
* @Author:         xqh
* @CreateDate:     2021/3/17 16:30
* @Version:        1.0
*/
@Log4j2
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    //自定义登陆路由，相当Controller里增加一个/login路由方法一样，就不需要实现登陆方法了
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/login");
    }

    /**
     * 接收并解析用户凭证 身份验证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,HttpServletResponse response) throws AuthenticationException {
        // 从输入流中获取到登录的信息
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getLoginName(), user.getPassword());
            if (authRequest==null){
                return null;
            }
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("身份验证异常：",e);
            return null;
        }
    }

    /**
     * 成功验证后调用的方法 如果验证成功，就生成token并返回
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        logger.info("jwtUser:"+jwtUser.toString());
        String token = JwtTokenUtils.createToken(jwtUser, false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new Result(ResultCode.SUCCESS,JwtTokenUtils.TOKEN_PREFIX + token)));
    }

    /**
     * 这是验证失败时候调用的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new Result(ResultCode.MOBILEORPASSWORDERROR)));
    }
}