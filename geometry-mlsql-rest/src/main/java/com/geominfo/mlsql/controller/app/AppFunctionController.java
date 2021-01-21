package com.geominfo.mlsql.controller.app;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.app.AppFunctionService;
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
 * @description: AppFunctionController
 * @author: BJZ
 * @create: 2021-01-19 09:23
 * @version: 1.0.0
 */
@RestController
@Api(value = "系统函数控制类",tags = {"系统函数控制类"})
@Log4j2
public class AppFunctionController  extends BaseController{

    @Autowired
    private AppFunctionService appFunctionService ;
    @Autowired
    private Message message;

    @RequestMapping(value = "/api_v1/appfunction", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "系统函数接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "搜索参数",
                    name = "fName", dataType = "String", paramType = "query" ,required =  false)
    })
    public Message appFunction(@RequestParam(value = "fName" ,required = false) String fName ){

        log.info("fName = " + fName);
        if(!ParamsUtil.containsKey("owner"))
            ParamsUtil.setParam("owner" , userName);
        Map<Integer ,Object> resMap = appFunctionService.find(fName) ;
        return message.returnValue(resMap);
    }
}