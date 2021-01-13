package com.geominfo.mlsql.controller.job;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.Message;
import com.geominfo.mlsql.domain.vo.MlsqlJob;
import com.geominfo.mlsql.domain.vo.MlsqlJobRender;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.cluster.ScriptLogService;
import com.geominfo.mlsql.service.job.MlsqlJobService;
import com.geominfo.mlsql.service.job.impl.MlsqlJobServiceImpl;
import com.geominfo.mlsql.service.user.UserService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.internal.org.objectweb.asm.Opcodes;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.asm.Advice;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @program: geometry-mlsql
 * @description: 执行任务信息控制类
 * @author: ryan(丁帅波)
 * @create: 2020-11-10 09:45
 * @version: 1.0.0
 */

@RestController
@RequestMapping("/api_v1/job")
@Api(value = "任务信息接口", tags = {"任务信息接口"})
@Log4j2
public class MlsqlJobController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MlsqlJobService mlsqlJobService;

    @Autowired
    private ScriptLogService scriptLogService ;


    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ApiOperation(value = "异步执行", httpMethod = "POST", notes = "post请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "__auth_secret", value = "命令", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "params", value = "engine执行成功或者失败信息", required = true, paramType = "query", dataType = "Map")
    })
    public Message jobCallBack(
            @RequestParam(value = "", required = true) Map<String, String> params) throws ExecutionException, InterruptedException {
        String aaa = "";
         /*CommandUtil.auth_secret()*/
        if (!params.get("__auth_secret__").equals("mlsql")) {
            return error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"requirement failed: __auth_secret__ is not right");
        } else {
            String jobName = JSONObject.parseObject(params.get("jobInfo")).getString("jobName");
            Map<String, Object> map;
            String msg = "";
            if (params.get("stat").equals("succeeded")) {
                //创建map
                Object o = params.get("res") ;
                log.info("回调接口返回的数据 res =" +o);
                map = mlsqlJobService.createMap(0, jobName, MlsqlJobServiceImpl.SUCCESS,
                        System.currentTimeMillis(), " ", o.toString());
                msg = mlsqlJobService.updateMlsqlJob(map);

                //保存相应Log
                scriptLogService.addLog(o.toString());

            } else {
                //创建map
                log.info("回调接口返回的数据 msg =" +params.get("msg"));
                map = mlsqlJobService.createMap(0, jobName, MlsqlJobServiceImpl.FAIL,
                        System.currentTimeMillis(), params.get("msg"), " ");
                msg = mlsqlJobService.updateMlsqlJob(map);

            }
            return msg.equals(InterfaceReturnInformation.SUCCESS) ? success(ReturnCode.RETURN_SUCCESS_STATUS, "update failure success") :
                    error(ReturnCode.RETURN_ERROR_STATUS, "update failure failed");
        }
    }


    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "获取用户最近前100条历史任务信息列表", notes = "该方法同时支持POST,GET两种请求方式")
    public Message jobList() {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        List<MlsqlJobRender> mlsqlJobList = mlsqlJobService.getMlsqlJobList(mlsqlUser.getId());
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"get success").addData("data", mlsqlJobList);
    }


    @RequestMapping(value = "/getJob", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "查询单个任务信息", notes = "该方法同时支持POST,GET两种请求方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = true, paramType = "query", dataType = "String")
    })
    public Message getJob(
            @RequestParam(value = "jobName", required = true) String jobName) {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("jobName", jobName);
        MlsqlJob mlsqlJob = mlsqlJobService.getMlsqlJob(map);
        if (mlsqlJob == null) {
            return error(ReturnCode.RETURN_ERROR_STATUS, "The task name does not exist");
        } else {
            return success(ReturnCode.RETURN_SUCCESS_STATUS,"get success").addData("data", mlsqlJob);
        }
    }


    @RequestMapping(value = "/kill", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiOperation(value = "杀死进程后修改状态", notes = "该方法同时支持POST,GET两种请求方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "任务名称", required = true, paramType = "query", dataType = "String")
    })
    public Message killJob(@RequestParam(value = "jobName", required = true) String jobName) {
        MlsqlUser mlsqlUser = userService.getUserByName(userName);
        //创建map
        Map<String, Object> map = mlsqlJobService.createMap(mlsqlUser.getId(), jobName, MlsqlJobServiceImpl.KILLED,
                System.currentTimeMillis(), " ", " ");
        String msg = mlsqlJobService.updateMlsqlJob(map);
        return msg.equals(InterfaceReturnInformation.SUCCESS) ? success(ReturnCode.RETURN_SUCCESS_STATUS, "kill success") :
                error(ReturnCode.RETURN_ERROR_STATUS, "kill failed");
    }
}
