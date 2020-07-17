package com.geominfo.mlsql.controller.cluster;


import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.service.cluster.ClusterProxyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class ClusterProxyController {

    @Autowired
    private Message message ;

    @Autowired
    private ClusterProxyService clusterProxyService ;

    @RequestMapping("/api_v1/cluster")
    @ApiOperation(value = "获取用户表授权接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "执行action", name = "action", required = true)
    })
    public ResponseEntity<String> clusterManager(HttpServletRequest request, HttpServletResponse response){

        ResponseEntity<String> result = clusterProxyService.clusterManager(request) ;
        return result ;

    }

}