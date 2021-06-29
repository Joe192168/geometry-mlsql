package com.geominfo.mlsql.services;  /**
 * @title: EngineMonitorService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/6/2414:32
 */

import com.geominfo.mlsql.domain.vo.EngineDetailsVO;

import java.util.Map;

/**
 * @Auther zrd
 * @Date 2021-06-24 14:32 
 *
 */

public interface EngineMonitorService {

    Map<String,Object> saveEngineMonitor(EngineDetailsVO engineDetailsVO);
}
