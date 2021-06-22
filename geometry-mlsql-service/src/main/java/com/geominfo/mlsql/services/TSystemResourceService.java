package com.geominfo.mlsql.services;  /**
 * @title: TSystemResourceService
 * @projectName geometry-mlsql
 * @description: TODO
 * @author Lenovo
 * @date 2021/6/815:38
 */

import com.geominfo.mlsql.domain.po.TSystemResources;

import java.util.HashMap;

/**
 * @Auther zrd
 * @Date 2021-06-08 15:38 
 *
 */

public interface TSystemResourceService {

    Boolean insertTSystemResourceAutoIncrement(TSystemResources systemResources);
}
