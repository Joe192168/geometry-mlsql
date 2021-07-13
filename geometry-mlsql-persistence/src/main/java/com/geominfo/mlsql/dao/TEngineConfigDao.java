package com.geominfo.mlsql.dao;  /**
 * @title: TEngineConfigDao
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/7/110:22
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.EngineStatusMananer;
import com.geominfo.mlsql.domain.po.TEngineConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Auther zrd
 * @Date 2021-07-01 10:22 
 *
 */
@Mapper
public interface TEngineConfigDao extends BaseMapper<TEngineConfig> {
    List<EngineStatusMananer> getLogByCondition(@Param("engineName") String engineName, @Param("engineUrl") String engineUrl, @Param("monitorStatus") Long monitorStatus, @Param("dealType") Long dealType, @Param("monitorStartTime") Date monitorStartTime, @Param("monitorEndTime") Date monitorEndTime);
}
