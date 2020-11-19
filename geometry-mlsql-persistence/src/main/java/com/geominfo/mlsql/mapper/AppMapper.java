package com.geominfo.mlsql.mapper;


import com.geominfo.mlsql.domain.vo.MlsqlApply;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: geometry-mlsql
 * @description: 集群后端配置Mpper
 * @author: ryan(丁帅波)
 * @create: 2020-11-19 15:08
 * @version: 1.0.0
 */
@Mapper
@Component
public interface AppMapper {

    List<MlsqlApply> getMlsqlApplyList(Map<String, Object> map);

}
