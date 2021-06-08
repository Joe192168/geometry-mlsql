package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.systemidentification.SystemTableName;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.CheckDatesService;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.SystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/8 11:08
 * @version: 1.0.0
 */
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
                baseResultVo.setReturnMsg(InterfaceMsg.RESOURCE_NAME_EXIT.getMsg());
                return baseResultVo;
            }
            BigDecimal id = numberControlService.getMaxNum(SystemTableName.T_SYSTEM_RESOURCES);
            resourceVo.setId(id);
            resourceVo.setCreateTime(new Date());
            resourceVo.setUpdateTime(new Date());
            systemResourcesDao.insert(resourceVo);
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
                baseResultVo.setReturnMsg(InterfaceMsg.RESOURCE_NAME_EXIT.getMsg());
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
    public Boolean deleteResourceById(Integer id) {
        int i = systemResourcesDao.deleteById(id);
        if (i > 0)
            return true;
        else
            return false;
    }
}
