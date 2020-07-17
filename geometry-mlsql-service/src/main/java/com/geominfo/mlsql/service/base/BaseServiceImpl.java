package com.geominfo.mlsql.service.base;



import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;


/**
 * @program: springboot_console_test
 * @description: 服务基础类
 * @author: BJZ
 * @create: 2020-07-09 11:08
 * @version: 1.0.0
 */
public class BaseServiceImpl {

    protected MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<String, String>();

    protected String[] params(String key ,HttpServletRequest request) {
        return request.getParameterMap().get(key);
    }

    protected String param(String key ,HttpServletRequest request) {
        return request.getParameter(key);
    }

    protected boolean hasParam(String key ,HttpServletRequest request)
    {
        return request.getParameterMap().containsKey(key) ;
    }




}