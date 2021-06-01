package com.geominfo.mlsql.aop;

import com.geominfo.mlsql.commons.SystemConstant;
import com.geominfo.mlsql.domain.pojo.User;
import com.geominfo.mlsql.domain.vo.OperateLogVo;
import com.geominfo.mlsql.services.AuthApiService;
import com.geominfo.mlsql.services.AuthQueryApiService;
import com.geominfo.mlsql.utils.FeignUtils;
import com.geominfo.mlsql.utils.IpUtil;
import com.geominfo.mlsql.utils.RequestResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;


@Component
@Aspect
public class LogAspect {

    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private AuthQueryApiService authQueryApiService;
    @Autowired
    private AuthApiService authApiService;

    //设置切入点：这里直接拦截被@RestController注解的类
    @Pointcut("@annotation(com.geominfo.mlsql.aop.GeometryLogAnno)")
    public void picut() {
    }

    //环绕通知 有了环绕通知，前置和后置可以不用
    @Around(value = "picut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //会执行用户当前访问的方法
        Object object = point.proceed();
        //访问目标方法的参数 可动态改变参数值
        //Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        //利用aop切面动态获取ApiOperation注解的value值
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        String value = apiOperation.value();
        //获取类名
        //String className = point.getTarget().getClass().getName();
        //获取方法名
        String methodName = signature.getName();
        //记录日志参数
        GeometryLogAnno geometryLogAnno = method.getAnnotation(GeometryLogAnno.class);
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Map<String, String> maps = RequestResponseUtil.getRequestHeaders(request);
        String appId = maps.get("appid");
        //如果是简化模式进去，则直接通过传参获取操作人
        if ("sendUserInfo".equals(methodName)) {
            String operateUserId = (String) AnnotationResolver.newInstance().resolver(point, geometryLogAnno.operateUserId());
            appId = operateUserId;
        }
        //登录的时候是通过body传入参数
        if (StringUtils.isEmpty(appId)) {
            Map<String, String> bodyMap = RequestResponseUtil.getRequestBodyMap(request);
            appId = bodyMap.get("appId");
        }
        logger.info("method # around request auth get userInfo param loginName:{}", appId);
        User user = FeignUtils.parseObject(authQueryApiService.getUserByLoginName(appId), User.class);
        logger.info("method # around request auth get userInfo user:{}", user);
        if (user != null)
            appId = user.getId().toString();
        String productId = "";
        //获取ip
        String ipAddr = IpUtil.getIpFromRequest(request);
        //判断是否忽略记录日志
        if (geometryLogAnno != null && geometryLogAnno.ignore()) {
            return object;
        }
        OperateLogVo operateLogVo = new OperateLogVo();
        if (geometryLogAnno != null) {
            productId = SystemConstant.SYSTEM_ROOT.toString();
        }
        operateLogVo.setOperateAppId(Integer.parseInt(productId));
        operateLogVo.setOperateIp(ipAddr);
        operateLogVo.setOperateLog(value);
        operateLogVo.setOperateType(geometryLogAnno.operateType().getLogTypeNum());
        operateLogVo.setOperateUserId(Integer.parseInt(appId));
        authApiService.recordInterfaceLog(operateLogVo);
        return object;
    }


}
