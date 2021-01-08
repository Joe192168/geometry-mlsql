package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: MlsqlDsMapper
 * @author: BJZ
 * @create: 2020-06-10 15:43
 * @version: 1.0.0
 */
@Mapper
@Component
public interface MlsqlDsMapper {

   void saveDs(MlsqlDs mlsqlDs) ;
   void deleteDs(Map<String, Object> map) ;
   List<MlsqlDs> listDs(int userId) ;
   List<MlsqlDs> getDs(Map<String, Object> map) ;

   MlsqlDs getDsInfo(String asName);

   void updateDs(MlsqlDs mlsqlDs);

    

}
