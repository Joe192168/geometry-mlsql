package com.geominfo.mlsql.mapper;
import com.geominfo.mlsql.domain.vo.ScriptExeLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @program: geometry-mlsql
 * @description: 脚本执行log
 * @author: BJZ
 * @create: 2021-1-11 15:08
 * @version: 1.0.0
 */
@Mapper
@Component
public interface ScriptExeLogMapper {

    void addLog(ScriptExeLog scriptExeLog) ;
    void updateLog(ScriptExeLog scriptExeLog) ;
}
