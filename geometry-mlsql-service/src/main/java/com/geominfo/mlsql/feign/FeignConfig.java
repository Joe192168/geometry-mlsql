package com.geominfo.mlsql.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * 服务间调用携带authorization请求头,拦截器统一处理
 */
@Configuration
public class FeignConfig implements RequestInterceptor {

    private static final String TOKEN_HEADER = "authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String token = null;
        if(attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            token = request.getHeader(TOKEN_HEADER);
        }
        //添加token
        requestTemplate.header(TOKEN_HEADER, token);
    }

}
