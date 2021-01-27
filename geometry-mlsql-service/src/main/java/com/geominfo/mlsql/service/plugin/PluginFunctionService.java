package com.geominfo.mlsql.service.plugin;

import org.springframework.stereotype.Service;

/**
 * @program: geometry-mlsql
 * @description: PluginFunctionService
 * @author: BJZ
 * @create: 2021-01-27 09:46
 * @version: 1.0.0
 */
@Service
public interface PluginFunctionService {

    <T> T find(String pName) ;
}