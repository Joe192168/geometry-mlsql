package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlEngine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineMapper
 * @author: anan
 * @create: 2020-11-23 14:34
 * @version: 1.0.0
 */
@Mapper
@Component
public interface EngineMapper {
    List<MlsqlEngine> findByName(@Param(value = "name") String name);
    int updateEngine(MlsqlEngine mlsqlEngine);
    int insertEngine(MlsqlEngine mlsqlEngine);
    int deleteEngineById(@Param(value = "id") int id);
    List<MlsqlEngine> getAllEngine(Map<String, Object> paraMap);
}
