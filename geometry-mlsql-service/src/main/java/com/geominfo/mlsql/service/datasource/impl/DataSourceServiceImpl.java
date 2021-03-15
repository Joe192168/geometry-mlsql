package com.geominfo.mlsql.service.datasource.impl;

import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import com.geominfo.mlsql.service.metadb.meta.core.MetaLoader;
import com.geominfo.mlsql.service.metadb.meta.core.MetaLoaderImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            metaLoader = new MetaLoaderImpl(jdbcd);
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

    @Override
    public JDBCD JDBCDConnectParams(JDBCD jdbcd) {
        JDBCD connectParams = null;
        if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("mysql") && jdbcd.getFormat().toLowerCase().equals("jdbc")){
            connectParams = createJDBCD(jdbcd, "jdbc:mysql://" + jdbcd.getHost() + ":"
                    + jdbcd.getPort() + "/" + jdbcd.getDb(),"com.mysql.jdbc.Driver");
        } else if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("oracle") && jdbcd.getFormat().toLowerCase().equals("jdbc")) {
            connectParams = createJDBCD(jdbcd, "jdbc:oracle:thin:@//" + jdbcd.getHost() +":"
                    + jdbcd.getPort()+"/"+jdbcd.getDb(),"oracle.jdbc.driver.OracleDriver");
        } else if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("sqlserver") && jdbcd.getFormat().toLowerCase().equals("jdbc")) {
            connectParams = createJDBCD(jdbcd, "jdbc:sqlserver://"+ jdbcd.getHost()+ ":" +
                    jdbcd.getPort() + ";DatabaseName=" + jdbcd.getDb(),"com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } else if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("hbase") && jdbcd.getFormat().toLowerCase().equals("hbase")) {
            connectParams = createJDBCD(jdbcd, "url","driver");
        }
        return connectParams;
    }


    /**
     * 创建JDBCD对象
     * @param jdbcd
     * @param url
     * @param driver
     * @return
     */
    public JDBCD createJDBCD(JDBCD jdbcd,String url,String driver) {
        JDBCD connectParams = new JDBCD();
        connectParams.setDb(jdbcd.getDb());
        if (jdbcd.getJType().replaceAll("\\s*", "").toLowerCase().equals("hbase")) {
            connectParams.setUrl(jdbcd.getHost() + ":" + jdbcd.getPort());
            connectParams.setFormat("org.apache.spark.sql.execution.datasources.hbase");
        } else {
            connectParams.setUrl(url);
            connectParams.setFormat(jdbcd.getFormat().toLowerCase());
        }
        connectParams.setDriver(driver);
        connectParams.setUser(jdbcd.getUser());
        connectParams.setName(jdbcd.getName());
        connectParams.setPassword(jdbcd.getPassword());
        connectParams.setHost(jdbcd.getHost());
        connectParams.setJType(jdbcd.getJType().replaceAll("\\s*", "").toLowerCase());
        connectParams.setPort(jdbcd.getPort());
        if (StringUtils.isBlank(jdbcd.getFamily()) || jdbcd.getFamily().toLowerCase().equals("string")) {
            connectParams.setFamily("0");
        } else {
            connectParams.setFamily(jdbcd.getFamily());
        }
        return connectParams;
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
