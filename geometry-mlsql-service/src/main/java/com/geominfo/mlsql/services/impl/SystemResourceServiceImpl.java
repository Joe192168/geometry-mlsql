package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.SystemTableConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.CheckDatesService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.SystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SystemResourceServiceImpl implements SystemResourceService {

    @Autowired
    private CheckDatesService checkDatesService;
    @Autowired
    private TSystemResourcesDao systemResourcesDao;
    @Autowired
    private NumberControlService numberControlService;


    @Override
    public BaseResultVo insertResource(TSystemResources resourceVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        try {
            if (!checkDatesService.CheckResourceDate(resourceVo)) {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(CommonConstants.NAME_EXIT);
                return baseResultVo;
            }
            BigDecimal id = numberControlService.getMaxNum(SystemTableConstants.T_SYSTEM_RESOURCES);
            resourceVo.setId(id);
            resourceVo.setCreateTime(new Date());
            resourceVo.setUpdateTime(new Date());
            systemResourcesDao.insert(resourceVo);
            baseResultVo.setId(id);
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_SUCCESS.getMsg());
            return baseResultVo;
        } catch (Exception e) {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_ERROR.getMsg());
            return baseResultVo;
        }

    }

    @Override
    public BaseResultVo updateResource(TSystemResources resourceVo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        try {
            if (!checkDatesService.CheckResourceDate(resourceVo)) {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(CommonConstants.NAME_EXIT);
                return baseResultVo;
            }
            resourceVo.setUpdateTime(new Date());
            systemResourcesDao.insert(resourceVo);
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.UPDATE_SUCCESS.getMsg());
            return baseResultVo;
        } catch (Exception e) {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.UPDATE_ERROR.getMsg());
            return baseResultVo;
        }

    }

    @Override
    public Boolean deleteResourceById(BigDecimal id) {
        TSystemResources systemResources = systemResourcesDao.selectById(id);
        if (systemResources !=null ){
            int i = systemResourcesDao.deleteById(id);
            if (i > 0)
                return true;
            else
                return false;
        }else {
            return false;
        }

    }

    @Override
    public List<TSystemResources> getRecentlyScripts(BigDecimal userId) {
        return systemResourcesDao.getRecentlyScripts(userId);
    }

    @Override
    public TSystemResources getResourceById(BigDecimal id) {
        return systemResourcesDao.selectById(id);
    }

}
