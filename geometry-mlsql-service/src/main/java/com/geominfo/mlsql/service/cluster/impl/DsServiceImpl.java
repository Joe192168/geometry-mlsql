package com.geominfo.mlsql.service.cluster.impl;

import com.geominfo.mlsql.domain.vo.JDBCD;
import com.geominfo.mlsql.domain.vo.MlsqlDs;
import com.geominfo.mlsql.domain.vo.MlsqlUser;
import com.geominfo.mlsql.mapper.MlsqlDsMapper;
import com.geominfo.mlsql.service.cluster.DsService;
import com.geominfo.mlsql.service.datasource.DataSourceService;
import com.geominfo.mlsql.utils.JSONTool;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @program: geometry-mlsql
 * @description: DsServiceImpl
 * @author: BJZ
 * @create: 2020-11-26 16:15
 * @version: 1.0.0
 */
@Service
public class DsServiceImpl implements DsService {

    @Autowired
    private MlsqlDsMapper mlsqlDsMapper ;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public void saveDs(MlsqlDs mlsqlDs) {
        mlsqlDsMapper.saveDs(mlsqlDs);
    }

    @Override
    public void deleteDs(MlsqlUser mlsqlUser, int pId) {
        Map<String ,Object> map = new ConcurrentHashMap<>() ;
        int userId = mlsqlUser.getId() ;
        map.put("pId" ,pId) ;
        map.put("userId" ,userId) ;
        mlsqlDsMapper.deleteDs(map);
    }

    @Override
    public List<MlsqlDs> listDs(MlsqlUser mlsqlUser) {
        int userId = mlsqlUser.getId() ;
        return mlsqlDsMapper.listDs(userId);
    }

    @Override
    public List<MlsqlDs> getDs(MlsqlUser mlsqlUser, String name, String format) {
        Map<String ,Object> map = new ConcurrentHashMap<>() ;
        map.put("userId" ,mlsqlUser.getId()) ;
        map.put("pName" ,name) ;
        map.put("pFormat" ,format) ;
        return mlsqlDsMapper.getDs(map);
    }

    @Override
    public String getConnect(String name ,MlsqlUser mlsqlUser){
        return
               getDs(mlsqlUser, name, "jdbc").stream().map(md -> (JDBCD) JSONTool.parseJson(
                       md.getParams(), JDBCD.class
               )).collect(Collectors.toList()).stream().map(item ->
                   "connect jdbc where \n url=\"" + item.getUrl() + "\" \n and driver=\"" + item.getDriver()
                       +"\" \n and user=\"" + item.getUser() + "\" \n and password=\"" + item.getPassword() + "\" \n as " +
                           item.getName()+";"
               ).collect(Collectors.toList()).get(0);
    }


    @Override
    public ResultSet showTable(JDBCD jdbc, String cloumnName,String tableName){
        JDBCD jdbcd = new JDBCD();
        jdbcd.setDb(jdbc.getDb());
        jdbcd.setUrl(jdbc.getUrl());
        jdbcd.setName(jdbc.getUser());
        jdbcd.setPassword(jdbc.getPassword());
        jdbcd.setJType(jdbc.getJType());
        return  dataSourceService.getQuery(jdbcd,"select max("+ cloumnName +"),min("+ cloumnName +") from "+ tableName);
    }

    @Override
    public Set<String> showTable(JDBCD jdbc){
        JDBCD jdbcd = new JDBCD();
        jdbcd.setDb(jdbc.getDb());
        jdbcd.setUrl(jdbc.getUrl());
        jdbcd.setName(jdbc.getUser());
        jdbcd.setPassword(jdbc.getPassword());
        jdbcd.setJType(jdbc.getJType());
        return  dataSourceService.getTables(jdbcd);
    }



}