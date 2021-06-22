package com.geominfo.mlsql.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.services.RecoverStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: RecoverStationServiceImpl
 * @date 2021/6/1511:03
 */
@Service
public class RecoverStationServiceImpl implements RecoverStationService {

    @Autowired
    private TSystemResourcesDao systemResourcesDao;

    @Override
    public List<TSystemResources> getAllRecoverResources(BigDecimal workSpaceId, BigDecimal userId) {
        List<TSystemResources> list =  systemResourcesDao.getAllRecoverResources(workSpaceId,userId);
        return list;
    }

    @Override
    public boolean deleteResource(BigDecimal resourceId) {
        int i = systemResourcesDao.deleteById(resourceId);
        return i > 0 ? true : false;
    }

    @Override
    public boolean recoverResource(BigDecimal resourceId) {
        TSystemResources tSystemResources = systemResourcesDao.selectById(resourceId);
        BigDecimal parentid = tSystemResources.getParentid();
        TSystemResources tSystemResources1 = systemResourcesDao.selectById(parentid);
        if (tSystemResources1 != null) {
            tSystemResources.setResourceState(new BigDecimal(0));
            tSystemResources.setUpdateTime(new Date());
            systemResourcesDao.updateById(tSystemResources);
            return true;
        }
        return false;
    }

    @Override
    public boolean reChoosePatentDir(BigDecimal resourceId, BigDecimal parentId) {
        TSystemResources tSystemResources = systemResourcesDao.selectById(resourceId);
        tSystemResources.setParentid(parentId);
        tSystemResources.setResourceState(new BigDecimal(0));
        tSystemResources.setUpdateTime(new Date());
        int i = systemResourcesDao.updateById(tSystemResources);
        return i > 0 ? true : false;
    }

    @Override
    public boolean deleteAll(BigDecimal workSpaceId,BigDecimal userId) {
        List<TSystemResources> allRecoverResources = systemResourcesDao.getAllRecoverResources(workSpaceId, userId);
        List<BigDecimal> ids = allRecoverResources.stream().map(systemResources -> systemResources.getId()).collect(Collectors.toList());
        int i = systemResourcesDao.deleteBatchIds(ids);
        return i > 0 ? true : false;
    }
}
