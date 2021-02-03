package com.geominfo.mlsql.controller.plugin;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.app.AppFunctionService;
import com.geominfo.mlsql.service.plugin.PluginFunctionService;
import com.geominfo.mlsql.utils.ParamsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @program: geometry-mlsql
 * @description: PluginFunctionController
 * @author: BJZ
 * @create: 2021-01-27 09:23
 * @version: 1.0.0
 */
@RestController
@Api(value = "插件函数控制类",tags = {"插件函数控制类"})
@Log4j2
public class PluginFunctionController extends BaseController{


    @Autowired
    private PluginFunctionService pluginFunctionService ;

    @Autowired
    private Message message;

    @RequestMapping(value = "/api_v1/pluginfunction", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "插件函数接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "搜索参数",
                    name = "pName", dataType = "String", paramType = "query" ,required =  false)
    })
    public Message appFunction(@RequestParam(value = "pName" ,required = false) String pName ){

        log.info("pName = " + pName);
        if(!ParamsUtil.containsKey("owner"))
            ParamsUtil.setParam("owner" , userName);
        Map<Integer ,Object> resMap = pluginFunctionService.find(pName) ;
        return message.returnValue(resMap);
    }
}