package com.geominfo.mlsql.utils;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: springboot_console_test
 * @description: 网络代理类
 * @author: BJZ
 * @create: 2020-07-14 11:42
 * @version: 1.0.0
 */
@Component
public class NetWorkProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (objects.length > 0) {
            String url = (String) objects[0];
            if (!url.startsWith("http:")) {
                objects[0] = "http://" + (CommandUtil.mlsqlUrlType ?
                        CommandUtil.mlsqlEngineUrl() : CommandUtil.mlsqlClusterUrl()) + url + "/";
            }
//            else {
//                objects[0] = CommandUtil.mlsqlClusterUrl() + url +  "/";
//            }
        }
        Object result = methodProxy.invokeSuper(o, objects);
        return result;
    }

    public static HttpUtil getNetWorkProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(new HttpUtil().getClass());
        enhancer.setCallback(new NetWorkProxy());
        return (HttpUtil) enhancer.create();
    }

}