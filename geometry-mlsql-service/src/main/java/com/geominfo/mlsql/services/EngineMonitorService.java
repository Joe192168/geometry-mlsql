package com.geominfo.mlsql.services;  /**
 * @title: EngineMonitorService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/6/2414:32
 */

import com.geominfo.mlsql.domain.po.EngineStatusMananer;
import com.geominfo.mlsql.domain.po.TEngineMonitorLog;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther zrd
 * @Date 2021-06-24 14:32 
 *
 */

public interface EngineMonitorService {

    Map<String,Object> saveEngineMonitor(EngineDetailsVO engineDetailsVO);

    Map<String,Object> restartMonitor(String jobName, String group);

    Map<String, Object> pauseJob(String jobName, String group);

    Map<String, Object> deleteJob(String jobName, String group,String resourceName);

    List<TSystemResources> listEngineMonitorJob(BigDecimal owner);

    List<TEngineMonitorLog> getMonitorLogByUserId();

    Boolean deleteMonitorLog(BigDecimal id);

    List<EngineStatusMananer> getLogByCondition(String engineName, String engineUrl, Long monitorStatus, Long dealType, Date monitorStartTime,Date monitorEndTime);
}
