package com.geominfo.mlsql.service.datasource.impl;

import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import com.geominfo.mlsql.utils.JDBCUtils;
import com.geominfo.mlsql.utils.metadb.meta.core.MetaLoader;
import com.geominfo.mlsql.utils.metadb.meta.core.MetaLoaderImpl;
import com.geominfo.mlsql.utils.metadb.util.ResultSetExtractor;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: DataSourceServiceImpl
 * @author: anan
 * @create: 2020-11-30 11:37
 * @version: 1.0.0
 */
@Log4j2
@Service
public class DataSourceServiceImpl implements DataSourceService {
    @Autowired
    DriverSettingFactory driverSettingFactory;
    @Override
    public MetaLoader getMetaLoader(JDBCD jdbcd) {
        MetaLoader metaLoader = null;
        try {
            HikariDataSource hikariDataSource = new HikariDataSource();
            hikariDataSource.setJdbcUrl(jdbcd.getUrl());
            hikariDataSource.setUsername(jdbcd.getName());
            hikariDataSource.setPassword(jdbcd.getPassword());
            hikariDataSource.setDriverClassName(jdbcd.getDriver());
            //解决oracle读取不到REMARKS表注解设置
            if ("oracle".equals(jdbcd.getJType().toLowerCase())){
                Properties properties = new Properties();
                properties.setProperty("remarks", "true");
                properties.setProperty("useInformationSchema", "true");
                hikariDataSource.setDataSourceProperties(properties);
            }
            // 最小空闲连接数量
            hikariDataSource.setMinimumIdle(5);
            // 空闲连接存活最大时间，默认600000（10分钟）
            hikariDataSource.setIdleTimeout(180000);
            // 连接池最大连接数，默认是10
            hikariDataSource.setMaximumPoolSize(50);
            // 此属性控制从池返回的连接的默认自动提交行为,默认值：true
            hikariDataSource.setAutoCommit(true);
            // 连接池名称
            hikariDataSource.setPoolName("MyHikariCP");
            // 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟
            hikariDataSource.setMaxLifetime(1800000);
            // 数据库连接超时时间,默认30秒，即30000
            hikariDataSource.setConnectionTimeout(30000);

            metaLoader = (MetaLoader) new MetaLoaderImpl(hikariDataSource);
        } catch (Exception e) {
            log.error("获取数据源异常：[{}]", e);
            e.printStackTrace();
        }
        return metaLoader;
    }

    @Override
    public Map<String, Object> testDataSource(JDBCD jdbcd) {
        Map<String, Object> map = new HashMap<>(4);
        try {
            setDriverClass(jdbcd);
            MetaLoader metaLoader = getMetaLoader(jdbcd);
            Connection conn = metaLoader.testDataSource();
            if (conn != null) {
                map.put("flag", Boolean.TRUE);
                map.put("msg", "连接成功");
            } else {
                map.put("flag", Boolean.FALSE);
                map.put("msg", "连接失败");
            }
        } catch (Exception e) {
            map.put("flag", Boolean.FALSE);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    @Override
    public Set<String> getTables(JDBCD jdbcd) {
        setDriverClass(jdbcd);
        MetaLoader metaLoader = getMetaLoader(jdbcd);
        return metaLoader.getTableNames();
    }

    @Override
    public ResultSet getQuery(JDBCD jdbcd, String sql) {
        setDriverClass(jdbcd);
        MetaLoader metaLoader = getMetaLoader(jdbcd);
        return metaLoader.query(sql);
    }

    /**
     *
     *
     * @param jdbcd
     * @return
     */
    public void setDriverClass(JDBCD jdbcd) {
        String databaseName = jdbcd.getDb();
        databaseName = databaseName.replaceAll(" ", "").toLowerCase();
        DriverSetting driverSetting = driverSettingFactory.getDriverSetting(databaseName);
        driverSetting.setDriverAndClass(jdbcd);
    }
}
