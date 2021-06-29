package com.geominfo.mlsql.controller.engine;

import com.geominfo.mlsql.commons.Message;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;
import com.geominfo.mlsql.job.EngineMonitorJob;
import com.geominfo.mlsql.services.EngineMonitorService;
import com.geominfo.mlsql.services.IQuartzService;
import com.geominfo.mlsql.utils.CronUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * @title: EngineMonitorController
 * @date 2021/6/2411:17
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
    public Message saveMonitor(@RequestBody(required = false) EngineDetailsVO engineDetailsVO, @RequestParam String cron) {
        //Map<String, Object> map = engineMonitorService.saveEngineMonitor(engineDetailsVO);
        quartzService.addJob(EngineMonitorJob.class, UUID.randomUUID().toString(),"mlsqlEngine",cron);
        return null;
    }

    @ApiOperation(value = "重启引擎监控接口",httpMethod = "POST")
    @PostMapping("/reStartMonitor")
    public Message reStartMonitor(@RequestParam String jobName,@RequestParam String group) {
        quartzService.resumeJob(jobName,group);
        return null;
    }

    @ApiOperation(value = "暂停引擎监控接口",httpMethod = "POST")
    @PostMapping("/pauseMonitor")
    public Message pauseMonitor(@RequestParam String jobName,@RequestParam String group) {
        quartzService.pauseJob(jobName,group);
        return null;
    }

    @ApiOperation(value = "删除引擎监控接口",httpMethod = "POST")
    @PostMapping("/deleteMonitor")
    public Message deleteMonitor(@RequestParam String jobName,@RequestParam String group) {
        quartzService.deleteJob(jobName,group);
        return null;
    }

    public static void main(String[] args) throws Exception {
        String s = CronUtil.AssembleCron("hour", "2", 1);
        System.out.println(s);
    }

}
