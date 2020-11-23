package com.geominfo.mlsql.service.engine.impl;

import com.geominfo.mlsql.domain.vo.MlsqlEngine;
import com.geominfo.mlsql.mapper.EngineMapper;
import com.geominfo.mlsql.service.engine.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: EngineServiceImpl
 * @author: anan
 * @create: 2020-11-23 14:15
 * @version: 1.0.0
 */
@Service
public class EngineServiceImpl implements EngineService {

    @Autowired
    EngineMapper engineMapper;

    @Override
    public List<MlsqlEngine> findByName(String name) {
        return engineMapper.findByName(name);
    }

    @Override
    public int updateEngine(MlsqlEngine mlsqlEngine) {
        return engineMapper.updateEngine(mlsqlEngine);
    }

    @Override
    public int insertEngine(MlsqlEngine mlsqlEngine) {
        return engineMapper.insertEngine(mlsqlEngine);
    }

    @Override
    public int deleteEngineById(int id) {
        return engineMapper.deleteEngineById(id);
    }

    @Override
    public List<MlsqlEngine> getAllEngine(Map<String, Object> paraMap) {
        return engineMapper.getAllEngine(paraMap);
    }

}
