package com.geominfo.mlsql.controller.auth;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geominfo.mlsql.controller.base.BaseController;
import com.geominfo.mlsql.domain.vo.*;
import com.geominfo.mlsql.globalconstant.ReturnCode;
import com.geominfo.mlsql.service.auth.TableAuthService;
import com.geominfo.mlsql.service.user.TeamRoleService;
import com.geominfo.mlsql.systemidentification.InterfaceReturnInformation;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: MLSQL CONSOLE后端接口
 * @description: TableAuthController
 * @author: anan
 * @create: 2020-06-10 11:47
 * @version: 1.0.0
 */
@RestController
@RequestMapping(value = "/api_v1")
@Api(value="用户表访问权限类",tags={"用户表访问权限接口"})
@Log4j2
public class TableAuthController extends BaseController{
    @Autowired
    private TableAuthService tableAuthService;

    @Autowired
    private TeamRoleService teamRoleService;

    @RequestMapping(value = "/verify/tables", method = RequestMethod.POST)
    @ApiOperation(value = "验证表是否授权接口", httpMethod = "POST")
    public String getVerifyAuthTable(@RequestParam Map params){

        String tables =  params.containsKey("tables")?(String) params.get("tables"):"";
        String owner =  params.containsKey("owner")?(String) params.get("owner"):"";
        String home =  params.containsKey("home")?(String) params.get("home"):"";
        String auth_secret =  params.containsKey("auth_secret")?(String) params.get("auth_secret"):"";

        if(auth_secret.equals("") || auth_secret.equals(tokenId) == false){
            return ReturnCode.FORBIDDEN_STATUS.toString();
        }
        if(tables.equals("") || owner.equals("")){
            return ReturnCode.RETURN_ERROR_STATUS.toString();
        }
        List<MLSQLAuthTable> authTablesInit = tableAuthService.fetchAuth(owner);
        Map<String, String> authTables = new HashMap<String, String>();
        List<Boolean> verifyAuth = new ArrayList<Boolean>();
        for(MLSQLAuthTable table : authTablesInit){
           String operateType = table.getOperateType();
           String key =  table.getDb() + "_" + table.getTableName() + "_" + table.getTableType() + "_" + table.getSourceType() + "_" +operateType;
           authTables.put(key, operateType);
        }

        JSONArray tableJson = JSONObject.parseArray(tables);
        List<MLSQLTable> tableLists = tableJson.toJavaList(MLSQLTable.class);
        for(MLSQLTable mlsqlTable : tableLists){
            String db = tableAuthService.unifyColumn(mlsqlTable.getDb());
            String table = tableAuthService.unifyColumn(mlsqlTable.getTable());
            String tableType = tableAuthService.unifyColumn(((Map<String, String>)mlsqlTable.getTableType()).get("name"));
            String SourceType = tableAuthService.unifyColumn(mlsqlTable.getSourceType());
            String key = db + "_"  + table + "_"  + tableType+ "_"  + SourceType;
            boolean verify = tableAuthService.checkAuth(key, mlsqlTable, home, authTables);
            verifyAuth.add(verify);
        }
        return JSONObject.toJSONString(verifyAuth);
    }

