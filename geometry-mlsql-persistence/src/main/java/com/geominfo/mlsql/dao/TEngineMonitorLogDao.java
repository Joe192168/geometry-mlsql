package com.geominfo.mlsql.dao;  /**
 * @title: TEngineMonitorLogDao
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/7/111:48
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geominfo.mlsql.domain.po.TEngineMonitorLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther zrd
 * @Date 2021-07-01 11:48 
 *
 */
@Mapper
public interface TEngineMonitorLogDao extends BaseMapper<TEngineMonitorLog> {
}
