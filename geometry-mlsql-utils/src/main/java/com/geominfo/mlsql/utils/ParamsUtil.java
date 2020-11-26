package com.geominfo.mlsql.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
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

    public static  LinkedMultiValueMap<String, String> objectToMap(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter!=null ? getter.invoke(obj) : null;
            map.put(key, value);
        }

        LinkedMultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>() ;
        for(Map.Entry entry : map.entrySet()){
            paramsMap.add((String)entry.getKey().toString(),(String)entry.getValue());
        }

        return paramsMap;
    }

    public static  LinkedMultiValueMap<String, String> MapToLinkedMultiValueMap(Map<String, String> map){
        LinkedMultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>() ;
        for(Map.Entry entry : map.entrySet()){
            paramsMap.add(entry.getKey().toString(),entry.getValue().toString());
        }

        return paramsMap;
    }


}
