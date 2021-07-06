package com.geominfo.mlsql.dao;  /**
 * @title: TSystemResourcesDao
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/5/1214:26
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.result.WorkSpaceInfoResult;
import com.geominfo.mlsql.domain.vo.CheckParamVo;
import com.geominfo.mlsql.domain.vo.SystemResourceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther zrd
 * @Date 2021-05-12 14:26 
 *
 */
@Mapper
public interface TSystemResourcesDao extends BaseMapper<TSystemResources> {

    /***
     * @Description: 根据父节点ID获取该父节点下所有资源
     * @Author: zrd
     * @Date: 2021/5/12 14:48
     * @param parentId 父节点id
     * @return java.util.List<com.geominfo.mlsql.domain.po.TSystemResources>
     */
    List<TSystemResources> listByParentId(@Param("parentId") BigDecimal parentId);

    /***
     * @Description: 根据json路径查询脚本
     * @Author: zrd
     * @Date: 2021/5/17 11:44
     * @param scriptRoute
     * @return com.geominfo.mlsql.domain.po.TSystemResources
     */
    TSystemResources selectScriptByRoute(String scriptRoute,int resourceId);

    /**
     * @description: 查询满足条件的实体
     * @author: LF
     * @date: 2021/6/8
     * @param checkParamVo
     * @return java.util.List<com.geominfo.mlsql.domain.vo.SystemResourceVo>
     */
    List<SystemResourceVo> checkSystemParamName(CheckParamVo checkParamVo);

    /**
     * @description: 查询工作空间列表
     * @author: LF
     * @date: 2021/6/11
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo>
     */
    List<WorkSpaceInfoResult> getWorkSpaceLists(Integer userId);

    /**
     * @description: 根据名称查询工作空间列表
     * @author: LF
     * @date: 2021/6/21
     * @param userId, spaceName
     * @return java.util.List<com.geominfo.mlsql.domain.vo.WorkSpaceInfoVo>
     */
    List<WorkSpaceInfoResult> getWorkSpaceListsByName(@Param("userId") BigDecimal userId,@Param("spaceName") String spaceName);

    List<TSystemResources> getAllRecoverResources(BigDecimal workSpaceId, BigDecimal userId);

    /**
     * @description: 获取首页查询最近相关脚本
     * @author: LF
     * @date: 2021/6/24
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.po.TSystemResources>
     */
    List<TSystemResources> getRecentlyScripts(Integer userId);
}
