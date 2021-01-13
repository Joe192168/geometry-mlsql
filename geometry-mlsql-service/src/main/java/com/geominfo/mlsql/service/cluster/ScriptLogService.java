package com.geominfo.mlsql.service.cluster;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @program: geometry-mlsql
 * @description: ScriptLogService
 * @author: BJZ
 * @create: 2021-01-06 10:11
 * @version: 1.0.0
 */
@Service
public interface ScriptLogService {

    <T> T addLog(T t) throws ExecutionException, InterruptedException;

}