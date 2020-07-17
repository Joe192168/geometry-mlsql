package com.geominfo.mlsql.mapper;

import com.geominfo.mlsql.domain.vo.MLSQLAuthTable;
import com.geominfo.mlsql.domain.vo.MlsqlGroupRoleAuth;
import com.geominfo.mlsql.domain.vo.MlsqlGroupTable;
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
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    List<MLSQLAuthTable> fetchAuth(String userName);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    List<MLSQLAuthTable> getTeamTable(int mlsql_group_id);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int insertMlsqlTable(MLSQLAuthTable mlsqlAuthTable);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int insertMlsqlGroupTable(MlsqlGroupTable mlsqlGroupTable);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int deleteMlsqlTable(int id);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int deleteMlsqlGroupTable(int id);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    Map<String, Object> getMlsqlGroupRoleAuth(MlsqlGroupRoleAuth mlsqlGroupRoleAuth);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int insertMlsqlGroupRoleAuth(MlsqlGroupRoleAuth mlsqlGroupRoleAuth);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    int deleteGroupRoleAuth(MlsqlGroupRoleAuth mlsqlGroupRoleAuth);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param:
      * return: 
     */
    
    List<Map<String, Object>> getAuthTableDetail(Map<String, Object> map);
}
