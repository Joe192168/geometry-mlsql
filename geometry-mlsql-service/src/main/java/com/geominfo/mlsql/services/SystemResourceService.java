package com.geominfo.mlsql.services;

import com.geominfo.authing.common.pojo.base.BaseResultVo;
import com.geominfo.mlsql.domain.po.TSystemResources;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: geometry-bi
 * @description: 资源类常用接口
 * @author: LF
 * @create: 2021/6/8 9:44
 * @version: 1.0.0
 */
public interface SystemResourceService {

    /**
     * @description: 新增资源
     * @author: LF
     * @date: 2021/6/8
     * @param resourceVo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo insertResource(TSystemResources resourceVo);

    /**
     * @description: 编辑资源
     * @author: LF
     * @date: 2021/6/8
     * @param resourceVo
     * @return com.geominfo.authing.common.pojo.base.BaseResultVo
     */
    BaseResultVo updateResource(TSystemResources resourceVo);

    /**
     * @description: 根据id删除资源
     * @author: LF
     * @date: 2021/6/8
     * @param id
     * @return java.lang.Boolean
     */
    Boolean deleteResourceById(BigDecimal id);

    /**
     * @description: 查询最近相关脚本
     * @author: LF
     * @date: 2021/6/24
     * @param userId
     * @return java.util.List<com.geominfo.mlsql.domain.po.TSystemResources>
     */
    List<TSystemResources> getRecentlyScripts(BigDecimal userId);


}
