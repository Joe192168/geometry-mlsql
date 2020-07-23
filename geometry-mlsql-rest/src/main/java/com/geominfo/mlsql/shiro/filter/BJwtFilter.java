package com.geominfo.mlsql.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.shiro.token.JwtToken;
import com.geominfo.mlsql.systemidentification.SystemCustomIdentification;
import com.geominfo.mlsql.util.JsonWebTokenUtil;
import com.geominfo.mlsql.util.RequestResponseUtil;
import com.geominfo.mlsql.utils.IpUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @program: geometry-bi
 * @description: 支持restful url 的过滤链  JWT json web token 过滤器，无状态验证
 * @author: 肖乔辉
 * @create: 2018-08-17 15:39
 * @version: 1.0.0
 */
public class BJwtFilter extends AbstractPathMatchingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BJwtFilter.class);
    private static final String STR_EXPIRED = "expiredJwt";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    TeamRoleService teamRoleService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
//记录系统接口日志由过滤器改为接口记录接口操作日志
//        //记录调用api日志到数据库
//        LogExeManager.getInstance().executeLogTask(LogTaskFactory.bussinssLog(WebUtils.toHttp(servletRequest).getHeader("appId"),
//                WebUtils.toHttp(servletRequest).getRequestURI(),WebUtils.toHttp(servletRequest).getMethod(),(short)1,null));
        boolean isJwtPost = (null != subject && !subject.isAuthenticated()) && isJwtSubmission(servletRequest);
        // 判断是否为JWT认证请求
        if (isJwtPost) {
            LOGGER.info("isJwtPost: [{}]",isJwtPost);
            Map<String,String> maps = RequestResponseUtil.getRequestHeaders(servletRequest);
            LOGGER.info("createJwtToken: [{}]",maps);
            AuthenticationToken token = createJwtToken(servletRequest);
            try {
                subject.login(token);
                return this.checkRoles(subject,mappedValue);
            }catch (AuthenticationException e) {
                LOGGER.error("登录异常:",e);
                // 如果是JWT过期
                if (STR_EXPIRED.equals(e.getMessage())) {
                    // 这里初始方案先抛出令牌过期，之后设计为在Redis中查询当前appId对应令牌，其设置的过期时间是JWT的两倍，此作为JWT的refresh时间
                    // 当JWT的有效时间过期后，查询其refresh时间，refresh时间有效即重新派发新的JWT给客户端，
                    // refresh也过期则告知客户端JWT时间过期重新认证
                    // 当存储在redis的JWT没有过期，即refresh time 没有过期
                    String appId = WebUtils.toHttp(servletRequest).getHeader("appId");
                    String jwt = WebUtils.toHttp(servletRequest).getHeader("authorization");
                    String refreshJwt = redisTemplate.opsForValue().get("JWT-SESSION-"+appId);
                    String productId = WebUtils.toHttp(servletRequest).getHeader(SystemCustomIdentification.PRODUCT_ID);
                    if (org.apache.commons.lang3.StringUtils.isBlank(productId)){
                        productId = GlobalConstant.BIXT_ROOT;
                    }
                    if (null != refreshJwt && refreshJwt.equals(jwt)) {
                        // 重新申请新的JWT
                        // 根据用户登录名查询用户所拥有权限资源id
                        String permissions = null;
                        //根据用户登录名查询用户所拥有的所有角色
                        String roles =  teamRoleService.getRolesByUserName(appId);
                        //过期时间暂改为30分钟
                        String newJwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(),appId,
                                "token-server",JsonWebTokenUtil.refreshPeriodTime >> SystemCustomIdentification.DEFAULT_INT_VALUE,roles,permissions);
                        // 将签发的JWT存储到Redis： {JWT-SESSION-{appID} , jwt}
                        redisTemplate.opsForValue().set("JWT-SESSION-"+appId,newJwt, JsonWebTokenUtil.refreshPeriodTime, TimeUnit.SECONDS);
                        Message message = new Message().ok(1005,"登录超时，请重新登录!").addData("jwt",newJwt);
                        RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                        return false;
                    }else {
                        // jwt时间失效过期,jwt refresh time失效 返回jwt过期客户端重新登录
                        Message message = new Message().error(1006,"登录超时，请重新登录!");
                        RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                        return false;
                    }

                }
                // 其他的判断为JWT错误无效
                Message message = new Message().error(1007,"登录超时，请重新登录!");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                return false;
            }catch (Exception e) {
                // 其他错误
                LOGGER.error(IpUtil.getIpFromRequest(WebUtils.toHttp(servletRequest))+"--JWT认证失败"+e.getMessage(),e);
                // 告知客户端JWT错误1007,需重新登录申请jwt
                Message message = new Message().error(1007,"error jwt");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                return false;
            }
        }else {
            // 请求未携带jwt 判断为无效请求
            Message message = new Message().error(500,"error request");
            RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);
        // 未认证的情况上面已经处理  这里处理未授权
        if (subject != null && subject.isAuthenticated()){
            //  已经认证但未授权的情况
            // 告知客户端JWT没有权限访问此资源
//            Message message = new Message().error(1008,"no permission");
            Message message = new Message().error(1008,"无访问此资源权限！");
            RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
        }
        // 过滤链终止
        return false;
    }

    private boolean isJwtSubmission(ServletRequest request) {

        String jwt = RequestResponseUtil.getHeader(request,"authorization");
        String appId = RequestResponseUtil.getHeader(request,"appId");
        return (request instanceof HttpServletRequest)
                && !StringUtils.isEmpty(jwt)
                && !StringUtils.isEmpty(appId);
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {

        Map<String,String> maps = RequestResponseUtil.getRequestHeaders(request);
        String appId = maps.get("appid");
        String ipHost = request.getRemoteAddr();
        String jwt = maps.get("authorization");
        String deviceInfo = maps.get("deviceInfo");

        return new JwtToken(ipHost,deviceInfo,jwt,appId);
    }

    /**
     * description 验证当前用户是否属于mappedValue任意一个权限
     *
     * @param subject 1
     * @param mappedValue 2
     * @return boolean
     */
    private boolean checkRoles(Subject subject, Object mappedValue){
        String[] rolesArray = (String[]) mappedValue;
        //这块修改成校验权限，加载shiro池为权限值(awh 修改为角色控制权限）
        return rolesArray == null || rolesArray.length == 0 || Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
    }


    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void setAccountService(UserService userService) {
        this.userService = userService;
    }

    public void setQueryPermissionService(TeamRoleService teamRoleService) {
        this.teamRoleService = teamRoleService;
    }
}
