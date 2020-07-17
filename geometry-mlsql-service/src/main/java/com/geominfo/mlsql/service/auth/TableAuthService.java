package com.geominfo.mlsql.service.auth;


import com.geominfo.mlsql.domain.vo.MLSQLAuthTable;
import com.geominfo.mlsql.domain.vo.MLSQLTable;
import com.geominfo.mlsql.domain.vo.MlsqlGroup;

import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TableAuthService
 * @author: anan
 * @create: 2020-06-10 11:51
 * @version: 1.0.0
 */
public interface TableAuthService {
    /**
      * description: 根据用户名称获取所有授权信息
      * author: anan
      * date: 2020/6/10
      * param: userName
      * return: Map<String, String>
     */
    public List<MLSQLAuthTable> fetchAuth(String userName);
    /**
      * description: engine传过来表验证表是否授权
      * author: anan
      * date: 2020/6/11
      * param:
      * return:
     */
    public boolean checkAuth(String key, MLSQLTable mlsqlTable, String home, Map<String, String> authTables);
    /**
      * description:
      * author: anan
      * date: 2020/6/11
      * param:
      * return:
     */
    public boolean forbidden(MLSQLTable mlsqlTable, String home);
    /**
     * description:临时表不需要权限验证
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    public boolean withoutAuthSituation(MLSQLTable mlsqlTable, String home);
    /**
     * description: 统一字段返回信息
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    public String unifyColumn(String column);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    List<Map<String, Object>> fetchTables(MlsqlGroup mlsqlGroup);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    String addTableForTeam(MLSQLAuthTable AuthTable, int groupId);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    String removeTable(MlsqlGroup mlsqlGroup, int tableId);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    String addTableForRole(int groupRoleId, String roleNames, String operatorTypes);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    String removeRoleTable(int groupRoleId,int tableId);
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param:
      * return: 
     */
    
    List<Map<String, Object>> getAuthTableDetail(Map<String, Object> map);
}
