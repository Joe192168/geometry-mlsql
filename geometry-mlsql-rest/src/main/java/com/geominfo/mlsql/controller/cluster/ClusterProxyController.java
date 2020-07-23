package com.geominfo.mlsql.controller.cluster;



import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.service.cluster.ClusterProxyService;
import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
@Api(value="集群后台配置接口",tags={"集群后台配置接口"})
@Log4j2
public class ClusterProxyController extends BaseController{

    @Autowired
    private Message message ;

    @Autowired
    private ClusterProxyService clusterProxyService ;

    @RequestMapping("/api_v1/cluster")
    @ApiOperation(value = "集群后台配置接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "执行动作", name = "action", dataType = "String", paramType = "query", required = true)
    })
    public Message clusterManager(@ApiParam(value="action", required = true) String action){

        ResponseEntity<String> result = clusterProxyService.clusterManager(action) ;
        return result.getStatusCode().value() == GlobalConstant.TOW_HUNDRED ? message.ok(result.toString()) :  message.error(result.toString());

    }


    @RequestMapping("/api_v1/run/script")
    @ApiOperation(value = "执行脚本接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "脚本", name = "sql", dataType = "String", paramType = "query", required = true)
    })
    public Message runScript(@ApiParam(value="sql", required = true) String sql){

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<String, String>();
        paramsMap.add("sql" ,sql);
        ResponseEntity<String> result = clusterProxyService.runScript(paramsMap) ;
        return result.getStatusCode().value() == GlobalConstant.TOW_HUNDRED ? message.ok(result.toString()) :  message.error(result.toString());

    }

}