package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MLSQLAuthTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: AuthMapper
 * @author: anan
 * @create: 2020-06-10 15:43
 * @version: 1.0.0
 */
@Mapper
@Component
public interface AuthMapper {
    public List<MLSQLAuthTable> fetchAuth(String userName);
}
