package com.geominfo.mlsql.controller.cloud;

import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.ClusterManagerParameter;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlEngine;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.cloud.CloudService;
import com.geominfo.mlsql.utils.ParamsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: CloudController
 * @author: anan
 * @create: 2020-11-25 14:25
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/api_v1")
@Api(value="Cloud维护接口类",tags={"Cloud维护接口"})
@Log4j2
public class CloudController  extends BaseController {

    @Autowired
    private CloudService cloudService;

    @Value("${cloud.url}")
    private String cloudUrl;

    @RequestMapping(value = "/proxy/api/create_engine", method = RequestMethod.POST)
    @ApiOperation(value = "新增engine", httpMethod = "POST")
    public Message createEngine(@RequestParam Map<String, String> map){
        ResponseEntity<String> responseEntity = null;
        try {
            LinkedMultiValueMap<String, String> params =  ParamsUtil.MapToLinkedMultiValueMap(map);
            params.set("url", cloudUrl+"/api/create_engine");
            params.set("UserName", userName);
            responseEntity = cloudService.execReqUrlAndResponse(params);

            return success(ReturnCode.RETURN_SUCCESS_STATUS,"operator success")
                    .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return success(ReturnCode.RETURN_ERROR_STATUS,"operator faild")
                    .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());
        }
    }

    @RequestMapping(value = "/proxy/api/delete_engine", method = RequestMethod.POST)
    @ApiOperation(value = "删除engine", httpMethod = "POST")
    public Message deleteEngine(@RequestParam Map<String, String> map){
        ResponseEntity<String> responseEntity = null;
        try {
            LinkedMultiValueMap<String, String> params =  ParamsUtil.MapToLinkedMultiValueMap(map);
            params.set("url", cloudUrl+"/api/delete_engine");
            params.set("UserName", userName);
            responseEntity = cloudService.execReqUrlAndResponse(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"operator success")
                .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());
    }

    @RequestMapping(value = "/proxy/api/status", method = RequestMethod.GET)
    @ApiOperation(value = "获取engine状态", httpMethod = "GET")
    public Message status(@RequestParam Map<String, String> map){
        ResponseEntity<String> responseEntity = null;
        try {
            LinkedMultiValueMap<String, String> params =  ParamsUtil.MapToLinkedMultiValueMap(map);
            params.set("url", cloudUrl+"/api/status");
            params.set("UserName", userName);
            responseEntity = cloudService.execReqUrlAndResponse(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"operator success")
                .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());
    }

    @RequestMapping(value = "/proxy/api/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取engine列表", httpMethod = "GET")
    public Message list(@RequestParam Map<String, String> map){
        ResponseEntity<String> responseEntity = null;
        try {
            LinkedMultiValueMap<String, String> params = ParamsUtil.MapToLinkedMultiValueMap(map);
            params.set("url", cloudUrl+"/api_v1/engine/list");
            params.set("UserName", userName);
            responseEntity = cloudService.execReqUrlAndResponse(params);
            return success(ReturnCode.RETURN_SUCCESS_STATUS,"operator success")
                    .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());

        } catch (Exception e) {
            e.printStackTrace();

            return success(ReturnCode.RETURN_ERROR_STATUS,"operator faild")
                    .addData("data",responseEntity.getStatusCode() + responseEntity.getBody());
        }
    }
}
