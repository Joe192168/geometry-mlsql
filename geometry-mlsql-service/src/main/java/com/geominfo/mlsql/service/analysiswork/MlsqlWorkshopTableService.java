package com.geominfo.mlsql.service.analysiswork;

import com.geominfo.mlsql.domain.vo.MlsqlWorkshopTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * @program: geometry-mlsql
 * @description: 分析工作信息接口
 * @author: ryan
 * @create: 2020-11-16 11：09
 * @version: 1.0.0
 */
public interface MlsqlWorkshopTableService {

    List<MlsqlWorkshopTable> getMlsqlWorkshopList(@Param("sessionId") String sessionId,
                                                  @Param("mlsqlUserId") Integer mlsqlUserId);


    String mlsqlWorkshopSave(MlsqlWorkshopTable mlsqlWorkshopTable);


    String mlsqlWorkshopDelete(String tableName);


    MlsqlWorkshopTable getMlsqlWorkshop(Map<String, Object> map);


    String updateMlsqlWorkshop(Map<String, Object> map);
}
