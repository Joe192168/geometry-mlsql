package com.geominfo.mlsql.service.plugin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.geominfo.mlsql.domain.vo.PluginStoreItem;
import com.geominfo.mlsql.service.plugin.PluginService;
import com.geominfo.mlsql.service.proxy.ProxyService;


import com.geominfo.mlsql.utils.VersionSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: geometry-mlsql
 * @description: 插件信息业务实现类
 * @author: ryan
 * @create: 2020-11-26 10:38
 * @version: 1.0.0
 */
@Service
public class PluginServiceImpl implements PluginService {

    private final static String URL = "http://store.mlsql.tech/run";

    @Autowired
    private ProxyService proxyService;

    @Override
    public List<PluginStoreItem> listPlugins(LinkedMultiValueMap<String, String> paramsMap) {
        ResponseEntity<String> responseEntity = proxyService.postForEntity(URL, paramsMap, String.class);
        String body = responseEntity.getBody();
        //转成对象
        List<PluginStoreItem> pluginStoreItems = JSON.parseObject(body, new TypeReference<List<PluginStoreItem>>() {
        });
        //过滤pluginType == 2的数据信息
        ArrayList<PluginStoreItem> groupPlugin = new ArrayList<>();
        for (PluginStoreItem p : pluginStoreItems) {
            if (p.getPluginType() == 2) {
                groupPlugin.add(p);
            }
        }
        //根据name分组
        Map<String, List<PluginStoreItem>> collect = groupPlugin.stream().collect(Collectors.groupingBy(PluginStoreItem::getName));
        //存储排序版本号最大的信息
        List<PluginStoreItem> versionNum = new ArrayList<>();
        List<PluginStoreItem> lastInfo = new ArrayList<>();
        //遍历出每个插件名中，最大的版本
        for (String key : collect.keySet()) {
            List<PluginStoreItem> value = collect.get(key);
            //排序，将结果存储
            versionNum = sortVersion(value, versionNum);
            //取出最后一个插件版本信息
            lastInfo.add(versionNum.get(versionNum.size()-1));
        }
        return lastInfo;
    }



    @Override
    public Map<PluginStoreItem,List<String>> getPlugins(String pluginName,Map map) {
        HashMap<PluginStoreItem, List<String>> plMap = new HashMap<>();
        //获取名称和版本号
        List<String> pluginNameAndVersion = getPluginNameAndVersion(pluginName);
        //获取接口调用数据
        List<PluginStoreItem> pluginInfo = getPluginInfo(pluginName);
        PluginStoreItem pluginStoreItem = null;
        for(PluginStoreItem p : pluginInfo){
            if (pluginNameAndVersion.get(pluginNameAndVersion.size()-1).equals(p.getVersion())){
                pluginStoreItem = p;
                break;
            }
        }
        plMap.put(pluginStoreItem,pluginNameAndVersion);
        return plMap;
    }

    //
    public List<String> getPluginNameAndVersion(String name){
        //存储名称和 版本
        HashMap<String, String> nameVersionMap = new HashMap<>();
        //存储名称和 版本'
        List<String> nameAndVersion = new ArrayList<>();
        if (name.contains(":")){
            String[] split = name.split(":");
            nameAndVersion.add(split[0]);
            nameAndVersion.add(split[1]);
        }else {
            //调用接口返回数据
            List<PluginStoreItem> pluginInfo = getPluginInfo(name);
            //存储排序版本号最大的信息
            List<PluginStoreItem> versionNum = new ArrayList<>();
            //排序取出最后一个最大版本信息
            versionNum = sortVersion(pluginInfo,versionNum);
            //nameVersionMap.put(name,versionNum.get(versionNum.size()-1).getVersion());
            nameAndVersion.add(name);
            nameAndVersion.add(versionNum.get(versionNum.size()-1).getVersion());
        }
        return nameAndVersion;
    }

    //远程接口调用
    public List<PluginStoreItem> getPluginInfo(String name){
        LinkedMultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("action", "getPlugin");
        postParameters.add("pluginName", name);
        postParameters.add("pluginType", "MLSQL_SCRIPT");
        ResponseEntity<String> responseEntity = proxyService.postForEntity(URL,postParameters,String.class);
        String body = responseEntity.getBody();
        return JSON.parseObject(body, new TypeReference<List<PluginStoreItem>>() { });
    }


    /**
     * list版本排序
     */
    public List<PluginStoreItem> sortVersion(List<PluginStoreItem> pluginInfo,List<PluginStoreItem> versionNum){
        //创建排序需要集合
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        //遍历添加对应的pluginName查询出所有版本号
        for (PluginStoreItem p1 : pluginInfo) {
            map.put("compareKey", p1.getVersion());
        }
        list.add(map);
        //排序，将排好序的数据重新放入list
        Collections.sort(list, new VersionSort("compareKey"));
        //根据最大的版本，获取信息
        for (PluginStoreItem u : pluginInfo) {
            if (list.get(list.size() - 1).get("compareKey").equals(u.getVersion())) {
                versionNum.add(u);
            }
        }
        return versionNum;



    }


}
