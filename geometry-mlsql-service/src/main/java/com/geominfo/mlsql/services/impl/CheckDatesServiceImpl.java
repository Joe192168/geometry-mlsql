package com.geominfo.mlsql.services.impl;

import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.CheckParamVo;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import com.geominfo.mlsql.services.CheckDatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
public class CheckDatesServiceImpl implements CheckDatesService {
    @Autowired
    private TSystemResourcesDao systemResourcesDao;

    @Override
    public Boolean CheckResourceDate(TSystemResources resourceVo) {
        CheckParamVo paramVo = new CheckParamVo();
        paramVo.setParamName(resourceVo.getResourceName());
        paramVo.setResourceType(resourceVo.getResourceTypeId().intValue());
        paramVo.setParentId(resourceVo.getParentid().intValue());
        List<SystemResourceVo> resourceVoList = systemResourcesDao.checkSystemParamName(paramVo);
        if (paramVo.getId() == null) {
            if (CollectionUtils.isEmpty(resourceVoList))
                return true;
            else
                return false;
        } else {
            paramVo.setId(resourceVo.getId());
            if (!CollectionUtils.isEmpty(resourceVoList) && resourceVoList.size() > 0) {
                TSystemResources resources = systemResourcesDao.selectById(paramVo.getId());
                if (resources.getResourceName().equals(resourceVoList.get(0).getResourceName())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

}
