package com.geominfo.mlsql.controller.plugin;


import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.PluginStoreItem;
import com.geominfo.mlsql.service.plugin.PluginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 插件控制类
 * @author: ryan(丁帅波)
 * @create: 2020-11-26 10:36
 * @version: 1.0.0
 */

@RestController
@Api(value = "插件信息控制类", tags = {"插件信息控制类"})
@Log4j2
@RequestMapping(value = "/api_v1/plugin")
public class PluginController extends BaseController {

    @Autowired
    private PluginService pluginService;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "获取所有插件信息")
    public Message list() {
        LinkedMultiValueMap<String, String> postParameters = new LinkedMultiValueMap<String, String>();
        postParameters.add("action", "listPlugins");
        List<PluginStoreItem> pluginStoreItems = pluginService.listPlugins(postParameters);
        return success(HttpStatus.SC_OK, "get success").addData("data", pluginStoreItems);
    }

    @RequestMapping(value = "/get", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "根据插件名获取插件信息")
    @ApiImplicitParam(name = "pluginName", value = "插件名", required = true, paramType = "query", dataType = "String")
    public Message get(@RequestParam(value = "pluginName", required = true) String pluginName) {
        Map<PluginStoreItem, List<String>> plugins = pluginService.getPlugins(pluginName, new HashMap<Object, Object>());
        PluginStoreItem pluginStoreItem = null;
        List<String> list = null;
        for(PluginStoreItem key : plugins.keySet()){
            pluginStoreItem = key;
            list = plugins.get(key);
        }
        if (!pluginStoreItem.getVersion().equals(list.get(list.size()-1))){
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"Plugin" + pluginName + " ：" + list.get(list.size()-1) + "is not exists");
        }
        return success(HttpStatus.SC_OK,"success").addData("data",pluginStoreItem.getExtraParams());
    }
}
