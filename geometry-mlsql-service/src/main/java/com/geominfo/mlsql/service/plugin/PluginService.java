package com.geominfo.mlsql.service.plugin;

import com.geominfo.mlsql.domain.vo.PluginStoreItem;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 插件信息业务接口
 * @author: ryan
 * @create: 2020-11-26 10:38
 * @version: 1.0.0
 */
public interface PluginService {

    /**
     * description:
     * author: ryan
     * date: 2020/11/26
     * param: map
     * return: List<PluginStoreItem>
     */
    List<PluginStoreItem> listPlugins(LinkedMultiValueMap<String, String> paramsMap);


    Map<PluginStoreItem,List<String>> getPlugins(String pluginName, Map map);


}
