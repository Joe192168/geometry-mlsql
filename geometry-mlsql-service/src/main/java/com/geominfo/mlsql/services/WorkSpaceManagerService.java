package com.geominfo.mlsql.services;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.domain.po.WorkSpaceEngine;
import com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/8 17:45
 * @version: 1.0.0
 */
public interface WorkSpaceManagerService {

    /**
     * @description:新增工作空间信息
     * @author: LF
     * @date: 2021/6/10
     * @param workSpaceInfoVo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertWorkSpace(WorkSpaceInfoVo workSpaceInfoVo);

    /**
     * @description:修改工作空间信息
     * @author: LF
     * @date: 2021/6/10
     * @param workSpaceInfoVo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo updateWorkSpace(WorkSpaceInfoVo workSpaceInfoVo);

    /**
     * @description:删除工作空间
     * @author: LF
     * @date: 2021/6/10
     * @param id
     * @return java.lang.Boolean
     */
    Boolean deleteWorkSpace(BigDecimal id);

    /**
     * @description: 设置默认工作空间
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId, userId
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo setDefaultWorkSpace(BigDecimal spaceId,BigDecimal userId);

    /**
     * @description: 转让工作空间
     * @author: LF
     * @date: 2021/6/11
     * @param spaceId, userId
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo transferWorkSpace(BigDecimal spaceId,BigDecimal userId);

    /**
     * @description: 查询工作空间列表
     * @author: LF
     * @date: 2021/6/11
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo>
     */
    List<WorkSpaceInfoVo> getWorkSpaceLists(BigDecimal userId);

    /**
     * @description: 根据名称查询工作空间列表
     * @author: LF
     * @date: 2021/6/18
     * @param userId, spaceName
     * @return java.util.List<com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo>
     */
    List<WorkSpaceInfoVo> getWorkSpaceListsByName(BigDecimal userId,String spaceName);

    /**
     * @description: 删除工作引擎配置
     * @author: LF
     * @date: 2021/6/17
     * @param engineId, spaceId
     * @return void
     */
    void deleteEngine(BigDecimal engineId,BigDecimal spaceId);

    /**
     * @description: 给工作空间新增引擎配置
     * @author: LF
     * @date: 2021/6/17
     * @param workSpaceEngine
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertWorkSpaceEngine(WorkSpaceEngine workSpaceEngine);

    /**
     * @description: 设置默认引擎
     * @author: LF
     * @date: 2021/6/17
     * @param engineId, spaceId
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo setDefaultEngine(BigDecimal engineId,BigDecimal spaceId);

}
