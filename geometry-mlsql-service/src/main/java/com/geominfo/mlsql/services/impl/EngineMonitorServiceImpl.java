package com.geominfo.mlsql.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.dao.QrtzTriggers;
import com.geominfo.mlsql.dao.TEngineConfigDao;
import com.geominfo.mlsql.dao.TEngineMonitorLogDao;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.EngineStatusMananer;
import com.geominfo.mlsql.domain.po.TEngineConfig;
import com.geominfo.mlsql.domain.po.TEngineMonitorLog;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;
import com.geominfo.mlsql.job.EngineMonitorJob;
import com.geominfo.mlsql.services.*;
import com.geominfo.mlsql.utils.CronUtil;
import com.jcraft.jsch.HASH;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @title: EngineMonitorServiceImpl
 * @date 2021/6/2414:33
 */
@Service
public class EngineMonitorServiceImpl implements EngineMonitorService {
    @Autowired
    private TSystemResourcesDao systemResourcesDao;
    @Autowired
    private TSystemResourceService systemResourceService;
    @Autowired
    private QrtzTriggers qrtzTriggers;
    @Autowired
    private TEngineConfigDao engineConfigDao;
    @Autowired
    private TEngineMonitorLogDao engineMonitorLogDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String, Object> saveEngineMonitor(EngineDetailsVO engineDetailsVO) {
        HashMap<String, Object> retMap = new HashMap<>();

        TSystemResources systemResources = systemResourcesDao.selectOne(new QueryWrapper<TSystemResources>().eq("resource_name", engineDetailsVO.getEngineUrl()));
        if (systemResources != null) {
            retMap.put("flag",false);
            retMap.put("msg","该引擎的监控已存在");
            return retMap;
        }
        TSystemResources insertResource = new TSystemResources();
        insertResource.setResourceName(engineDetailsVO.getEngineUrl());
        insertResource.setCreateTime(new Date());
        insertResource.setUpdateTime(new Date());
        insertResource.setResourceTypeId(new BigDecimal(ResourceTypeConstants.YQJK));
        insertResource.setOwner(engineDetailsVO.getOwner());
        insertResource.setContentInfo(JSONObject.toJSONString(engineDetailsVO));
        insertResource.setResourceState(new BigDecimal(engineDetailsVO.getOpen()));

        //资源表添加记录
        systemResourceService.insertTSystemResourceAutoIncrement(insertResource);
        //引擎配置表添加记录
        TEngineConfig tEngineConfig = new TEngineConfig();
        tEngineConfig.setEngineId(engineDetailsVO.getEngineId());
        tEngineConfig.setCreateTime(new Date());
        tEngineConfig.setUpdateTime(new Date());
        tEngineConfig.setDealType(engineDetailsVO.getExceptionSolution());
        tEngineConfig.setIsEnable(engineDetailsVO.getOpen());
        tEngineConfig.setOperatorId(engineDetailsVO.getUserId());
        tEngineConfig.setInterval(engineDetailsVO.getHour() * 60L);
        if (engineDetailsVO.getExceptionSolution().equals(1L)) {
            tEngineConfig.setExtraOpts(JSONObject.toJSONString(engineDetailsVO.getRestartConfig()));
        }
        engineConfigDao.insert(tEngineConfig);
        engineDetailsVO.setConfigId(tEngineConfig.getId());

        retMap.put("flag",true);
        retMap.put("msg","添加成功");
        return retMap;
    }

    @Override
    public Map<String,Object> restartMonitor(String jobName, String group) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("flag",false);
        com.geominfo.mlsql.domain.po.QrtzTriggers qrtzTriggers = this.qrtzTriggers.selectOne(new QueryWrapper<com.geominfo.mlsql.domain.po.QrtzTriggers>().eq("trigger_name", jobName).eq("trigger_group", group));
        if (qrtzTriggers == null) {
            retMap.put("flag",false);
            retMap.put("msg","job不存在，请检查jobName或group是否正确");
            return retMap;
        }
        if ( qrtzTriggers.getTriggerState().equalsIgnoreCase("acquired") || qrtzTriggers.getTriggerState().equalsIgnoreCase("waiting")){
            retMap.put("flag",true);
            return retMap;
        }
        return retMap;
    }

    @Override
    public Map<String, Object> pauseJob(String jobName, String group) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("flag",false);
        com.geominfo.mlsql.domain.po.QrtzTriggers qrtzTriggers = this.qrtzTriggers.selectOne(new QueryWrapper<com.geominfo.mlsql.domain.po.QrtzTriggers>().eq("trigger_name", jobName).eq("trigger_group", group));
        if (qrtzTriggers == null) {
            retMap.put("flag",false);
            retMap.put("msg","job不存在，请检查jobName或group是否正确");
            return retMap;
        }
        if (qrtzTriggers.getTriggerState().equalsIgnoreCase("pause")){
            retMap.put("flag",true);
            return retMap;
        }
        return retMap;
    }

    @Override
    public Map<String, Object> deleteJob(String jobName, String group,String resourceName) {
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("flag",false);
        com.geominfo.mlsql.domain.po.QrtzTriggers qrtzTriggers = this.qrtzTriggers.selectOne(new QueryWrapper<com.geominfo.mlsql.domain.po.QrtzTriggers>().eq("trigger_name", jobName).eq("trigger_group", group));
        HashMap<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("resource_name",resourceName);
        deleteMap.put("resource_type_id",ResourceTypeConstants.YQJK);
        systemResourcesDao.deleteByMap(deleteMap);

        //监控配置删除
        engineConfigDao.selectByMap()
        if (qrtzTriggers == null) {
            retMap.put("flag", true);
            retMap.put("msg","删除成功");
            return retMap;
        }
        return retMap;
    }

    @Override
    public List<TSystemResources> listEngineMonitorJob(BigDecimal owner) {
        List<TSystemResources> systemResourcesList = systemResourcesDao.selectList(new QueryWrapper<TSystemResources>().eq("owner", owner).eq("resource_type_id", ResourceTypeConstants.YQJK));
        return systemResourcesList;
    }

    @Override
    public List<TEngineMonitorLog> getMonitorLogByUserId() {
        List<TEngineMonitorLog> monitorLogs = engineMonitorLogDao.selectList(new QueryWrapper<>());
        return monitorLogs;
    }

    @Override
    public Boolean deleteMonitorLog(BigDecimal id) {
        int i = engineMonitorLogDao.deleteById(id);
        return i > 0 ? true : false;
    }

    @Override
    public List<EngineStatusMananer> getLogByCondition(String engineName, String engineUrl, Long monitorStatus, Long dealType, Date monitorStartTime,Date monitorEndTime) {
        List<EngineStatusMananer> logsList = engineConfigDao.getLogByCondition(engineName, engineUrl, monitorStatus, dealType, monitorStartTime,monitorEndTime);
        return logsList;
    }
}
