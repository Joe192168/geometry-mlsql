package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlScriptFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @program: geometry-mlsql
 * @description: 脚本映射类
 * @author: BJZ
 * @create: 2020-06-04 14:48
 * @version: 3.0.0
 */
@Mapper
@Component
public interface ScriptFileMapper {

    MlsqlScriptFile getScriptById(Integer id);
}