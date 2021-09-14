package com.geominfo.mlsql.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geominfo.authing.common.enums.EnumOperateLogType;
import com.geominfo.authing.common.utils.IdWorker;
import com.geominfo.mlsql.aop.GeometryLogAnno;
import com.geominfo.mlsql.commons.*;
import com.geominfo.mlsql.domain.dto.JwtUser;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.domain.vo.UserPermissionInfosVo;
import com.geominfo.mlsql.services.SystemPermissionService;
import com.geominfo.mlsql.utils.JwtTokenUtils;
import com.geominfo.mlsql.utils.TreeVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
* @Description:    认证
* @Author:         xqh
* @CreateDate:     2021/3/17 16:30
* @Version:        1.0
*/
@Log4j2
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private AuthenticationManager authenticationManager;
    @Autowired
    private SystemPermissionService systemPermissionService;

    //自定义登陆路由，相当Controller里增加一个/login路由方法一样，就不需要实现登陆方法了
    public JWTAuthenticationFilter(IdWorker idWorker,StringRedisTemplate redisTemplate,AuthenticationManager authenticationManager,SystemPermissionService systemPermissionService) {
        this.idWorker = idWorker;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
        this.systemPermissionService = systemPermissionService;
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
    @GeometryLogAnno(operateType = EnumOperateLogType.MLSQL_LOGIN_OPERATE)
    protected void successfulAuthentication(HttpServletRequest request,HttpServletResponse response,FilterChain chain,
                                            Authentication authResult) throws IOException  {
        // 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
        // 所以就是JwtUser啦
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        logger.info("jwtUser:"+jwtUser.toString());
        // 返回创建成功的token
        String token = JwtTokenUtils.createToken(jwtUser, false);
        //获取token的唯一标识
        String tokenId = idWorker.nextId()+"";
        //获取人员的权限信息
        UserPermissionInfosVo userPermissionInfosVos = systemPermissionService.getUserPermissionInfos(token,jwtUser);
        //用户的权限树
        List<TreeVo<SystemResourceVo>> allPermissionTrees = null;
        //用户信息
        User user = userPermissionInfosVos.getUser();
        String jwt = null;
        if (null != userPermissionInfosVos){
            //查询用户的权限树
            allPermissionTrees = userPermissionInfosVos.getPermissionTrees();
            //获取用户基本信息
            user = userPermissionInfosVos.getUser();
            //获取jwt
            jwt = userPermissionInfosVos.getJwt();
            //会话管理
            systemPermissionService.userSession(user,jwt,tokenId,request);
        }
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        // 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
        redisTemplate.opsForValue().set(SystemCustomIdentification.TOKEN_FOLDER+tokenId, JwtTokenUtils.TOKEN_PREFIX + token, 1800, TimeUnit.SECONDS);
        response.getWriter().write(JSON.toJSONString(new Message().ok("登陆成功")
                .addData("token",JwtTokenUtils.TOKEN_PREFIX + token)
                .addData("tokenId",tokenId).addData("allPermissionTrees",allPermissionTrees).addData("user",user)));
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