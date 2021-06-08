package com.geominfo.mlsql.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.systemidentification.SystemTableName;
import com.geominfo.mlsql.services.NumberControlService;
import com.geominfo.mlsql.services.ScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @title: ScriptServiceImpl
 * @date 2021/6/214:30
 */
@Service
public class ScriptServiceImpl implements ScriptService {

    @Autowired
    private TSystemResourcesDao systemResourcesDao;
    @Autowired
    private NumberControlService numberControlService;

    @Override
    public boolean mkdir(BigDecimal parentId,BigDecimal owner,String dirName) {
        TSystemResources resources = systemResourcesDao.selectOne(new QueryWrapper<TSystemResources>().eq("parentid", parentId).eq("resource_name", dirName).eq("resource_type_id", ResourceTypeConstants.FOLDER));
        if (resources == null) {
            TSystemResources tSystemResources = new TSystemResources();
            tSystemResources.setResourceTypeId(new BigDecimal(ResourceTypeConstants.FOLDER));
            tSystemResources.setId(numberControlService.getMaxNum(SystemTableName.T_SYSTEM_RESOURCES));
            tSystemResources.setParentid(parentId);
            tSystemResources.setOwner(owner);
            tSystemResources.setResourceName(dirName);
            tSystemResources.setCreateTime(new Date());
            int insert = systemResourcesDao.insert(tSystemResources);
            if (insert > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteDir(BigDecimal resourceId) {
        List<TSystemResources> parentid = systemResourcesDao.selectList(new QueryWrapper<TSystemResources>().eq("parentid", resourceId));
        if (parentid.isEmpty()) {
            int i = systemResourcesDao.deleteById(resourceId);
            if (i > 0 ) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean modifyDirName(String name,BigDecimal resourceId,BigDecimal parentId) {

        TSystemResources tSystemResources1 = systemResourcesDao.selectOne(new QueryWrapper<TSystemResources>().eq("parentid", parentId).eq("resource_name", name));
        if (tSystemResources1 == null) {
            TSystemResources tSystemResources = new TSystemResources();
            tSystemResources.setId(resourceId);
            tSystemResources.setResourceName(name);
            tSystemResources.setUpdateTime(new Date());
            int i = systemResourcesDao.updateById(tSystemResources);
            if (i > 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteScript(BigDecimal resourceId) {
        TSystemResources tSystemResources = new TSystemResources();
        tSystemResources.setId(resourceId);
        tSystemResources.setResourceState(new BigDecimal(1));
        tSystemResources.setUpdateTime(new Date());
        int i = systemResourcesDao.updateById(tSystemResources);
        if (i > 0) {
            return true;
        }
        return false;
    }
}
