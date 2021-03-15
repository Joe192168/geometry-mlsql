package com.geominfo.mlsql.service.base;

import com.geominfo.mlsql.service.proxy.ProxyService;
import com.geominfo.mlsql.utils.CommandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: springboot_console_test
 * @description: 服务基础类
 * @author: BJZ
 * @create: 2020-07-09 11:08
 * @version: 1.0.0
 */
@Service
public class BaseServiceImpl {

    @Autowired
    private ProxyService netWorkUtil ;

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

    protected ResponseEntity<String> cPost(String url, LinkedMultiValueMap<String, String> params) {
//        System.out.println("run rul = " +CommandUtil.mlsqlEngineUrl() );
        return  netWorkUtil.postForEntity(CommandUtil.mlsqlEngineUrl()+url ,params ,String.class) ;
    }



}