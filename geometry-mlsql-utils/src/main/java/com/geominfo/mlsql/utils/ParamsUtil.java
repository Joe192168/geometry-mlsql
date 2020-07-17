package com.geominfo.mlsql.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @program: geometry-mlsql
 * @description: 参数解析工具类
 * @author: BJZ
 * @create: 2020-07-09 11:08
 * @version: 1.0.0
 */
@Component
public class ParamsUtil {

    Logger logger = LoggerFactory.getLogger(ParamsUtil.class);

    private Map<String, String> paramsMap = new HashMap<String, String>();

    public String getParam(String key, String defaultValue) {
        String value = null ;
        if(paramsMap.containsKey(key)){
            value = paramsMap.get(key);
        }

        return (value == null || "".equals(value)) ? defaultValue : value;
    }

}
