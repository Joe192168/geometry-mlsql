package com.geominfo.mlsql.service.scriptfile.impl;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import com.geominfo.mlsql.mapper.ScriptFileMapper;
import com.geominfo.mlsql.service.scriptfile.ScriptFileService;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: geometry-mlsql
 * @description: 获取脚本内容实现类
 * @author: BJZ
 * @create: 2020-06-04 14:55
 * @version: 3.0.0
 */
@Service
public class ScripteFileSverviceImpl implements ScriptFileService {


    @Autowired
    private ScriptFileMapper scriptFileMapper ;

    @Override
    public MlsqlScriptFile getScriptById(@NotNull Integer id) {
        return scriptFileMapper.getScriptById(id);
    }
}