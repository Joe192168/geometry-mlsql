package com.geominfo.mlsql.controller.cluster;


import com.geominfo.mlsql.config.restful.CustomException;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.utils.ParamsUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @program: geometry-mlsql
 * @description: 集群后台配置控制类
 * @author:BJZ
 * @create: 2020-07-14 10:08
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/cluster")
@Api(value = "集群后台中转接口控制类", tags = {"集群后台中转接口控制类"})
@Log4j2
public class ClusterController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private ClusterService clusterService;


    @Autowired
    private Message message;

    @RequestMapping(value = "/api_v1/cluster", method = RequestMethod.POST)
    @ApiOperation(value = "集群后台配置接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "执行动作所有接口都要传的参数", name = "action", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "/backend/name/check接口需要传的参数", name = "name", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/add 接口需要传的参数", name = "teamName", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/list 接口需要传的参数", name = "tag", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/list/names 接口需要传的参数", name = "names", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/add 接口需要传的参数", name = "url", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/tags/update 接口需要传的参数", name = "id", dataType = "String", paramType = "query", required = false),
            @ApiImplicitParam(value = "/backend/tags/update 接口需要传的参数", name = "merge", dataType = "String", paramType = "query", required = false)

    })
    public Message clusterManager(@RequestBody ClusterManagerParameter clusterManagerParameter) throws Exception {

        Map<String, Object> params = ParamsUtil.objectToMap(clusterManagerParameter);
        Map<Integer, Object> resMap = clusterService.clusterManager(params);
        return message.returnValue(resMap);

    }


    @RequestMapping(value = "/api_v1/run/script", method = RequestMethod.POST)
    @ApiOperation(value = "执行脚本接口", httpMethod = "POST")
    public Message runScript(@RequestBody ScriptRun scriptRun
    ) throws Exception {
        Map<String, Object> params = ParamsUtil.objectToMap(scriptRun);
        if (!ParamsUtil.containsKey("owner") && params.containsKey("owner"))
            ParamsUtil.setParam("owner", params.get("owner"));
        if (!params.containsKey("owner")) params.put("owner", scriptRun.getOwner());

        Map<Integer ,Object> temp = clusterService.runScript(params);
        if(temp.containsKey(500)){
            CustomException e = (CustomException)temp.get(500);
            temp.put(500,e.getBody());
        }
        return message.returnValue(temp);
    }


}