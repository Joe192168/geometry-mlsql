package com.geominfo.mlsql.service.datasource;

import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.service.metadb.meta.core.MetaLoader;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DataSourceService
 * @author: anan
 * @create: 2020-11-30 11:38
 * @version: 1.0.0
 */
public interface DataSourceService {
    MetaLoader getMetaLoader(JDBCD jdbcd);
    Map<String, Object> testDataSource(JDBCD jdbcd);
    Set<String> getTables(JDBCD jdbcd);
    ResultSet getQuery(JDBCD jdbcd, String sql);

    /**
     * 根据不同的数据库类型获取JDBCD对象
     * @param jdbcd
     * @return
     */
    JDBCD JDBCDConnectParams(JDBCD jdbcd);
}
