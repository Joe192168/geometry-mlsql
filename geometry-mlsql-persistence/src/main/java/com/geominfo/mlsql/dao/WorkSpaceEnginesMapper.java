package com.geominfo.mlsql.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.WorkSpaceEngine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;


/**
 * @program: geometry-bi
 * @description:
 * @author: LF
 * @create: 2021/6/16 11:25
 * @version: 1.0.0
 */
@Mapper
public interface WorkSpaceEnginesMapper extends BaseMapper<WorkSpaceEngine> {

    /**
     * @description: 删除空间引擎配置
     * @author: LF
     * @date: 2021/6/17
     * @param engineId, spaceId
     * @return void
     */
    void deleteEngine(@Param("engineId") BigDecimal engineId,@Param("spaceId") BigDecimal spaceId);

    int setDefaultEngine(@Param("engineId") BigDecimal engineId,@Param("spaceId") BigDecimal spaceId);

    List<WorkSpaceEngine> getSpaceEngineBySpaceIdAndEngineId(@Param("engineId") BigDecimal engineId,@Param("spaceId") BigDecimal spaceId);
}
