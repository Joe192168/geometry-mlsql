package com.geominfo.mlsql.controller;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.constant.ExceptionMsgConstant;
import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.domain.vo.MlsqlJobsVO;
import com.geominfo.mlsql.services.MlsqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @title: MlsqlController
 * @date 2021/4/614:40
 */

@RestController
@RequestMapping("/mlsql")
@Slf4j
public class MlsqlController {

    @Autowired
    private MlsqlService mlsqlService;


    @PostMapping("/executeSql")
    public Message executeSql(@RequestBody( required = false) MlsqlExecuteSqlVO mlsqlExecuteSqlVO){
        try {
            String result = mlsqlService.executeMlsql(mlsqlExecuteSqlVO);
            return new Message().ok().addData("data",result);
        } catch (Exception e) {
            log.error("executeSql : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    @GetMapping("/getAlljobs")
    public Message getRunningJobs(){
        try {
            JSONObject allExecuteJobs = mlsqlService.getAllExecuteJobs();
            return new Message().ok().addData("data",allExecuteJobs);
        } catch (Exception e) {
            log.error("getRunningJobs : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    @GetMapping("/getEngineState")
    public Message getEngineState(){
        try {
            JSONObject allExecuteJobs = mlsqlService.getEngineState();
            return new Message().ok().addData("data",allExecuteJobs);
        } catch (Exception e) {
            log.error("getEngineState : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    @PostMapping("/killJob")
    public Message killJob(@RequestParam(required = false) String jobName,@RequestParam(required = false) String groupId){
        try {
            String result = mlsqlService.killMlsqlJob(jobName, groupId);
            return new Message().ok().addData("data",result);
        } catch (Exception e) {
            log.error("getEngineState : {}",e.getMessage());
            return new Message().error(ExceptionMsgConstant.MLSQL_NOT_RESPONSE);
        }
    }

    /***
     * @Description: 异步回调接口
     * @Author: zrd
     * @Date: 2021/5/6 14:16
     * @param
     * @param
     * @return com.geominfo.mlsql.commons.Message
     */
    @PostMapping("/asyncCallback")
    public void asyncCallback(@RequestParam("jobInfo") String jobInfo){
        MlsqlJobsVO mlsqlJobsVO = JSONObject.parseObject(jobInfo, MlsqlJobsVO.class);
        System.out.println(mlsqlJobsVO.toString());
        System.out.println(jobInfo);
    }
}
