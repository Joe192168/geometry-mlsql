package com.geominfo.mlsql.service.auth.impl;

import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.GlobalConstant;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.mapper.AuthMapper;
import com.geominfo.mlsql.service.auth.TableAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TableAuthServiceImpl
 * @author: anan
 * @create: 2020-06-10 11:51
 * @version: 1.0.0
 */
@Service
public class TableAuthServiceImpl implements TableAuthService {
    @Autowired
    private AuthMapper authMapper;
    /**
     * description: 根据用户名称获取所有授权信息
     * author: anan
     * date: 2020/6/10
     * param: userName
     * return: Map<String, String>
     */
    @Override
    public List<MLSQLAuthTable> fetchAuth(String userName) {
        return authMapper.fetchAuth(userName);
    }
    /**
     * description: engine传过来表验证表是否授权
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    @Override
    public boolean checkAuth(String key, MLSQLTable mlsqlTable, String home, Map<String, String> authTables) {
        if (forbidden(mlsqlTable, home)) return false;
        if (withoutAuthSituation(mlsqlTable, home)) return true;
        String operateType = mlsqlTable.getOperateType().get("name");
        if(authTables.get(key) != null && authTables.get(key).equals(operateType)){
            return true;
        }
        return false;
    }
    /**
     * description:
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    @Override
    public boolean forbidden(MLSQLTable mlsqlTable, String home) {
        String operateType = mlsqlTable.getOperateType().get("name");
        return (GlobalConstant.HDFS.getIncludes().contains(mlsqlTable.getDb()) && operateType.equals("select"))
                ||operateType.equals("create")
                ||operateType.equals("insert");
    }
    /**
     * description:临时表不需要权限验证
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    @Override
    public boolean withoutAuthSituation(MLSQLTable mlsqlTable, String home) {
        String  tableType = ((Map<String, String>)mlsqlTable.getTableType()).get("name");
        String table = mlsqlTable.getTable() == null?"":mlsqlTable.getTable();
        String operateType = mlsqlTable.getOperateType().get("name");

        return tableType.equals(GlobalConstant.TEMP.getName()) || tableType.equals("custom") ||
                (tableType.equals(GlobalConstant.HDFS.getName()) && table.startsWith(home)) ||
                (tableType.equals("system") && table.equals("__resource_allocate__")==false) ||
                tableType.equals(GlobalConstant.GRAMMAR.getName())|| operateType.equals("set");
    }
    /**
     * description: 统一字段返回信息
     * author: anan
     * date: 2020/6/11
     * param:
     * return:
     */
    @Override
    public String unifyColumn(String column) {
        if(column == null){
            return GlobalConstant.KEY_WORD;
        }
        return column;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    @Override
    public List<Map<String, Object>> fetchTables(MlsqlGroup mlsqlGroup) {
        List<MLSQLAuthTable> mlsqlAuthTables = authMapper.getTeamTable(mlsqlGroup.getId());
        Map<String, Object> tables = new HashMap<>();
        List<Map<String, Object>> tableR = new ArrayList<>();
        for(MLSQLAuthTable mlsqlAuthTable : mlsqlAuthTables){
            tables.put("name", mlsqlAuthTable.getTableName());
            tables.put("db", mlsqlAuthTable.getDb());
            tables.put("tableType", mlsqlAuthTable.getTableType());
            tables.put("sourceType", mlsqlAuthTable.getSourceType());
            tables.put("id", mlsqlAuthTable.getId());
            tableR.add(tables);
        }
        return tableR;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addTableForTeam(MLSQLAuthTable mlsqlAuthTable, int mlsql_group_id) {
        authMapper.insertMlsqlTable(mlsqlAuthTable);
        MlsqlGroupTable mlsqlGroupTable = new MlsqlGroupTable();
        mlsqlGroupTable.setGroupId(mlsql_group_id);
        mlsqlGroupTable.setTableId(mlsqlAuthTable.getId());
        authMapper.insertMlsqlGroupTable(mlsqlGroupTable);
        return ReturnCode.SUCCESS;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeTable(MlsqlGroup mlsqlGroup, int tableId) {
        authMapper.deleteMlsqlTable(tableId);
        authMapper.deleteMlsqlGroupTable(tableId);
        return ReturnCode.SUCCESS;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addTableForRole(int mlsql_group_role_id, String tableIds, String operatorTypes) {
        for(String tableId : tableIds.split(",") ){
            for(String operatorType : operatorTypes.split(",")){
                MlsqlGroupRoleAuth mlsqlGroupRoleAuth = new MlsqlGroupRoleAuth();
                mlsqlGroupRoleAuth.setTableId(Integer.valueOf(tableId));
                mlsqlGroupRoleAuth.setOperateType(operatorType);
                mlsqlGroupRoleAuth.setGroupRoleId(mlsql_group_role_id);
                if(authMapper.getMlsqlGroupRoleAuth(mlsqlGroupRoleAuth) == null ){
                    authMapper.insertMlsqlGroupRoleAuth(mlsqlGroupRoleAuth);
                }
            }
        }
        return ReturnCode.SUCCESS;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param: 
      * return: 
     */
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String removeRoleTable(int groupRoleId, int tableId) {
        MlsqlGroupRoleAuth mlsqlGroupRoleAuth = new MlsqlGroupRoleAuth();
        mlsqlGroupRoleAuth.setGroupRoleId(groupRoleId);
        mlsqlGroupRoleAuth.setTableId(tableId);
        authMapper.deleteGroupRoleAuth(mlsqlGroupRoleAuth);
        return ReturnCode.SUCCESS;
    }
    /**
      * description: 
      * author: anan
      * date: 2020/7/15
      * param:
      * return: 
     */
    
    @Override
    public List<Map<String, Object>> getAuthTableDetail(Map<String, Object> map) {
        return authMapper.getAuthTableDetail(map);
    }
}
