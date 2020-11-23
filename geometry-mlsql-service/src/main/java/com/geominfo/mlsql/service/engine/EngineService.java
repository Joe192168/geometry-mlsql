package com.geominfo.mlsql.service.engine;

import com.geominfo.mlsql.domain.vo.MlsqlEngine;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineService
 * @author: anan
 * @create: 2020-11-23 14:14
 * @version: 1.0.0
 */

public interface EngineService {
    List<MlsqlEngine> findByName(String name);
    int updateEngine(MlsqlEngine mlsqlEngine);
    int insertEngine(MlsqlEngine mlsqlEngine);
    int deleteEngineById(int id);
    List<MlsqlEngine> getAllEngine(Map<String, Object> paraMap);
}
