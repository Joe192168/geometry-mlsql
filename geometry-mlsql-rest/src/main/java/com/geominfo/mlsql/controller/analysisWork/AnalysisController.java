package com.geominfo.mlsql.controller.analysisWork;


import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.analysisWork.MlsqlWorkshopTableService;
import com.geominfo.mlsql.service.analysisWork.impl.MlsqlWorkshopTableServiceImpl;
import com.geominfo.mlsql.service.app.MlsqlApplyService;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.service.job.impl.MlsqlJobServiceImpl;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
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

import java.util.HashMap;
import java.util.List;

/**
 * @program: geometry-mlsql
 * @description: 分析工作信息控制类
 * @author: ryan(丁帅波)
 * @create: 2020-11-10 09:45
 * @version: 1.0.0
 */

@Api(value = "分析工坊信息", tags = {"分析工坊信息"})
@RestController
@RequestMapping(value = "/api_v1/analysis")
@Log4j2
public class AnalysisController extends BaseController {

    @Autowired
    private Message message;

    @Autowired
    private UserService userService;

    @Autowired
    private MlsqlWorkshopTableService mlsqlWorkshopTableService;

    @Autowired
    private MlsqlJobService mlsqlJobService;

    @Autowired
    private MlsqlApplyService mlsqlApplyService;


    @RequestMapping(value = "/tables", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "获取工坊列表信息", httpMethod = "POST", notes = "该接口POST,GET两种请求都支持")
    @ApiImplicitParam(name = "sessionId", value = "sessionId", required = false, paramType = "query", dataType = "String")
    public Message tables(@RequestParam(value = "sessionId", required = false) String sessionId) {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        List<MlsqlWorkshopTable> mlsqlWorkshopList;
        if (sessionId != null && sessionId.length() != 0) {
            mlsqlWorkshopList = mlsqlWorkshopTableService.getMlsqlWorkshopList(sessionId, null);
            return message.addData("data", mlsqlWorkshopList);
        }
        mlsqlWorkshopList = mlsqlWorkshopTableService.getMlsqlWorkshopList("", 1);
        return message.addData("data", mlsqlWorkshopList);
    }


    @RequestMapping(value = "/apply")
    @ApiOperation(value = "根据应用名称获取单个应用信息", httpMethod = "GET" )
    @ApiImplicitParam(name = "name", value = "name", required = true, paramType = "query", dataType = "String")
    public Message apply(@RequestParam(value = "name",required = true) String name){
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("userId",1);
        List<MlsqlApply> mlsqlApplyList = mlsqlApplyService.getMlsqlApplyList(map);
        MlsqlApply mlsqlApply = null;
        if (mlsqlApplyList.size() > 0){
            mlsqlApply = mlsqlApplyList.get(0);
        }
        return mlsqlApply != null ? message.addData("data",mlsqlApply) : message.error(404,"get apply failed");
    }


    @RequestMapping(value = "/table/get")
    @ApiOperation(value = "获取单个工坊表信息", httpMethod = "POST", notes = "该接口POST,GET两种请求都支持")
    @ApiImplicitParam(name = "tableName", value = "表名", required = true, paramType = "query", dataType = "String")
    public Message tableInfo(@RequestParam(value = "tableName", required = true) String tableName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tableName",tableName);
        MlsqlWorkshopTable mlsqlWorkshop = mlsqlWorkshopTableService.getMlsqlWorkshop(map);
        if (mlsqlWorkshop != null){
            map.put("jobName",mlsqlWorkshop.getJobName());
            map.put("status", MlsqlJobServiceImpl.SUCCESS);
            MlsqlJob mlsqlJob = mlsqlJobService.getMlsqlJob(map);
            if(mlsqlJob != null){
                map.put("tableId",mlsqlWorkshop.getId());
                map.put("status", MlsqlWorkshopTableServiceImpl.SUCCESS);
                String res = mlsqlWorkshopTableService.updateMlsqlWorkshop(map);
                MlsqlWorkshopTable mlsqlWorkshop1 = null;
                if (res.equals(InterfaceReturnInformation.SUCCESS)){
                    HashMap<String, Object> idMap = new HashMap<>();
                    idMap.put("tableId",mlsqlWorkshop.getId());
                    mlsqlWorkshop1 = mlsqlWorkshopTableService.getMlsqlWorkshop(idMap);
                }
                return mlsqlWorkshop1 != null ? message.addData("data",mlsqlWorkshop1) : message.error(500, "get workshop failed");
            }else {
                return mlsqlJob != null ? message.addData("data",mlsqlJob) : message.error(500,"get mlsqlJob failed");
            }
        }
        return message.error(404,"table " + tableName +  " is not found");
    }


    @RequestMapping(value = "/table/delete")
    @ApiOperation(value = "根据表名删除工坊列表信息", httpMethod = "POST", notes = "该接口POST,GET两种请求都支持")
    @ApiImplicitParam(name = "tableName", value = "表名", required = true, paramType = "query", dataType = "String")
    public Message delete(@RequestParam(value = "tableName", required = true) String tableName) {
        String result = mlsqlWorkshopTableService.mlsqlWorkshopDelete(tableName);
        return result.equals(InterfaceReturnInformation.SUCCESS) ? message.addData("data", "delete success") :
                message.addData("data", "delete failed");
    }


    @RequestMapping(value = "/tables/save")
    @ApiOperation(value = "保存工坊信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sql", value = "脚本信息", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "sessionId", value = "sessionId", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "scheam", value = "结构模式", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = true, paramType = "query", dataType = "String")
    })
    public Message save(@RequestParam(value = "tableName") String tableName,
                        @RequestParam(value = "sql") String sql,
                        @RequestParam(value = "sessionId", required = false) String sessionId,
                        @RequestParam(value = "schema") String schema,
                        @RequestParam(value = "status") Integer status,
                        @RequestParam(value = "jobName") String jobName) {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        MlsqlWorkshopTable mlsqlWorkshopTable = new MlsqlWorkshopTable();
        mlsqlWorkshopTable.setTableName(tableName);
        mlsqlWorkshopTable.setContent(sql);
        mlsqlWorkshopTable.setMlsqlUserId(1);
        mlsqlWorkshopTable.setSessionId(sessionId);
        mlsqlWorkshopTable.setStatus(status);
        mlsqlWorkshopTable.setTableSchema(schema);
        mlsqlWorkshopTable.setJobName(jobName);
        String msg = mlsqlWorkshopTableService.mlsqlWorkshopSave(mlsqlWorkshopTable);
        return msg.equals(InterfaceReturnInformation.SUCCESS) ? success(ReturnCode.RETURN_SUCCESS_STATUS, "save success") :
                error(ReturnCode.RETURN_ERROR_STATUS, "save failed");
    }


}
