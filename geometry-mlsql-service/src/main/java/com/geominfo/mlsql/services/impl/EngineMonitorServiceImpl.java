package com.geominfo.mlsql.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.EngineDetailsVO;
import com.geominfo.mlsql.job.EngineMonitorJob;
import com.geominfo.mlsql.services.*;
import com.geominfo.mlsql.utils.CronUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private IQuartzService quartzService;

    @Override
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

        String cron ;
        try {
            cron = CronUtil.AssembleCron(engineDetailsVO.getMonitorType(), engineDetailsVO.getDay(), engineDetailsVO.getHour());
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("flag",false);
            retMap.put("msg",e.getMessage());
            return retMap;
        }

        systemResourceService.insertTSystemResourceAutoIncrement(insertResource);

        quartzService.addJob(EngineMonitorJob.class, UUID.randomUUID().toString(),engineDetailsVO.getGroupName(),cron);

        retMap.put("flag",true);
        retMap.put("msg","添加成功");
        return retMap;
    }
}
