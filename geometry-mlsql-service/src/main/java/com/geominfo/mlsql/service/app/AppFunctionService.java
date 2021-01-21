package com.geominfo.mlsql.service.app;

import org.springframework.stereotype.Service;

/**
 * @program: geometry-mlsql
 * @description: AppFunctionService
 * @author: BJZ
 * @create: 2021-01-19 09:46
 * @version: 1.0.0
 */
@Service
public interface AppFunctionService  {

    <T> T find(String fName) ;
}