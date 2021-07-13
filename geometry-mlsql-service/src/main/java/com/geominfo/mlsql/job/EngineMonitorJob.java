package com.geominfo.mlsql.job;

import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.dao.TEngineConfigDao;
import com.geominfo.mlsql.dao.TEngineMonitorLogDao;
import com.geominfo.mlsql.domain.po.TEngineMonitorLog;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;
import com.geominfo.mlsql.domain.vo.JobParameter;
import com.geominfo.mlsql.services.MlsqlService;
import com.geominfo.mlsql.utils.ExecuteShellUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.util.Date;

/**
 * @title: EngineMonitorJob
 * @date 2021/6/2511:31
 */
@Component
@SuppressWarnings("all")
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class EngineMonitorJob extends QuartzJobBean {

    @Autowired
    private MlsqlService mlsqlService;

    @Autowired
    private TEngineConfigDao engineConfigDao;

    @Autowired
    private TEngineMonitorLogDao engineMonitorLogDao;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        JobParameter jobParam = (JobParameter) dataMap.get(JobParameter.JOB_PARAM);
        EngineDetailsVO engineDetailsVO = jobParam.getEngineDetailsVO();
        JSONObject engineState = null;
        try {
            engineState = mlsqlService.getEngineState(engineDetailsVO.getEngineUrl());
        }catch (ResourceAccessException e) {
            e.printStackTrace();
            TEngineMonitorLog tEngineMonitorLog = new TEngineMonitorLog();
            tEngineMonitorLog.setConfigId(engineDetailsVO.getConfigId());
            tEngineMonitorLog.setMonitorTime(new Date());
            tEngineMonitorLog.setMonitorStatus(3);
            engineMonitorLogDao.insert(tEngineMonitorLog);

            //调用重启引擎接口
            if (engineDetailsVO.getOpen().equals(1) && engineDetailsVO.getExceptionSolution().equals(1)) {
                EngineDetailsVO.RestartConfig restartConfig = engineDetailsVO.getRestartConfig();
                executeShellScript(engineDetailsVO.getEngineUrl(),
                        Integer.parseInt(engineDetailsVO.getPort()),
                        restartConfig.getUsername(),restartConfig.getPassword(),restartConfig.getScriptRoute());
            }
            return;
        }
        insertEngineMonitorLog(engineState,engineDetailsVO);
    }

    public void insertEngineMonitorLog(JSONObject json,EngineDetailsVO engineDetailsVO){
        String status = json.get("status").toString();
        TEngineMonitorLog tEngineMonitorLog = new TEngineMonitorLog();
        tEngineMonitorLog.setConfigId(engineDetailsVO.getConfigId());
        tEngineMonitorLog.setMonitorTime(new Date());
        if (status.equals("UP")) {
            tEngineMonitorLog.setMonitorStatus(2);
        }else if (status.equals("DOWN")) {
            tEngineMonitorLog.setMonitorStatus(1);
        }else {
            tEngineMonitorLog.setMonitorStatus(3);
        }
        engineMonitorLogDao.insert(tEngineMonitorLog);
    }

    public void executeShellScript(String ip, Integer port, String username, String password, String shellCmd) {
        try {
            ExecuteShellUtil instance = ExecuteShellUtil.getInstance();
            instance.init(ip,port,username,password);
            instance.execCmd(shellCmd);
            log.info("ip：{} 重启成功",ip+port);
        } catch (Exception e) {
            log.error("执行重启引擎脚本失败:{}",e.getMessage());
        }
    }
}
