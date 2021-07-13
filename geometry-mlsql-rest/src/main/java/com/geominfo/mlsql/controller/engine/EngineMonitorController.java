package com.geominfo.mlsql.controller.engine;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.po.EngineStatusMananer;
import com.geominfo.mlsql.domain.po.TEngineMonitorLog;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;
import com.geominfo.mlsql.domain.vo.JobParameter;
import com.geominfo.mlsql.job.EngineMonitorJob;
import com.geominfo.mlsql.services.EngineMonitorService;
import com.geominfo.mlsql.services.IQuartzService;
import com.geominfo.mlsql.utils.CronUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.ws.rs.POST;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @title: EngineMonitorController
 * @date 2021/6/24 11:17
 */

@Api(tags = {"引擎监控接口"})
@RestController
@RequestMapping("/engineMonitor")
public class EngineMonitorController {

    @Autowired
    private EngineMonitorService engineMonitorService;
    @Autowired
    private IQuartzService quartzService;


    @ApiOperation(value = "添加引擎监控接口",httpMethod = "POST")
    @PostMapping("/saveMonitor")
    public Message saveMonitor(@RequestBody(required = false) EngineDetailsVO engineDetailsVO) {
        String cron = "";
        if (StringUtils.isEmpty(engineDetailsVO.getCron())) {
            try {
                cron = CronUtil.AssembleCron(engineDetailsVO.getMonitorType(), engineDetailsVO.getDay(), engineDetailsVO.getHour());
            } catch (Exception e) {
                return new Message().error(e.getMessage());
            }
        }else {
            cron = engineDetailsVO.getCron();
        }
        Map<String, Object> map = engineMonitorService.saveEngineMonitor(engineDetailsVO);
        if ((Boolean) map.get("flag")) {
            JobParameter jobParameter = new JobParameter();
            jobParameter.setEngineDetailsVO(engineDetailsVO);
            quartzService.addJob(EngineMonitorJob.class, UUID.randomUUID().toString(),engineDetailsVO.getGroupName(),cron,jobParameter);
            return new Message().ok(map.get("msg").toString());
        }

        return new Message().error("监控添加失败");
    }

    @ApiOperation(value = "重启引擎监控接口",httpMethod = "POST")
    @PostMapping("/restartMonitor")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",value = "任务名",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "group",value = "分组名",type = "String",paramType = "param")
            }
    )
    public Message restartMonitor(@RequestParam String jobName,@RequestParam String group) {
        quartzService.resumeJob(jobName,group);
        Map<String, Object> map = engineMonitorService.restartMonitor(jobName, group);
        if ((Boolean) map.get("flag")) {
            return new Message().ok("重启成功");
        }
        return new Message().error(map.get("msg").toString());
    }

    @ApiOperation(value = "暂停引擎监控接口",httpMethod = "POST")
    @PostMapping("/pauseMonitor")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",value = "任务名",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "group",value = "分组名",type = "String",paramType = "param")
    }
    )
    public Message pauseMonitor(@RequestParam String jobName,@RequestParam String group) {
        quartzService.pauseJob(jobName,group);
        Map<String, Object> map = engineMonitorService.pauseJob(jobName, group);
        if ((Boolean) map.get("flag")) {
            return new Message().ok("暂停成功");
        }
        return new Message().error(map.get("msg").toString());
    }

    @ApiOperation(value = "删除引擎监控接口",httpMethod = "DELETE")
    @DeleteMapping("/deleteMonitor")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName",value = "任务名",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "group",value = "分组名",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "resourceName",value = "资源名",type = "String",paramType = "param")
    }
    )
    public Message deleteMonitor(@RequestParam String jobName,@RequestParam String group,@RequestParam String resourceName) {
        quartzService.deleteJob(jobName,group);
        Map<String, Object> map = engineMonitorService.deleteJob(jobName, group,resourceName);
        if ((Boolean) map.get("flag")) {

            return new Message().ok("删除成功");
        }
        return new Message().error(map.get("msg").toString());
    }

    @ApiOperation(value = "获取引擎的所有监控",httpMethod = "GET")
    @GetMapping("/listEngineMonitorJob/{owner}")
    @ApiImplicitParam(name = "owner",value = "用户id",type = "BigDecimal",paramType = "path")
    public Message listEngineMonitorJob(@PathVariable BigDecimal owner) {
        List<TSystemResources> systemResourcesList = engineMonitorService.listEngineMonitorJob(owner);
        return new Message().ok().addData("data",systemResourcesList);
    }

    @ApiOperation(value = "获取所有监控日志",httpMethod = "GET")
    @GetMapping("/getAllMonitorLog")
    public Message getAllMonitorLog() {
        List<TEngineMonitorLog> monitorLogs = engineMonitorService.getMonitorLogByUserId();
        return new Message().ok().addData("data",monitorLogs);
    }

    @ApiOperation(value = "删除日志",httpMethod = "DELETE")
    @DeleteMapping("/deleteMonitorLog/{id}")
    @ApiImplicitParam(name = "资源id",value = "资源名",type = "BigDecimal",paramType = "path")
    public Message deleteMonitorLog(@PathVariable BigDecimal id) {
        Boolean delete = engineMonitorService.deleteMonitorLog(id);
        if (delete) {
            return new Message().ok("删除成功");
        }
        return new Message().error("删除失败");
    }

    @ApiOperation(value = "根据条件查询日志",httpMethod = "POST")
    @PostMapping("/getLogByCondition")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "engineName",value = "引擎名",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "engineUrl",value = "引擎url",type = "String",paramType = "param"),
            @ApiImplicitParam(name = "monitorStatus",value = "监控状态",type = "int",paramType = "param"),
            @ApiImplicitParam(name = "dealType",value = "处理类型",type = "int",paramType = "param"),
            @ApiImplicitParam(name = "monitorStartTime",value = "监控开始时间",type = "Date",paramType = "param"),
            @ApiImplicitParam(name = "monitorEndTime",value = "监控结束时间",type = "Date",paramType = "param"),
    })
    public Message getLogByCondition(@RequestParam(required = false) String engineName,@RequestParam(required = false) String engineUrl,
                                     @RequestParam(required = false) Long monitorStatus,@RequestParam(required = false) Long dealType,
                                     @RequestParam(required = false) Date monitorStartTime,@RequestParam(required = false) Date monitorEndTime) {
        List<EngineStatusMananer> logsList = engineMonitorService.getLogByCondition(engineName,engineUrl,monitorStatus,dealType,monitorStartTime,monitorEndTime);
        return new Message().ok().addData("data",logsList);
    }

}
