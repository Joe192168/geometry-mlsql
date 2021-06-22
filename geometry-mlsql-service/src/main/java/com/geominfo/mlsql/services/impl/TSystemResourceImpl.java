package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.systemidentification.SystemTableName;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.TSystemResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @title: TSystemResourceImpl
 * @date 2021/6/8 15:39
 */
@Service
public class TSystemResourceImpl implements TSystemResourceService {

    @Autowired
    private NumberControlService numberControlService;

    @Autowired
    private TSystemResourcesDao systemResourcesDao;
    @Override
    public Boolean insertTSystemResourceAutoIncrement(TSystemResources systemResources) {
        BigDecimal maxmum = numberControlService.getMaxNum(SystemTableName.T_SYSTEM_RESOURCES);
        systemResources.setId(maxmum);
        int insert = systemResourcesDao.insert(systemResources);
        return insert >= 1 ? true : false;
    }
}
