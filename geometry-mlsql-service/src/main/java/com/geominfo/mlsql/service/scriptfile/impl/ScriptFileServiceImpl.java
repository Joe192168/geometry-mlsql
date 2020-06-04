package com.geominfo.mlsql.service.scriptfile.impl;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;

/**
 * @program: geometry-mlsql
 * @description: 获取脚本接口
 * @author: BJZ
 * @create: 2020-06-04 14:53
 * @version: 3.0.0
 */

public interface ScriptFileServiceImpl {

    MlsqlScriptFile getScriptById(Integer id);
}