package com.geominfo.mlsql.services;

import com.geominfo.mlsql.domain.dto.DataBaseDTO;
import com.geominfo.mlsql.domain.dto.TransferDataSource;
import com.geominfo.mlsql.domain.po.TSystemResources;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @title: DataSourceService
 * @date 2021/6/89:47
 */
public interface DataSourceService {

    /***
     * @Description: 添加数据源方法
     * @Author: zrd
     * @Date: 2021/6/8 10:08
     * @param dataBaseDTO 数据源对象
     * @param resourceId 工作空间did
     * @param userId 数据源所有者id
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String,Object> addDataSource(DataBaseDTO dataBaseDTO, BigDecimal resourceId, BigDecimal userId);

    Map<String, Object> updateDataSource(DataBaseDTO dataBaseDTO, BigDecimal resourceId,BigDecimal parentId);

    boolean deleteDataSource(BigDecimal resourceId);

    List<TSystemResources> selectDataSources(String dataSourceName);

    List<Map<String, Object>> listTablesByDataSource(DataBaseDTO dataBaseDTO);

    List<Map<String, Object>> getColumnsByTableName(DataBaseDTO dataBaseDTO);
}
