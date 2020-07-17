package com.geominfo.mlsql.utils;

import com.geominfo.mlsql.constants.Constants;
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
        if (objects.length > Constants.ZERO) {
            String url = (String) objects[Constants.ZERO];
            if (!url.startsWith(Constants.HTTP_HEAD_ONE)) {
                objects[Constants.ZERO] = Constants.HTTP_HEAD_TOW + CommandUtil.mlsqlClusterUrl() + url + Constants.HTTP_SEPARATED;
            } else {
                objects[Constants.ZERO] = CommandUtil.mlsqlClusterUrl() + url + Constants.HTTP_SEPARATED;
            }
        }
        Object result = methodProxy.invokeSuper(o, objects);
        return result;
    }

    public static NetWorkUtil getNetWorkProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(new NetWorkUtil().getClass());
        enhancer.setCallback(new NetWorkProxy());
        return (NetWorkUtil) enhancer.create();
    }
}