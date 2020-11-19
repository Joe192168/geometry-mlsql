package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlWorkshopTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan(丁帅波)
 * @create: 2020-11-16 10:49
 * @version: 1.0.0
 */
@Mapper
@Component
public interface WorkshopTableMapper {

    List<MlsqlWorkshopTable> getMlsqlWorkshopList(@Param("sessionId") String sessionId,
                                                  @Param("mlsqlUserId") Integer mlsqlUserId);


    int mlsqlWorkshopSave(MlsqlWorkshopTable mlsqlWorkshopTable);


    int mlsqlWorkshopDelete(String tableName);


    MlsqlWorkshopTable getMlsqlWorkshop(Map<String, Object> map);


    int updateMlsqlWorkshop(Map<String, Object> map);




}
