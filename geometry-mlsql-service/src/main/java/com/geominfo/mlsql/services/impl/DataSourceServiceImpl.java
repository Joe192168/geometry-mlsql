package com.geominfo.mlsql.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geominfo.authing.common.constants.ResourceTypeConstants;
import com.geominfo.mlsql.dao.TSystemResourcesDao;
import com.geominfo.mlsql.domain.dto.DataBaseDTO;
import com.geominfo.mlsql.domain.po.TSystemResources;
import com.geominfo.mlsql.domain.vo.MlsqlExecuteSqlVO;
import com.geominfo.mlsql.services.DataSourceService;
import com.geominfo.mlsql.services.TSystemResourceService;
import com.geominfo.mlsql.utils.DtaBaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: DataSourceServiceImpl
 * @date 2021/6/8 9:48
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private TSystemResourcesDao systemResourcesDao;

    @Autowired
    private TSystemResourceService systemResourceService;

    @Autowired
    private MlsqlServiceImpl mlsqlService;

    @Override
    public Map<String,Object> addDataSource(DataBaseDTO dataBaseDTO, BigDecimal resourceId, BigDecimal userId) {
        HashMap<String, Object> retMap = new HashMap<>();

        boolean b = DtaBaseUtil.tryLink(dataBaseDTO);
        if (!b) {
            retMap.put("flag",false);
            retMap.put("msg","连接失败");
            return retMap;
        }

        List<TSystemResources> tSystemResources = systemResourcesDao.selectList(new QueryWrapper<TSystemResources>().eq("resource_type_id", ResourceTypeConstants.SJY).eq("parentid", resourceId));

        for (TSystemResources tSystemResource : tSystemResources) {
            DataBaseDTO dataBaseDTO1 = JSONObject.parseObject(tSystemResource.getContentInfo(), dataBaseDTO.getClass());
            if (dataBaseDTO1.getConnectName().equals(dataBaseDTO.getConnectName())) {
                retMap.put("flag",false);
                retMap.put("msg",dataBaseDTO.getConnectName() + "已存在" );
                return retMap;
            }
        }

        //向mlsql发送固化请求
        storageConnectInfoToMlsql(dataBaseDTO,resourceId,userId);


        TSystemResources insertSystemResource = new TSystemResources();
        insertSystemResource.setResourceName(dataBaseDTO.getConnectName());
        insertSystemResource.setDisplayName(dataBaseDTO.getConnectName());
        insertSystemResource.setContentInfo(JSONObject.toJSONString(dataBaseDTO));
        insertSystemResource.setDescription(dataBaseDTO.getConnectName());
        insertSystemResource.setParentid(resourceId);
        insertSystemResource.setResourceTypeId(new BigDecimal(ResourceTypeConstants.SJY));
        insertSystemResource.setResourceLevel("0");
        insertSystemResource.setIsInnerResource(BigDecimal.ZERO);
        insertSystemResource.setOwner(userId);
        insertSystemResource.setResourceState(BigDecimal.ZERO);
        insertSystemResource.setCreateTime(new Date());
        insertSystemResource.setUpdateTime(new Date());

        Boolean aBoolean = systemResourceService.insertTSystemResourceAutoIncrement(insertSystemResource);
        retMap.put("flag",aBoolean);
        return retMap;
    }

    @Override
    public Map<String, Object> updateDataSource(DataBaseDTO dataBaseDTO, BigDecimal resourceId,BigDecimal parentId) {
        HashMap<String, Object> retMap = new HashMap<>();
        QueryWrapper<TSystemResources> queryWrapper = new QueryWrapper<TSystemResources>().eq("resource_type_id", ResourceTypeConstants.SJY).eq("parentid", parentId).eq("resource_name", dataBaseDTO.getConnectName());
        TSystemResources tSystemResources = systemResourcesDao.selectOne(queryWrapper);
        if (tSystemResources != null) {
            retMap.put("flag",false);
            retMap.put("msg",dataBaseDTO.getConnectName() + "已存在");
            return retMap;
        }

        //向engine发送固化请求
        storageConnectInfoToMlsql(dataBaseDTO,resourceId,parentId);


        TSystemResources tSystemResources1 = new TSystemResources();
        tSystemResources1.setId(resourceId);
        tSystemResources1.setContentInfo(JSONObject.toJSONString(dataBaseDTO));
        tSystemResources1.setResourceName(dataBaseDTO.getConnectName());
        tSystemResources1.setUpdateTime(new Date());
        tSystemResources1.setDisplayName(dataBaseDTO.getConnectName());
        tSystemResources1.setDescription(dataBaseDTO.getConnectName());
        systemResourcesDao.updateById(tSystemResources1);
        retMap.put("flag",true);
        return retMap;
    }

    @Override
    public boolean deleteDataSource(BigDecimal resourceId) {
        int i = systemResourcesDao.deleteById(resourceId);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<TSystemResources> selectDataSources(String dataSourceName) {
        List<TSystemResources> resource_name = systemResourcesDao.selectList(new QueryWrapper<TSystemResources>().eq("resource_type_id",ResourceTypeConstants.SJY).like("resource_name", dataSourceName));
        return resource_name;
    }

    @Override
    public List<Map<String, Object>> listTablesByDataSource(DataBaseDTO dataBaseDTO) {
        List<Map<String, Object>> maps = DtaBaseUtil.listTables(dataBaseDTO);
        return maps;
    }

    @Override
    public List<Map<String, Object>> getColumnsByTableName(DataBaseDTO dataBaseDTO) {
        List<Map<String, Object>> maps = DtaBaseUtil.listColumns(dataBaseDTO);
        return maps;
    }

    void storageConnectInfoToMlsql(DataBaseDTO dataBaseDTO,BigDecimal resourceId,BigDecimal userId) {

        String url = "";
        String driver = "";


        if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("ORACLE")) {
            //
            url += "jdbc:oracle:thin:@//";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += "/" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("MYSQL")) {
            //
            url += "jdbc:mysql://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("SQLSERVER")) {
            //
            url += "jdbc:jtds:sqlserver://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
            url += ";tds=8.0;lastupdatecount=true";
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("SQLSERVER2005")) {
            //
            url += "jdbc:sqlserver://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("DB2")) {
            url += "jdbc:db2://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("INFORMIX")) {
            // Infox mix 可能有BUG
            url += "jdbc:informix-sqli://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("SYBASE")) {
            url += "jdbc:sybase:Tds:";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        } else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("POSTGRESQL")) {
            url += "jdbc:postgresql://";
            url += dataBaseDTO.getIp().trim();
            url += ":" + dataBaseDTO.getPort().trim();
            url += ":" + dataBaseDTO.getDbname();
        }else {
            throw new RuntimeException("不认识的数据库类型!");
        }

        if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("ORACLE")) {
            driver = "oracle.jdbc.driver.OracleDriver";
        }else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("MYSQL")) {
            driver = "com.mysql.jdbc.driver";
        }else if (dataBaseDTO.getDataBaseType().equalsIgnoreCase("POSTGRESQL")) {
            driver ="com.postgresql.jdbc.driver";
        }
        String sql = "connect jdbc where url= \""+ url +"\" and driver=\"" + driver + "\" and user=\" " +dataBaseDTO.getUsername() +"\" and password=\""+dataBaseDTO.getPassword()+"\" as " +resourceId+"_"+dataBaseDTO.getConnectName()+ ";";

        MlsqlExecuteSqlVO mlsqlExecuteSqlVO = new MlsqlExecuteSqlVO();
        mlsqlExecuteSqlVO.setSql(sql);
        mlsqlExecuteSqlVO.setAsync(false);
        mlsqlExecuteSqlVO.setRefuseConnect(true);
        mlsqlExecuteSqlVO.setOwner(userId.toString());
        mlsqlExecuteSqlVO.setExecuteMode("query");
        String s = mlsqlService.executeMlsql(mlsqlExecuteSqlVO);
    }

}
