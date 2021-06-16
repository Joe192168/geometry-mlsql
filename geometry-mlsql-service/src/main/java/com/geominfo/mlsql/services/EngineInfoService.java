package com.geominfo.mlsql.services;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.domain.po.EngineInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/16 11:11
 * @version: 1.0.0
 */
public interface EngineInfoService {

    /**
     * @description: 新增引擎管理
     * @author: LF
     * @date: 2021/6/16
     * @param engineInfo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertEngineInfo(EngineInfo engineInfo);

    /**
     * @description: 编辑引擎管理
     * @author: LF
     * @date: 2021/6/16
     * @param engineInfo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo updateEngineInfo(EngineInfo engineInfo);

    /**
     * @description: 删除引擎管理
     * @author: LF
     * @date: 2021/6/16
     * @param id
     * @return java.lang.Boolean
     */
    Boolean deleteEngineInfo(BigDecimal id);

    /***
     * @description: 校验引擎是否存在
     * @author: LF
     * @date: 2021/6/16
     * @param engineInfo
     * @return java.lang.Boolean
     */
    Boolean checkEngineName(EngineInfo engineInfo);

    /**
     * @description: 获取引擎列表
     * @author: LF
     * @date: 2021/6/16
     * @param
     * @return java.util.List<com.geominfo.mlsql.domain.po.EngineInfo>
     */
    List<EngineInfo> getEngineInfos();
}
