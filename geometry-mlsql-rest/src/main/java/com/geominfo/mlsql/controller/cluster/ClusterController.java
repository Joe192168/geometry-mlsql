package com.geominfo.mlsql.controller.cluster;


import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.ClusterManagerParameter;
import com.geominfo.mlsql.domain.vo.MLSQLRunScriptParameter;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.cluster.ClusterService;
import com.geominfo.mlsql.utils.ParamsUtil;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @program: geometry-mlsql
 * @description: 集群后台配置控制类
 * @author:BJZ
 * @create: 2020-07-14 10:08
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/cluster")
@Api(value = "集群后台配置接口", tags = {"集群后台配置接口"})
@Log4j2
public class ClusterController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ClusterController.class);

    @Autowired
    private ClusterService clusterService;

    @RequestMapping("/api_v1/cluster")
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

        LinkedMultiValueMap<String, String> params = ParamsUtil.objectToMap(clusterManagerParameter);
        ResponseEntity<String> result = clusterService.clusterManager(params);
        return result.getStatusCode().value() == 200 ?
                        success(200, "success").addData("data", result.getBody()) :
                        error(400, "error").addData("data", result.getBody());

    }



    @RequestMapping("/api_v1/run/script")
    @ApiOperation(value = "执行脚本接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "MLSQL script content", name = "sql", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "the user who execute this API default: admin", name = "owner", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "tscript|stream|sql; default is script", name = "jobType", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "query|analyze; default is query", name = "executeMode", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "the last sql in the script will return nothing. default: false", name = "silence", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "If set true, the owner will have their own session otherwise share the same. default: false", name = "sessionPerUser", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "If set true ,please also provide a callback url use `callback` parameter and the job will run in background and the API will return.  default: false", name = "async", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "Used when async is set true. callback is a url. default: false", name = "callback", dataType = "String", paramType = "query"),
            @ApiImplicitParam(value = "disable include statement. default: false", name = "skipInclude", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "disable table authorize . default: true", name = "skipAuth", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "validate mlsql grammar. default: true", name = "skipGrammarValidate", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(value = "proxy parameter,filter backend with this tags", name = "tags", dataType = "String", paramType = "query")
    })
    public Message runScript(@RequestBody MLSQLRunScriptParameter mlsqlRunScriptParameter) throws Exception {

        LinkedMultiValueMap<String, String> params = ParamsUtil.objectToMap(mlsqlRunScriptParameter);
        if (MapUtils.isEmpty(params)) return error(400, "参数为空!");
        if (!params.containsKey("owner")) params.add("owner", userName);
        ResponseEntity<String> result = clusterService.runScript(params);
        logger.info("执行脚本返回结果result = " + result.getBody());

        return result.getStatusCode().value() == 200 ?
                success(200, "success").addData("data", result.getBody()) :
                error(400, "error").addData("data", result.getBody());

    }


}