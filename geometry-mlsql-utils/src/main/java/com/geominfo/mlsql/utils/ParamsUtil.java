package com.geominfo.mlsql.utils;


import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @program: geometry-mlsql
 * @description: 参数解析工具类
 * @author: BJZ
 * @create: 2020-07-09 11:08
 * @version: 1.0.0
 */
@Component
public class ParamsUtil {

    //    Logger logger = LoggerFactory.getLogger(ParamsUtil.class);
    private volatile static Map<String, Object> paramsMap = new ConcurrentHashMap<>();

    public static Object getParam(String key, Object defaultValue) {
        Object value = null;
        if (paramsMap.containsKey(key))
            value = paramsMap.get(key);

        return value == null ? defaultValue : value;
    }

    public static void setParam(String key, Object value) {
        if (key != null && value != null)
            paramsMap.put(key, value);
    }

    public static boolean containsKey(String key) {
        return paramsMap.containsKey(key);
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        Map<String, Object> map = new ConcurrentHashMap<>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            if (value != null)
                map.put(key, value);
        }
        return map;
    }

    public static LinkedMultiValueMap<String, String> MapToLinkedMultiValueMap(Map<String, String> map) {
        LinkedMultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        for (Map.Entry entry : map.entrySet()) {
            paramsMap.add(entry.getKey().toString(), entry.getValue().toString());
        }

        return paramsMap;
    }


}