    @RequestMapping(value = "/team/tables", method = RequestMethod.POST)
    @ApiOperation(value = "获取组授权表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true)
    })
    public Message teamTables(@RequestParam(value = "teamName", required = true) String teamName){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        List<Map<String, Object>> teamTables = tableAuthService.fetchTables(mlsqlGroup);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"get team table list").addData("data", teamTables);
    }

    @RequestMapping(value = "/team/table/add", method = RequestMethod.POST)
    @ApiOperation(value = "组新增表授权", httpMethod = "POST")
    public Message teamTableAdd(@RequestBody MLSQLAuthTable mlsqlAuthTable){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(mlsqlAuthTable.getTeamName());
        if(mlsqlGroup == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        String teamTables = tableAuthService.addTableForTeam(mlsqlAuthTable, mlsqlGroup.getId());
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"team add table auth").addData("data", teamTables);
    }

    @RequestMapping(value = "/team/table/remove", method = RequestMethod.POST)
    @ApiOperation(value = "删除授权表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "表id", name = "tableId", dataType = "int", paramType = "query", required = true)
    })
    public Message teamTableRemove(@RequestParam(value = "teamName", required = true) String teamName,
                                    @RequestParam(value = "tableId", required = true) int tableId){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        String res = tableAuthService.removeTable(mlsqlGroup, tableId);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"remove table").addData("data", res);
    }

    @RequestMapping(value = "/role/table/add", method = RequestMethod.POST)
    @ApiOperation(value = "角色新增表授权", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "表id列表(多个逗号分隔)", name = "tableIds", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "操作列表(多个逗号分隔)", name = "operateTypes", dataType = "String", paramType = "query", required = true)
    })
    public Message roleTableAdd(@RequestParam(value = "teamName", required = true) String teamName,
                                 @RequestParam(value = "roleName", required = true) String roleName,
                                 @RequestParam(value = "tableIds", required = true) String tableIds,
                                 @RequestParam(value = "operateTypes", required = true) String operateTypes){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
        map.put("groupId", mlsqlGroup.getId());
        MlsqlGroupRole mlsqlGroupRole = teamRoleService.getGroupRole(map);
        if(mlsqlGroupRole == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_ROLE_NOT_EXISTS).addData("data", mlsqlGroupRole);
        }
        String res = tableAuthService.addTableForRole(mlsqlGroupRole.getId(), tableIds, operateTypes);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"add role table operator auth").addData("data", res);
    }

    @RequestMapping(value = "/role/table/remove", method = RequestMethod.POST)
    @ApiOperation(value = "角色移除表授权", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "表id", name = "tableId", dataType = "int", paramType = "query", required = true)
    })
    public Message roleTableRemove(@RequestParam(value = "teamName", required = true) String teamName,
                                @RequestParam(value = "roleName", required = true) String roleName,
                                @RequestParam(value = "tableId", required = true) int tableId){
        MlsqlGroup mlsqlGroup = teamRoleService.getGroupByName(teamName);
        if(mlsqlGroup == null){
            return success(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_NOT_EXISTS).addData("data", mlsqlGroup);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", roleName);
        map.put("groupId", mlsqlGroup.getId());
        MlsqlGroupRole mlsqlGroupRole = teamRoleService.getGroupRole(map);
        if(mlsqlGroupRole == null){
            return error(ReturnCode.RETURN_ERROR_STATUS, InterfaceReturnInformation.TEAM_ROLE_NOT_EXISTS).addData("data", mlsqlGroupRole);
        }
        String res = tableAuthService.removeRoleTable(mlsqlGroupRole.getId(), tableId);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"remove role table auth").addData("data", res);
    }

    @RequestMapping(value = "/role/tables", method = RequestMethod.POST)
    @ApiOperation(value = "获取授权表详细信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "组名", name = "teamName", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(value = "角色名", name = "roleName", dataType = "String", paramType = "query", required = true)
    })
    public Message roleTableRemove(@RequestParam(value = "teamName", required = true) String teamName,
                                   @RequestParam(value = "roleName", required = true) String roleName){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleName", roleName);
        map.put("teamName", teamName);
        List<Map<String, Object>> authTableDetail = tableAuthService.getAuthTableDetail(map);
        return success(ReturnCode.RETURN_SUCCESS_STATUS,"get table auth detail").addData("data", authTableDetail);
    }
    @RequestMapping(value = "/verify/column", method = RequestMethod.POST)
    @ApiOperation(value = "验证表是否授权接口", httpMethod = "POST")
    public String getVerifyAuthColumns(@RequestParam Map params) {
        String tables = params.containsKey("tables") ? (String) params.get("tables") : "";
        String owner = params.containsKey("owner") ? (String) params.get("owner") : "";
        String home = params.containsKey("home") ? (String) params.get("home") : "";
        String auth_secret = params.containsKey("auth_secret") ? (String) params.get("auth_secret") : "";
        if(auth_secret.equals("") || auth_secret.equals(tokenId) == false){
            return ReturnCode.FORBIDDEN_STATUS.toString();
        }
        if(tables.equals("") || owner.equals("")){
            return ReturnCode.RETURN_ERROR_STATUS.toString();
        }
        JSONObject tableJson = JSONObject.parseObject(tables);
        MLSQLTable mlsqlTable = tableJson.toJavaObject(MLSQLTable.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("db", mlsqlTable.getDb());
        map.put("tableName", mlsqlTable.getTable());
        map.put("userName", owner);
        return tableAuthService.getWithOutColumns(map);
    }

}
