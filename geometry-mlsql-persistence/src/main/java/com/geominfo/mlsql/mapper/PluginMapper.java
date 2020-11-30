package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MlsqlAnalysisPlugin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan(丁帅波)
 * @create: 2020-11-25 15:08
 * @version: 1.0.0
 */
@Mapper
@Component
public interface PluginMapper {

    List<MlsqlAnalysisPlugin> getMlsqlAnalysisPluginList(Map<String, Object> map);


    int updateMlsqlAnalysis(Map<String, Object> map);


    int addMlsqlAnalysis(Map<String, Object> map);

}
