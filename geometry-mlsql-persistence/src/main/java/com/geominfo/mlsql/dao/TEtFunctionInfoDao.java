package com.geominfo.mlsql.dao;  /**
 * @title: TEtFunctionInfoDao
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/5/19 10:35
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.TEtFunctionInfo;
import com.geominfo.mlsql.domain.po.TSystemResources;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther zrd
 * @Date 2021-05-19 10:35 
 *
 */
@Mapper
public interface TEtFunctionInfoDao extends BaseMapper<TEtFunctionInfo> {

    /**
     * @Description: 根据父节点ID获取该父节点下所有资源
     * @Author: zrd
     * @Date: 2021/5/12 14:48
     * @param parentId 父节点id
     * @return java.util.List<com.geominfo.mlsql.domain.po.TSystemResources>
     */
    List<TEtFunctionInfo> listByParentId(@Param("parentId") BigDecimal parentId);
}
