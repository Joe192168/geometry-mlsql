package com.geominfo.mlsql.services.impl;

import com.geominfo.authing.common.constants.CommonConstants;
import com.geominfo.authing.common.constants.SystemTableConstants;
import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.dao.EngineInfoMapper;
import com.geominfo.mlsql.domain.po.EngineInfo;
import com.geominfo.mlsql.enums.InterfaceMsg;
import com.geominfo.mlsql.services.EngineInfoService;
import com.geominfo.mlsql.services.NumberControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/16 11:22
 * @version: 1.0.0
 */
@Service
public class EngineInfoServiceImpl implements EngineInfoService {
    @Autowired
    private NumberControlService numberControlService;
    @Autowired
    private EngineInfoMapper engineInfoMapper;

    @Override
    public BaseResultVo insertEngineInfo(EngineInfo engineInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        if (checkEngineName(engineInfo)){
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.NAME_EXIT);
            return baseResultVo;
        }
        BigDecimal id = numberControlService.getMaxNum(SystemTableConstants.T_ENGINE_INFO);
        engineInfo.setId(id);
        engineInfo.setCreateTime(new Date());
        engineInfo.setUpdateTime(new Date());
        int i = engineInfoMapper.insert(engineInfo);
        if (i > 0) {
            baseResultVo.setSuccess(Boolean.TRUE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_ENGINE_SUCCESS.getMsg());
            baseResultVo.setId(id);
            return baseResultVo;
        } else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(InterfaceMsg.INSERT_ENGINE_FAIL.getMsg());
            return baseResultVo;
        }
    }

    @Override
    public BaseResultVo updateEngineInfo(EngineInfo engineInfo) {
        BaseResultVo baseResultVo = new BaseResultVo();
        if (checkEngineName(engineInfo)){
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.NAME_EXIT);
            return baseResultVo;
        }

        if (engineInfo.getId() != null) {
            EngineInfo engineInfo1 = engineInfoMapper.selectById(engineInfo.getId());
            if (engineInfo1 != null) {
                engineInfoMapper.updateById(engineInfo);
                baseResultVo.setSuccess(Boolean.TRUE);
                baseResultVo.setReturnMsg(InterfaceMsg.UPDATE_ENGINE_SUCCESS.getMsg());
                return baseResultVo;
            } else {
                baseResultVo.setSuccess(Boolean.FALSE);
                baseResultVo.setReturnMsg(InterfaceMsg.UPDATE_ENGINE_FAIL.getMsg());
                return baseResultVo;
            }
        } else {
            baseResultVo.setSuccess(Boolean.FALSE);
            baseResultVo.setReturnMsg(CommonConstants.PARAM_ERROR);
            return baseResultVo;
        }
    }

    @Override
    public Boolean deleteEngineInfo(BigDecimal id) {
        EngineInfo engineInfo = engineInfoMapper.selectById(id);
        if (engineInfo != null) {
            engineInfoMapper.deleteEngine(id);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Boolean checkEngineName(EngineInfo engineInfo) {
        List<EngineInfo> engineInfos = engineInfoMapper.getEngineInfoByEngineUri(engineInfo.getEngineUri());
        if (engineInfo.getId() == null) {
            if (CollectionUtils.isEmpty(engineInfos)){
                return false;
            }else {
                return true;
            }
        }else {
            EngineInfo engineInfo1 = engineInfoMapper.selectById(engineInfo.getId());
            if (engineInfo1 != null && !CollectionUtils.isEmpty(engineInfos)){
                if (engineInfo1.getEngineName().equals(engineInfos.get(0).getEngineName()) && engineInfo1.getEngineUri().equals(engineInfos.get(0).getEngineUri())){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }

        }

    }

    @Override
    public List<EngineInfo> getEngineInfos() {
        return engineInfoMapper.getEngineInfos();
    }

    @Override
    public List<EngineInfo> getEnginesBySpaceId(BigDecimal spaceId) {
        return engineInfoMapper.getEnginesBySpaceId(spaceId);
    }

    @Override
    public List<EngineInfo> getEngineLists(BigDecimal spaceId) {
        return engineInfoMapper.getEngineLists(spaceId);
    }
}
